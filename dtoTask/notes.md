# Pending features
* Support arrays as payloads
* Support for ChoiceGene
* Support for Kotlin as DTO output


# Pending discussion


# Test notes
* `oneOf` parsing returns `ChoiceGene` with an array of choices. Schemas defined in the components section have a `refType` for the name. Everything should be ok here.
* `anyOf` parsing returns `ChoiceGene` with an array of choices, both schemas and the mix. Schemas defined in the components section have a `refType` for the name. Everything should be ok here.
* `allOf` creates a single Dto with each one. Naming is only missing


# Pending tests
* Source code generation of getters and setters. Could be a unit test
* E2E tests to evaluate successful compilation


# Detected bugs
1. No body param still tries to generate dto. [FIXED]
2. Duplicate variable names in the same test case.
3. DTO instantiated with the key name: `Key dto_Key_3 = new Key()` when it should have been: `ContributorKey dto_Key_3 = new ContributorKey()`.
4. For `Long` values, add `L` as last char in the number.
5. Add imports for lists in DTO classes.