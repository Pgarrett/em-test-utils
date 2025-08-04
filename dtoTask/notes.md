# Pending features
* Support arrays as payloads
* Support for `additionalFields`
* Support for Kotlin as DTO output


# Pending discussion
1. A request body might reference different/multiple schemas by the usage of `oneOf`/`anyOf`/`allOf`. How do we handle those scenarios? Top of mind: `allOf` are merged into a single object, `oneOf` creates separate objects, `anyOf` are merged into a single object. My main issue here is: how can we guarantee we're generating spec compliant payloads? Example: 
```yaml
schema:
  anyOf:
    - type: object
      required: [email]
      properties:
        email:
          type: string
        active:
          type: boolean

    - type: object
      required: [phone]
      properties:
        phone:
          type: string
        number:
          type: integer
```

Where payload
```json
{ "active": true, "number": 1234 }
```
is not valid.

2. Do we want to check on `additionalProperties`? For situations such as:
```yaml
schema:
  anyOf:
    - type: object
      required: [email]
      properties:
        email:
          type: string
        active:
          type: boolean

    - type: object
      required: [phone]
      properties:
        phone:
          type: string
```

Where payload
```json
{ "active": true, "phone": "123-456-7890" }
```
is valid unless the second object is defined as:
```yaml
- type: object
  required: [phone]
  additionalProperties: false
  properties:
    phone:
      type: string
```

3. Regarding naming of these situations: No issue here when the schema being referenced is a schema in the `components` section. Problem is when schemas are being defined inline since we're using the action name: `POST:/example` to name the DTO. Example:
```yaml
schema:
  anyOf:
    - type: object
      required: [email]
      properties:
        email:
          type: string
    - type: object
      required: [phone]
      properties:
        phone:
          type: string
```
Scenarios such as:
* Ref and inline
```yaml
schema:
  allOf:
    - $ref: '#/components/schemas/UserBase'
    - type: object
      properties:
        isAdmin:
          type: boolean
```

* Both refs
```yaml
requestBody:
  required: true
  content:
    application/json:
      schema:
        allOf:
          - $ref: '#/components/schemas/UserBase'
          - $ref: '#/components/schemas/UserMetadata'
```

Should work fine.



# Pending tests
* Source code generation of getters and setters. Could be a unit test
* E2E tests to evaluate successful compilation