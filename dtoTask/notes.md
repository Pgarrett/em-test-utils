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