## Before viewing code

- nice, parsing EIDs
- not entirely clear to me, what the constraints mean
  - !Should I avoid primitive obsession? (let's assume this)
  - [Parse Don't Validate](https://xtrem-tdd.netlify.app/flavours/design/parse-dont-validate/) -> interesting, new to me
    - "guarantees only valid data will be created"
    - parser makes sure, only valid input will end up in a business object
    - now the image makes sense: in hex arch terms, the parser is an adapter protecting the domain model from invalid EIDs
      - not sure, how i like the `eid: String` part, though
    - ! hide constructor (ensures creation is done through parsing) 
    - ! use either monad
  - ! TDD with [Property-Based Testing](https://xtrem-tdd.netlify.app/flavours/testing/pbt/)
  - Parser signature: String -> Either[ParsingError, EID]
- Library research:
  - PBT: kotest vs jqwik -> let's try with jqwik from the start this time
    - maybe read [this in-depth article](https://johanneslink.net/property-based-testing-in-kotlin/#finding-good-properties) sometime soon
  - Either monad: Arrow Lib has `Either`, Kotlins built-in `Result` is similar to either for error handling, Funktionale lib
    - Want to explore arrow!
    - maybe, I should also read [more about monads](https://medium.com/@albert.llousas/monads-explained-in-kotlin-4126ac0cb7f2)

## Reading the code

- not much to read ;-)

## Implementing

- [How to use Either for error handling](https://proandroiddev.com/how-to-use-arrows-either-for-exception-handling-in-your-application-a73574b39d07)
- https://www.conventionalcommits.org/en/v1.0.0/#summary