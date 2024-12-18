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
- found it hard to come up with a good to express a test that combines gender + rest of EID
- researching concise ways to work with either took some time and mental effort. 
  - Any feedback is welcome! (especially on EID.companion.parseYear ... there must be a better way to do this)
- using property based tests everywhere could become too slow for large SW
- wow, the property based tests found an actual bug: year = "+0" was parsed and probably should not have been
- NICE: found kotest-assertions-arrow
- copilot (chat) seems helpful when learning new idioms like arrow
  - need to experiment with scope & abstraction level of prompts

### Test List:
- [x] parse valid gender
- [ ] reject any non digits
- [x] reject gender = 0
- [x] reject gender > 3
- [x] parse valid year
- [x] reject negative year
- [x] reject non-number year
- [x] reject serial number = 0
- [x] reject negative serial number
- [x] reject non-number serial number
- [x] parse valid serial number
- [x] calculate control key from payload
  - [x] control key calculation should not fail vor valid EIDs  
- [x] reject control key calculation for non-numbers
- [x] reject non-matching control key
- [x] accept matching control key
- [ ] reject control keys outside of valid range
- [ ] parse matching control key?
- [ ] accept all valid eids

## TODOs
- [x] use a builder to construct eids from candidates and individual fields?
- [x] find more succinct ways of asserting?
  - [x] parsed.shouldBeRight()
  - [x] parsed shouldHaveStringRepresentation eidString
  - [x] parsed shouldBeLeft someError