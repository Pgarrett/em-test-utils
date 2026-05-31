# MCP Problem Type — Implementation Internals

This document is a deep-dive reference for developers implementing the MCP problem type.
It explains *how* each component works, not just *what* to build.

---

## 1. The overall data flow

Before going into individual components, it helps to see how they connect:

```
┌─────────────────────────────────────────────────────────────────────────┐
│  EvoMaster core (this process)                                          │
│                                                                         │
│   SearchAlgorithm (MIO/MOSA/…)                                          │
│     │                                                                   │
│     ├─ McpSampler.sample()          → McpIndividual (list of actions)  │
│     │                                                                   │
│     └─ McpFitness.calculateCoverage(individual)                         │
│           │                                                              │
│           │  for each McpCallAction:                                    │
│           │    1. registerNewAction(action, index)  ──────────────────┐ │
│           │    2. POST /mcp  {tools/call, args}     ──────────────────┤ │
│           │    3. parse response → McpCallResult                      │ │
│           │                                                            ▼ │
│           │  updateFitnessAfterEvaluation()  ◄── rc.getTestResults() ──┘ │
│           │    (collects bytecode branch coverage from SUT)             │
│           │                                                             │
│           └─ handleResponseTargets()                                    │
│                (adds MCP-specific fitness targets to FitnessValue)      │
│                                                                         │
│   Archive  ← stores best EvaluatedIndividuals per target               │
└─────────────────────────────────────────────────────────────────────────┘
           │ HTTP (JSON-RPC 2.0)                  │ REST (EM driver API)
           ▼                                      ▼
    MCP Server (SUT)                      EM Driver / SUT Controller
    (tools/call, tools/list)              (bytecode instrumentation,
                                           resetSUT, getTestResults)
```

Two separate HTTP connections are maintained:
- **MCP channel**: EvoMaster → MCP server, JSON-RPC 2.0 POST for tool calls
- **Driver channel**: EvoMaster → EM driver REST API, for lifecycle control and coverage collection

---

## 2. McpClient — the JSON-RPC 2.0 transport

### Wire format

Every call is a standard JSON-RPC 2.0 request sent as an HTTP POST to the MCP server's endpoint URL.
The request body is always `application/json`. The response body is also `application/json` and
must be read in full before processing — no streaming.

**Request envelope:**
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "params": {
    "name": "get_weather",
    "arguments": { "city": "London", "days": 3 }
  },
  "id": 7
}
```

The `id` field is an integer counter incremented per request. It is used to match responses to
requests — important because JSON-RPC allows batching, though we never use that.

**Success response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "content": [{ "type": "text", "text": "15°C, cloudy" }],
    "isError": false
  },
  "id": 7
}
```

Note the `isError` flag inside `result`. This is MCP-specific: even a successful HTTP 200 can
carry an application-level error. When `isError: true`, the tool ran but reported a failure
(e.g., "city not found"). This is distinct from a JSON-RPC protocol error.

**Tool-level error (application error):**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "content": [{ "type": "text", "text": "Unknown city: Foo" }],
    "isError": true
  },
  "id": 7
}
```

**Protocol-level error:**
```json
{
  "jsonrpc": "2.0",
  "error": {
    "code": -32602,
    "message": "Invalid params: 'city' is required"
  },
  "id": 7
}
```

Protocol errors use the `error` key (not `result`). The standard codes are:
- `-32700` Parse error
- `-32600` Invalid Request
- `-32601` Method not found
- `-32602` Invalid params
- `-32603` Internal error

### The initialize handshake

Before calling any tools, MCP requires a handshake. The client sends `initialize` and then
a `notifications/initialized` notification:

```json
// Step 1: initialize request
{
  "jsonrpc": "2.0",
  "method": "initialize",
  "params": {
    "protocolVersion": "2024-11-05",
    "capabilities": {},
    "clientInfo": { "name": "evomaster", "version": "1.0.0" }
  },
  "id": 1
}

// Step 2: notifications/initialized (no id = notification, no response expected)
{ "jsonrpc": "2.0", "method": "notifications/initialized" }
```

The server responds to step 1 with its capabilities (which tools it supports, protocol version,
server name). We only need to store the server's protocol version for logging; we don't gate any
behaviour on its capabilities.

### Tool discovery

```json
// Request
{ "jsonrpc": "2.0", "method": "tools/list", "id": 2 }

// Response
{
  "jsonrpc": "2.0",
  "result": {
    "tools": [
      {
        "name": "get_weather",
        "description": "Get weather forecast for a city",
        "inputSchema": {
          "type": "object",
          "properties": {
            "city":  { "type": "string" },
            "units": { "type": "string", "enum": ["celsius", "fahrenheit"] },
            "days":  { "type": "integer", "minimum": 1, "maximum": 7 }
          },
          "required": ["city"]
        }
      }
    ]
  },
  "id": 2
}
```

The `inputSchema` is a JSON Schema object. Its top level is always `type: object`. The properties
inside it are what we convert to Genes.

### McpClient design

```kotlin
class McpClient(private val cfg: McpClientConfig) {

    private var requestId = AtomicInteger(1)

    // Sends a JSON-RPC POST and returns the parsed response node.
    // Throws McpTransportException on non-2xx or network error.
    private fun send(method: String, params: Any?): JsonNode

    fun initialize(): McpInitializeResult {
        val result = send("initialize", mapOf(
            "protocolVersion" to "2024-11-05",
            "capabilities" to emptyMap<String, Any>(),
            "clientInfo" to mapOf("name" to "evomaster", "version" to "1.0.0")
        ))
        // fire-and-forget notification
        sendNotification("notifications/initialized")
        return McpInitializeResult(result)
    }

    fun listTools(): List<McpToolSchemaDto> {
        val result = send("tools/list", null)
        return parseToolList(result)
    }

    fun callTool(name: String, arguments: Map<String, Any?>): McpToolCallResultDto {
        val result = send("tools/call", mapOf("name" to name, "arguments" to arguments))
        // result is a JsonNode; check for protocol error vs result
        return parseToolCallResult(result)
    }

    fun shutdown() {
        // MCP shutdown: send shutdown request, then exit notification
        send("shutdown", null)
        sendNotification("notifications/exit")
    }
}
```

Use OkHttp + Jackson, both already on the classpath. Set a `timeoutMs` from `McpClientConfig`
(default 30 seconds). The client is **not** thread-safe — it is owned by `McpSampler` and used
from the single fitness evaluation thread.

### McpToolCallResultDto

When parsing the response from `tools/call`, we produce:

```kotlin
data class McpToolCallResultDto(
    val isToolError: Boolean,   // true if result.isError == true
    val isProtocolError: Boolean, // true if the response has an "error" key (not "result")
    val errorCode: Int?,        // JSON-RPC error code, only when isProtocolError
    val errorMessage: String?,
    val contentJson: String?    // raw JSON of result.content[], for assertions
)
```

The distinction between `isToolError` and `isProtocolError` maps directly to
`McpCallResultCategory`:
- `isProtocolError && errorCode == -32602` → `INVALID_PARAMS`
- `isProtocolError && errorCode == -32603` → `INTERNAL_ERROR`
- `isProtocolError` (other codes) → `TOOL_ERROR`
- `isToolError` → `TOOL_ERROR`
- neither → `SUCCESS`
- HTTP failure → `TRANSPORT_ERROR` (thrown as `McpTransportException`, caught in `McpFitness`)

---

## 3. McpEndpointsHandler — from tool schema to actionCluster

### Responsibility

`McpEndpointsHandler` is the bridge between the MCP server's tool descriptions and EvoMaster's
internal search representation. Its main job is to convert each tool's JSON Schema into a
`McpCallAction` with a gene tree, and register that action in the `actionCluster`.

It is analogous to `RPCEndpointsHandler`, which converts Thrift/gRPC IDL into `RPCCallAction`s.

### initActionCluster

```kotlin
fun initActionCluster(
    problem: McpProblemDto,
    actionCluster: MutableMap<String, Action>,
    tools: List<McpToolSchemaDto>   // already fetched by McpSampler via McpClient.listTools()
) {
    for (tool in tools) {
        val params = buildParams(tool.name, tool.inputSchemaJson)
        val action = McpCallAction(
            toolName = tool.name,
            id = tool.name,
            inputParameters = params
        )
        actionCluster[tool.name] = action
    }
}
```

The `actionCluster` is keyed by tool name. Tool names must be unique within an MCP server (this
is guaranteed by the MCP spec). If two tools have the same name (which shouldn't happen), the
last one wins.

### JSON Schema → Gene conversion

The top level of `inputSchema` is always `{ "type": "object", "properties": {...}, "required": [...] }`.
We iterate over `properties` and build one `McpParam` per property.

```kotlin
fun buildParams(toolName: String, schemaJson: String?): MutableList<Param> {
    if (schemaJson == null) return mutableListOf()

    val schema = objectMapper.readTree(schemaJson)
    val properties = schema.get("properties") ?: return mutableListOf()
    val required = schema.get("required")?.map { it.asText() }?.toSet() ?: emptySet()

    return properties.fields().asSequence().map { (name, propSchema) ->
        val gene = schemaToGene(name, propSchema)
        val wrapped = if (name in required) gene else OptionalGene(name, gene)
        McpParam(name, wrapped)
    }.toMutableList()
}
```

**`schemaToGene` — the recursive converter:**

```kotlin
fun schemaToGene(name: String, schema: JsonNode): Gene {
    val type = schema.get("type")?.asText()

    // enum takes priority over type
    if (schema.has("enum")) {
        val values = schema.get("enum").map { it.asText() }
        return EnumGene(name, values)
    }

    return when (type) {
        "string"  -> StringGene(name)
        "integer" -> {
            val min = schema.get("minimum")?.asInt() ?: Int.MIN_VALUE / 1000
            val max = schema.get("maximum")?.asInt() ?: Int.MAX_VALUE / 1000
            IntegerGene(name, min = min, max = max)
        }
        "number"  -> {
            val min = schema.get("minimum")?.asDouble() ?: Double.MIN_VALUE
            val max = schema.get("maximum")?.asDouble() ?: Double.MAX_VALUE
            DoubleGene(name, min = min, max = max)
        }
        "boolean" -> BooleanGene(name)
        "array"   -> {
            val itemsSchema = schema.get("items") ?: return StringGene(name)  // fallback
            val template = schemaToGene("element", itemsSchema)
            ArrayGene(name, template)
        }
        "object"  -> {
            val innerParams = buildInnerObject(name, schema)
            ObjectGene(name, innerParams)
        }
        "null"    -> NullGene(name)
        else      -> {
            log.warn("Unsupported JSON Schema type '{}' for param '{}' in tool", type, name)
            StringGene(name)   // best-effort fallback
        }
    }
}
```

Key decisions:
- **`enum` before `type`**: some schemas define both `"type": "string"` and `"enum"`, so check `enum` first
- **Missing `minimum`/`maximum`**: use safe defaults (divide MIN/MAX_VALUE by 1000 to avoid overflow in mutations)
- **Unsupported types**: log a warning and fall back to `StringGene`. Never crash — a degraded test is better than none
- **`required` vs optional**: wrapping in `OptionalGene` lets the search explore calling the tool with and without each optional parameter

### Gene serialization (action → arguments map)

When `McpFitness` executes an action, it needs to convert the current gene values back to the
`Map<String, Any?>` that `McpClient.callTool()` expects.

```kotlin
fun geneToValue(gene: Gene): Any? {
    return when (gene) {
        is OptionalGene  -> if (gene.isPresent) geneToValue(gene.gene) else null
        is StringGene    -> gene.value
        is IntegerGene   -> gene.value
        is DoubleGene    -> gene.value
        is BooleanGene   -> gene.value
        is EnumGene<*>   -> gene.value.toString()
        is ArrayGene<*>  -> gene.getAllElements().map { geneToValue(it) }
        is ObjectGene    -> gene.fields.associate { it.name to geneToValue(it) }
        is NullGene      -> null
        else             -> gene.getValueAsRawString()
    }
}

fun actionToArguments(action: McpCallAction): Map<String, Any?> {
    return action.inputParameters
        .filter { param ->
            // exclude optional params whose gene is absent
            val g = param.gene
            g !is OptionalGene || g.isPresent
        }
        .associate { param ->
            param.name to geneToValue(param.gene)
        }
        .filterValues { it != null }   // skip absent optionals
}
```

---

## 4. McpSampler — how test cases are generated

### What the sampler owns

```kotlin
class McpSampler : ApiWsSampler<McpIndividual>() {

    @Inject lateinit var configuration: EMConfig
    @Inject lateinit var mcpEndpointsHandler: McpEndpointsHandler

    // The MCP client is created in initialize() and owned for the lifetime of the search
    private lateinit var mcpClient: McpClient

    // One individual per tool × auth combination, drained at the start of the search
    private val adHocInitialIndividuals: MutableList<McpIndividual> = mutableListOf()
}
```

The `actionCluster` (`MutableMap<String, Action>`) is inherited from `Sampler`. It acts as the
"template library": we never execute actions from `actionCluster` directly; we always `.copy()`
them first and then randomize the copy.

### @PostConstruct initialize()

This runs once after Guice injection completes, before the search starts.

```kotlin
@PostConstruct fun initialize() {
    rc.checkConnection()

    val started = rc.startSUT()
    if (!started) throw SutProblemException("Failed to start the SUT")

    val infoDto = rc.getSutInfo()
        ?: throw SutProblemException("Failed to get SUT info")

    val problem = infoDto.mcpProblem
        ?: throw IllegalStateException("mcpProblem is null in SutInfoDto")

    // Create the MCP client (no calls yet)
    mcpClient = McpClient(McpClientConfig(
        serverUrl = problem.serverUrl,
        authToken = problem.authToken,
        timeoutMs = 30_000
    ))

    // MCP handshake
    mcpClient.initialize()

    // Discover tools and build actionCluster
    val tools = if (problem.tools != null) problem.tools   // driver pre-provided them
                else mcpClient.listTools()                  // discover at runtime
    mcpEndpointsHandler.initActionCluster(problem, actionCluster, tools)

    if (actionCluster.isEmpty()) {
        throw SutProblemException("MCP server exposes no tools")
    }

    // DB setup (for SUTs that also use a database)
    initSqlInfo(infoDto)

    // Build the ad-hoc queue (one test per tool)
    initAdHocInitialIndividuals()

    updateConfigBasedOnSutInfoDto(infoDto)

    log.debug("McpSampler initialized: {} tools discovered", actionCluster.size)
}
```

**Why `problem.tools` as an optional override?** When the SUT controller already knows the tool
list (e.g., because the MCP server is embedded in the same JVM), it can send the schema directly
rather than requiring an extra round-trip to `tools/list`. This is an optimisation, not a
requirement.

### sampleAtRandom()

```kotlin
override fun sampleAtRandom(): McpIndividual {
    val len = randomness.nextInt(1, config.maxTestSize)

    val actions: MutableList<ActionComponent> = (0 until len).map {
        val action = sampleRandomAction()
        EnterpriseActionGroup(mutableListOf(action), McpCallAction::class.java)
    }.toMutableList()

    val ind = createMcpIndividual(SampleType.RANDOM, actions)
    ind.doGlobalInitialize(searchGlobalState)   // wires up the gene tree's parent references
    return ind
}
```

**`doGlobalInitialize`** must always be called after constructing a new individual. It walks the
entire gene tree and sets up internal cross-references (parent pointers, taint tracking, global
state hooks). Without it, mutation and taint analysis will malfunction.

### sampleRandomAction()

```kotlin
fun sampleRandomAction(): McpCallAction {
    val action = randomness.choose(actionCluster.values).copy() as McpCallAction
    action.doInitialize(randomness)   // randomizes all gene values
    action.forceNewTaints()           // assigns fresh taint IDs for taint analysis
    return action
}
```

**`doInitialize(randomness)`** is defined on `Gene` and called transitively on all genes in the
action. It sets each gene to a random valid value:
- `StringGene`: random string of length 0–16 (or a taint value if taint analysis is enabled)
- `IntegerGene`: random integer in [min, max]
- `DoubleGene`: random double in [min, max]
- `BooleanGene`: random true/false
- `EnumGene`: random element from the enum list
- `OptionalGene`: randomly decides to be present or absent (probability based on APC)
- `ObjectGene`/`ArrayGene`: recursively initializes all nested genes

**`forceNewTaints()`** assigns fresh identifiers to any `StringGene` that participates in taint
analysis. Taint analysis tracks which string inputs were used in comparisons inside the SUT, and
uses that to generate more targeted string values in subsequent iterations.

### smartSample() and ad-hoc individuals

```kotlin
override fun smartSample(): McpIndividual {
    if (adHocInitialIndividuals.isNotEmpty()) {
        return adHocInitialIndividuals.removeAt(adHocInitialIndividuals.lastIndex)
    }
    return sampleAtRandom()
}
```

The search algorithm calls `sample()` (defined in `Sampler`), which in turn calls `smartSample()`
or `sampleAtRandom()` depending on whether there are seeded individuals and the search phase.

**The ad-hoc queue** is the most important source of early-search diversity. It ensures that every
tool gets tried with at least one fully-initialized call before the random search takes over.

```kotlin
private fun initAdHocInitialIndividuals() {
    adHocInitialIndividuals.clear()

    for ((_, templateAction) in actionCluster) {
        val action = templateAction.copy() as McpCallAction
        action.doInitialize(randomness)
        action.forceNewTaints()

        val group = EnterpriseActionGroup(mutableListOf(action), McpCallAction::class.java)
        val ind = createMcpIndividual(SampleType.SMART, mutableListOf(group))
        ind.doGlobalInitialize(searchGlobalState)
        adHocInitialIndividuals.add(ind)
    }

    // Shuffle so they are not always drained in the same order
    adHocInitialIndividuals.shuffle(randomness.getRandom())
}
```

With 10 tools and no auth variants, this produces 10 individuals. Compare to `RPCSampler` which
also generates auth-variant combinations — for MCP, auth is per-request (Bearer token in header),
not per-action, so we start simpler.

### createMcpIndividual()

```kotlin
private fun createMcpIndividual(
    sampleType: SampleType,
    actions: MutableList<ActionComponent>,
    sqlSize: Int = 0
): McpIndividual {
    return McpIndividual(
        sampleType = sampleType,
        trackOperator = if (config.trackingEnabled()) this else null,
        index = if (config.trackingEnabled()) time.evaluatedIndividuals else -1,
        allActions = actions,
        mainSize = actions.size - sqlSize,
        sqlSize = sqlSize
    )
}
```

`trackOperator` and `index` are for EvoMaster's genealogy tracking feature, which records how
each individual was created and mutated. It is optional and controlled by `config.trackingEnabled()`.

---

## 5. McpIndividual — the test case

`McpIndividual` is just a list of `McpCallAction`s (wrapped in `EnterpriseActionGroup`s), plus
optional SQL initialization actions at the front.

The most important things it provides:
- `seeMainExecutableActions(): List<McpCallAction>` — the tool calls in order
- `addAction(position, action)` and `removeAction(position)` — used by `McpStructureMutator`
- `copyContent()` — deep copy (required by the search; the archive stores copies of good individuals)
- `canMutateStructure(): Boolean` — always `true` for MCP (structure mutation is always enabled)

The group layout mirrors `RPCIndividual` exactly:

```
children:
  [SqlAction, SqlAction, ...]           ← INITIALIZATION_SQL group
  [EnterpriseActionGroup<McpCallAction>, ...]  ← MAIN group
```

Each `EnterpriseActionGroup` wraps a single `McpCallAction` plus any external service stubs (empty
for MCP initially).

---

## 6. McpFitness — evaluating a test case

### The single method to implement: `doCalculateCoverage`

`FitnessFunction.doCalculateCoverage` is abstract. It is called by the framework-managed
`calculateCoverage` wrapper, which handles retry logic and timing.

```kotlin
override fun doCalculateCoverage(
    individual: McpIndividual,
    targets: Set<Int>,
    allTargets: Boolean,
    fullyCovered: Boolean,
    descriptiveIds: Boolean,
): EvaluatedIndividual<McpIndividual>? {

    rc.resetSUT()   // (1) reset SUT state between test cases

    val actionResults = mutableListOf<ActionResult>()

    // (2) run any SQL setup actions first
    doDbCalls(individual.seeInitializingActions().filterIsInstance<SqlAction>(), actionResults)

    val fv = FitnessValue(individual.size().toDouble())

    // (3) execute each tool call in sequence
    var stopped = false
    individual.seeMainExecutableActions().forEachIndexed { index, action ->
        if (stopped) return@forEachIndexed
        stopped = !executeAction(action, index, actionResults)
    }

    // (4) collect bytecode branch/line coverage from the SUT controller
    val dto = updateFitnessAfterEvaluation(targets, allTargets, fullyCovered, descriptiveIds, individual, fv)
        ?: return null   // null means the SUT is unresponsive; caller will retry

    // (5) handle SQL heuristics etc.
    handleExtra(dto, fv)

    // (6) add MCP-specific response targets
    val mcpResults = actionResults.filterIsInstance<McpCallResult>()
    if (mcpResults.isNotEmpty()) {
        handleResponseTargets(fv, individual.seeMainExecutableActions(), mcpResults, dto.additionalInfoList)
    }

    return EvaluatedIndividual(
        fv, individual.copy() as McpIndividual, actionResults,
        trackOperator = individual.trackOperator,
        index = time.evaluatedIndividuals,
        config = config
    )
}
```

### Step 1: rc.resetSUT()

The remote controller resets the SUT to a clean state. For a Spring Boot server this might mean
rolling back the database. For an MCP server it means whatever the SUT controller's
`resetStateOfSUT()` implementation does.

### Step 3: executeAction()

```kotlin
private fun executeAction(
    action: McpCallAction,
    index: Int,
    actionResults: MutableList<ActionResult>
): Boolean {

    searchTimeController.waitForRateLimiter()   // respects --ratePerMinute config

    val result = McpCallResult(action.getLocalId())
    actionResults.add(result)

    // IMPORTANT: tell the SUT which action we are about to execute.
    // This lets the bytecode instrumentation associate coverage data
    // with the correct action index.
    registerNewAction(action, index)

    val arguments = mcpEndpointsHandler.actionToArguments(action)

    try {
        val response = mcpClient.callTool(action.toolName, arguments)

        result.category = when {
            response.isProtocolError && response.errorCode == -32602 -> McpCallResultCategory.INVALID_PARAMS
            response.isProtocolError && response.errorCode == -32603 -> McpCallResultCategory.INTERNAL_ERROR
            response.isProtocolError -> McpCallResultCategory.TOOL_ERROR
            response.isToolError     -> McpCallResultCategory.TOOL_ERROR
            else                     -> McpCallResultCategory.SUCCESS
        }
        result.contentJson = response.contentJson
        result.errorCode = response.errorCode
        result.errorMessage = response.errorMessage

    } catch (e: McpTransportException) {
        result.category = McpCallResultCategory.TRANSPORT_ERROR
        result.stopping = true
        return false
    }

    return true
}
```

**`registerNewAction(action, index)` is critical.** It is a REST call to the EM driver that says
"action N is starting now". The bytecode instrumentation uses this to tag every branch it observes
with the action index. When `updateFitnessAfterEvaluation` later asks for coverage, the controller
returns `additionalInfoList[i]` for the i-th action — including the last executed statement, which
is used as the fault location for internal errors.

**`stopping = true` on transport failure** causes `doCalculateCoverage` to stop executing further
actions in this individual. There is no point executing subsequent tool calls if the server is
unreachable.

### Step 4: updateFitnessAfterEvaluation()

This is inherited from `EnterpriseFitness`. It:
1. Calls `targetsToEvaluate(targets, individual)` to decide which coverage targets to request
   (capped at 100 to keep the response small)
2. Calls `rc.getTestResults(ids)` to get coverage data from the SUT
3. For each `TargetInfoDto` returned, calls `fv.updateTarget(id, value, actionIndex)` to record
   the coverage score

The returned `TestResultsDto` also contains `additionalInfoList` — one entry per action. Each
entry has `lastExecutedStatement`, which is the descriptive name of the last bytecode statement
the SUT executed before the action finished. This is the key piece for fault localisation.

### Step 6: handleResponseTargets()

This is the domain-specific part. It adds fitness targets that are not derivable from bytecode
coverage — targets based on what the MCP server returned.

```kotlin
private fun handleResponseTargets(
    fv: FitnessValue,
    actions: List<McpCallAction>,
    results: List<McpCallResult>,
    additionalInfoList: List<AdditionalInfoDto>
) {
    results.indices.forEach { i ->
        val name = actions[i].toolName
        val result = results[i]
        val lastStatement = additionalInfoList.getOrNull(i)?.lastExecutedStatement
            ?: McpFitness.MCP_DEFAULT_FAULT_CODE

        handleActionTargets(fv, name, result, i, lastStatement)
    }
}

private fun handleActionTargets(
    fv: FitnessValue,
    toolName: String,
    result: McpCallResult,
    actionIndex: Int,
    lastStatement: String
) {
    val successId = idMapper.handleLocalTarget("MCP_SUCCESS:$toolName")
    val toolErrorId = idMapper.handleLocalTarget(
        idMapper.getFaultDescriptiveId(ExperimentalFaultCategory.MCP_TOOL_ERROR, toolName))

    when (result.category) {
        SUCCESS -> {
            fv.updateTarget(successId, 1.0, actionIndex)
            fv.updateTarget(toolErrorId, 0.5, actionIndex)
        }
        TOOL_ERROR -> {
            fv.updateTarget(successId, 0.5, actionIndex)
            fv.updateTarget(toolErrorId, 1.0, actionIndex)
            result.setLastStatementForInternalError(lastStatement)
        }
        INVALID_PARAMS -> {
            // Reward finding a set of inputs that triggers parameter validation
            val invalidId = idMapper.handleLocalTarget(
                idMapper.getFaultDescriptiveId(ExperimentalFaultCategory.MCP_INVALID_PARAMS, toolName))
            fv.updateTarget(invalidId, 1.0, actionIndex)
        }
        INTERNAL_ERROR -> {
            fv.updateTarget(successId, 0.5, actionIndex)
            val postfix = "$lastStatement $toolName"
            val bugId = idMapper.handleLocalTarget(
                idMapper.getFaultDescriptiveId(ExperimentalFaultCategory.MCP_INTERNAL_ERROR, postfix))
            fv.updateTarget(bugId, 1.0, actionIndex)
            result.setLastStatementForInternalError(lastStatement)
        }
        TRANSPORT_ERROR -> { /* no reward — we can't learn from a transport failure */ }
    }
}
```

### How idMapper targets work

`idMapper.handleLocalTarget(descriptiveId: String): Int`

This converts a human-readable string like `"MCP_SUCCESS:get_weather"` into a stable integer ID.
The mapping is stored in a concurrent map inside `IdMapper`. Once a string is registered, the
same integer is returned for all subsequent calls with the same string. The integer is what
`FitnessValue.updateTarget(id, score, actionIndex)` operates on.

`IdMapper.getFaultDescriptiveId(category, postfix)` formats the string as:
`"${category.testCaseLabel}:$postfix"` — e.g., `"causesMcpInternalError:get_weather"`.

This string ends up in the generated test case name and in the console output, so keep it human-readable.

### FitnessValue and scores

`FitnessValue.updateTarget(id, score, actionIndex)` records that target `id` was reached with
score `score` by action at position `actionIndex`.

Score semantics:
- **1.0** — fully covered / fault triggered
- **0.5** — partially covered (we reached the vicinity but not the exact target)
- **0.0** — not covered
- Between 0 and 1 — heuristic distance (used for branch coverage: how close we were to flipping a branch)

The Archive stores the best individual seen for each target ID. If an individual scores 1.0 for
a target that was previously only at 0.5, the archive updates. This is the core of MIO's coverage
maximization.

---

## 7. ExperimentalFaultCategory — adding MCP entries

Add these to `ExperimentalFaultCategory.kt`. Codes 945–949 are available (RPC uses 940–944):

```kotlin
// 5xx: MCP
MCP_INTERNAL_ERROR(945, "MCP Internal Error", "causesMcpInternalError",
    "The MCP server returned a JSON-RPC internal error (-32603)"),
MCP_TOOL_ERROR(946, "MCP Tool Error", "causesMcpToolError",
    "The MCP tool returned isError: true in its result"),
MCP_INVALID_PARAMS(947, "MCP Invalid Params", "causesMcpInvalidParams",
    "The MCP server returned a JSON-RPC invalid params error (-32602)"),
```

The `code` (945 etc.) is used for classifying faults in the final test report.
The `testCaseLabel` appears in generated test method names — keep it a valid Java identifier.

---

## 8. McpStructureMutator — add/remove tool calls

Structure mutation changes the *shape* (number/order of actions) of an individual, as opposed to
gene mutation which changes the *values* within actions.

```kotlin
override fun mutateStructure(
    individual: Individual,
    evaluatedIndividual: EvaluatedIndividual<*>,
    mutatedGenes: MutatedGeneSpecification?,
    targets: Set<Int>
) {
    if (individual !is McpIndividual) throw IllegalArgumentException(...)
    if (!individual.canMutateStructure()) return
    if (config.maxTestSize == 1) return

    mutateForRandom(individual, mutatedGenes)

    if (config.trackingEnabled()) tag(individual, time.evaluatedIndividuals)
}

private fun mutateForRandom(individual: McpIndividual, mutatedGenes: MutatedGeneSpecification?) {
    val size = individual.seeMainExecutableActions().size

    if (size + 1 < config.maxTestSize && (size <= 1 || randomness.nextBoolean())) {
        // ADD: append a new tool call
        val newAction = sampler.sampleRandomAction()
        individual.addAction(action = newAction)
        mutatedGenes?.addRemovedOrAddedByAction(newAction, individual.seeFixedMainActions().indexOf(newAction), null, false, size)
    } else {
        // REMOVE: delete a random existing action
        val chosen = randomness.nextInt(size)
        val removed = individual.seeMainExecutableActions()[chosen]
        mutatedGenes?.addRemovedOrAddedByAction(removed, individual.seeFixedMainActions().indexOf(removed), null, true, size)
        individual.removeAction(chosen)
    }
}

override fun addInitializingActions(individual: EvaluatedIndividual<*>, mutatedGenes: MutatedGeneSpecification?) {
    addInitializingActions(individual, mutatedGenes, sampler)   // inherited from ApiWsStructureMutator
}

override fun getSqlInsertBuilder(): SqlInsertBuilder? = sampler.sqlInsertBuilder
```

**Why structure mutation matters for MCP**: Some tools have preconditions. For example, a
`delete_item` tool may only succeed if `create_item` was called first. Structure mutation allows
the search to discover that a sequence of `[create_item, delete_item]` exercises more code than
a single `delete_item` call. The archive rewards the longer individual if it covers new targets.

The add/remove probabilities here are a simple baseline. Smarter strategies (e.g., preferring to
add tools that were seen to produce data consumed by subsequent tools) can be added later.

---

## 9. Gene mutation — the part you don't have to write

Gene-level mutation (changing the *values* of parameters) is handled entirely by
`StandardMutator<McpIndividual>`, which is bound in `McpModule` just like in `RPCModule`:

```kotlin
bind(object : TypeLiteral<Mutator<McpIndividual>>() {})
    .to(object : TypeLiteral<StandardMutator<McpIndividual>>() {})
    .asEagerSingleton()
```

`StandardMutator` uses `AdaptiveParameterControl` (APC) to decide how many genes to mutate per
iteration. Each selected gene has `mutate(randomness, apc)` called on it:

| Gene type | Mutation behaviour |
|---|---|
| `StringGene` | insert/delete/replace chars, or switch to taint value, or random |
| `IntegerGene` | delta: ±1, ±small random, ±large random, or random in range |
| `DoubleGene` | similar to IntegerGene |
| `BooleanGene` | flip |
| `EnumGene` | pick a different enum value |
| `OptionalGene` | toggle presence (absent ↔ present), or mutate inner gene if present |
| `ArrayGene` | add/remove elements, or mutate existing elements |
| `ObjectGene` | pick a random field and mutate it |

You do not need to implement any of this — it comes for free as long as you build the gene tree
correctly in `McpEndpointsHandler`.

---

## 10. McpCallResult — recording what happened

```kotlin
class McpCallResult(actionId: String) : ActionResult(actionId) {

    companion object {
        const val CATEGORY_KEY = "MCP_CATEGORY"
        const val CONTENT_KEY  = "MCP_CONTENT"
        const val ERROR_CODE_KEY = "MCP_ERROR_CODE"
        const val LAST_STATEMENT_KEY = "MCP_LAST_STATEMENT"
    }

    var category: McpCallResultCategory
        get() = McpCallResultCategory.valueOf(getResultValue(CATEGORY_KEY) ?: "TRANSPORT_ERROR")
        set(v) { addResultValue(CATEGORY_KEY, v.name) }

    var contentJson: String?
        get() = getResultValue(CONTENT_KEY)
        set(v) { if (v != null) addResultValue(CONTENT_KEY, v) }

    var errorCode: Int?
        get() = getResultValue(ERROR_CODE_KEY)?.toIntOrNull()
        set(v) { if (v != null) addResultValue(ERROR_CODE_KEY, v.toString()) }

    fun setLastStatementForInternalError(statement: String) {
        addResultValue(LAST_STATEMENT_KEY, statement)
    }

    fun getLastStatement(): String? = getResultValue(LAST_STATEMENT_KEY)
}
```

`ActionResult` stores results as a `Map<String, String>` internally. Use string keys and string
values; that is the convention across the codebase (see `RPCCallResult`).

`stopping = true` (inherited from `ActionResult`) tells `doCalculateCoverage` to stop executing
the remaining actions in this individual. Set it when the MCP server is unreachable.

---

## 11. McpModule — the Guice wiring

The module must bind every generic and concrete type. The double-binding pattern (once typed,
once wildcard) is a Guice requirement for parameterised types:

```kotlin
class McpModule : EnterpriseModule() {

    override fun configure() {
        // Sampler: three bindings (typed, wildcard, concrete)
        bind(object : TypeLiteral<EnterpriseSampler<McpIndividual>>() {}).to(McpSampler::class.java).asEagerSingleton()
        bind(object : TypeLiteral<Sampler<McpIndividual>>() {}).to(McpSampler::class.java).asEagerSingleton()
        bind(object : TypeLiteral<Sampler<*>>() {}).to(McpSampler::class.java).asEagerSingleton()
        bind(McpSampler::class.java).asEagerSingleton()

        // Fitness
        bind(object : TypeLiteral<FitnessFunction<McpIndividual>>() {}).to(McpFitness::class.java).asEagerSingleton()
        bind(object : TypeLiteral<FitnessFunction<*>>() {}).to(McpFitness::class.java).asEagerSingleton()

        // Archive
        bind(object : TypeLiteral<Archive<McpIndividual>>() {}).asEagerSingleton()
        bind(object : TypeLiteral<Archive<*>>() {}).to(object : TypeLiteral<Archive<McpIndividual>>() {})
        bind(Archive::class.java).to(object : TypeLiteral<Archive<McpIndividual>>() {})

        // Minimizer
        bind(object : TypeLiteral<Minimizer<McpIndividual>>() {}).asEagerSingleton()
        bind(object : TypeLiteral<Minimizer<*>>() {}).to(object : TypeLiteral<Minimizer<McpIndividual>>() {}).asEagerSingleton()

        // Flakiness detector
        bind(object : TypeLiteral<FlakinessDetector<McpIndividual>>() {}).asEagerSingleton()
        bind(object : TypeLiteral<FlakinessDetector<*>>() {}).to(object : TypeLiteral<FlakinessDetector<McpIndividual>>() {}).asEagerSingleton()

        // Mutator (use the generic standard mutator — no custom gene mutation needed)
        bind(object : TypeLiteral<Mutator<McpIndividual>>() {}).to(object : TypeLiteral<StandardMutator<McpIndividual>>() {}).asEagerSingleton()

        // Structure mutator
        bind(StructureMutator::class.java).to(McpStructureMutator::class.java).asEagerSingleton()

        // Domain-specific handler
        bind(McpEndpointsHandler::class.java).asEagerSingleton()

        // Remote controller
        bind(RemoteController::class.java).to(RemoteControllerImplementation::class.java).asEagerSingleton()

        // Test output
        bind(TestCaseWriter::class.java).to(McpTestCaseWriter::class.java).asEagerSingleton()
        bind(TestSuiteWriter::class.java).asEagerSingleton()
    }
}
```

`asEagerSingleton()` means the object is created at injection time, not lazily. This ensures
that any `@PostConstruct` methods (like `McpSampler.initialize()`) run during startup, before
the search begins.

---

## 12. Generated test output

`McpTestCaseWriter` extends `TestCaseWriter`. For Java output, each test method should produce
code like:

```java
@Test
public void test_0() throws Exception {
    // MCP tool call: get_weather
    Map<String, Object> args0 = new LinkedHashMap<>();
    args0.put("city", "London");
    args0.put("days", 3);
    McpResponse res0 = McpTestUtils.callTool(MCP_URL, "get_weather", args0);
    assertFalse("Tool should not return an error", res0.isError);
}
```

`McpTestUtils` is a helper class emitted alongside the tests. It should be self-contained:
no external MCP SDK dependency — just OkHttp + Jackson, which are also on the test classpath.
It handles the JSON-RPC envelope construction, so the generated tests stay readable.

For fault-detecting tests (where `McpCallResult.category != SUCCESS`), the assertion should
*verify the fault is still present*, not just that no error occurred:

```java
// Test that detected an internal error — assert the server still fails on this input
McpResponse res0 = McpTestUtils.callTool(MCP_URL, "divide", args0);
assertTrue("Should cause an internal error", res0.isError || res0.isProtocolError);
```

---

## 13. Quick reference: what to implement vs what you get for free

| Concern | Write yourself | Inherited / automatic |
|---|---|---|
| JSON-RPC HTTP transport | `McpClient` | — |
| JSON Schema → Gene | `McpEndpointsHandler.schemaToGene` | Gene types (StringGene etc.) |
| Tool call serialization | `McpEndpointsHandler.actionToArguments` | — |
| Random test generation | `McpSampler.sampleAtRandom`, `sampleRandomAction` | `actionCluster`, `doInitialize` |
| Ad-hoc queue | `initAdHocInitialIndividuals` | — |
| Executing tool calls | `McpFitness.executeAction` | `registerNewAction`, `waitForRateLimiter` |
| Bytecode coverage | — | `updateFitnessAfterEvaluation`, whole bytecode stack |
| Response fitness targets | `McpFitness.handleResponseTargets` | `idMapper.handleLocalTarget`, `fv.updateTarget` |
| Gene-level mutation | — | `StandardMutator` |
| Structure mutation | `McpStructureMutator.mutateForRandom` | `addInitializingActions` |
| Test code generation | `McpTestCaseWriter` | `TestCaseWriter` base, `TestSuiteWriter` |
| Guice wiring | `McpModule.configure()` | `EnterpriseModule` base bindings |
| Archive management | — | `Archive<McpIndividual>` |
| Search algorithm | — | MIO/MOSA wired in `Main.kt` |
