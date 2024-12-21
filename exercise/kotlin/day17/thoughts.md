## TL;DR - My highlights summarized

Once again, I had a great time working on this one.

First of all a request: I would greatly appreciate feedback about the readability of my code in this one.
Special focus: How can I improve my usage of Either & Raise (in prod & test)

This day was so full of learning for me:
- Learned the basics of using Either (and arrow-kt's Raise along with it)
- Deepened my understanding of how property-based testing (PBT; with jqwik) works
- Learned that "Parse, Don't Validate" formally expresses my intuition that having potentially invalid domain objects should be preventend when constructing them.
- GPT 4o seems to have the potential of greatly flattening the learning curve on new libs and even concepts
- test list reduced mental load again (especially with all the learning) 

##  Thoughts afterwards - My highlights

My Either/Arrow highlights:
- Need to learn new idioms to create well-readable code
- Synergizes very well with kotlin built-in mechanisms
- Missing knowledge about a few idioms or kotlin mechanisms makes the code "magical" (how does this even work).
  -  miss a few more and it becomes very hard to understand how it works
- Assuming the reader can understand, I really like how expressive the resulting code is
- Resulted in way more fine-grained feedback about potential errors during parsing
  - => seems especially well-suited for scenarios that go wrong often and in various ways
- kotest-assertions-arrow greatly increases test readability
- Learned about arrow plugin for IDEA after most work was done -> want to try starting out with it sometime soon to learn if⁄how it helps

About working with Copilot / GPT 4o
- IDE integration is crucial (VS Code is sooo much better than IDEA, here)
  - Switching between IDEA for "manual mode" and Code for "AI mode" is distracting, but bearable and worth the effort from my perspective
- Worked well for:
  - concrete questions about idiomaticness (hopefully)
  - questions about concrete language features
  - high level refactorings with limited context size
- Mixed results for:
  - asking about refactorings without having a specific problem or direction in mind

Some of my PBT / jqwik highlights:
- has the potential to uncover some corner cases
- might harm readability of the tests
  - annotations add lots of boilerplate
  - splitting of test data & test step via @Provider & @Property makes understanding hard
  - can be offset to a degree with expressive test- & Arbitrary-naming
  - i don't like referencing the @Providers via strings
- adds several orders of magnitude to test execution time
  - probably not suited as general compile-suite strategy
- builder was extremely helpful to combine arbitrary valid EID with invalid part in a concise & well-readable way
- combining different invalid variants for an EID part (i.e. non-digit & <1) in a single @Provider greatly reduces annotation & fun boilerplate
  - conflicts with my goto practice of one test per concept
  - see `should reject negative years` and `should reject invalid control keys` for examples of both variants
  - need more experience with that to decide or come up with a better solution

- containing both the input string and the parsed fields in EID feels odd, but worked out well

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
- learned: kotlins let is a great way of handling nullables

## Reading the code

- not much to read ;-)

## Implementing

- [How to use Either for error handling](https://proandroiddev.com/how-to-use-arrows-either-for-exception-handling-in-your-application-a73574b39d07)
- https://www.conventionalcommits.org/en/v1.0.0/#summary
- found it hard to come up with a good way to express a test that combines gender + rest of EID (solved later)
- researching concise ways to work with either took some time and mental effort. 
  - Any feedback is welcome! (especially on EID.companion.parseYear ... there must be a better way to do this - improved later on... i like parseIntFieldWithErrorProvider now)
- using property based tests everywhere could become too slow for large SW
- wow, the property based tests found an actual bug: year = "+0" was parsed and probably should not have been
- NICE: found kotest-assertions-arrow
- copilot (chat) seems helpful when learning new idioms like arrow
  - need to experiment with scope & abstraction level of prompts
- implementing the full parser in the companion object:
  - doesn't feel 100% correct
  - but allows for the private constructor
  - => keep
- kotlin: return in let-lambda returns from the enclosing fun (see [stackoverflow](https://stackoverflow.com/questions/56671453/kotlin-where-the-return-from-inside-the-let-go) & [kotlin docs](https://kotlinlang.org/docs/returns.html))
  - not so with anonymous funs
- and another (albeit minor) bug detected by PBT: "-00003xx" yielded a control key above 100, which in turn led to different parsing errors than expected
- copilot
  - GPT:
    - converting class of 170 lines failed (probably due to length restrictions)
    - worked in browser
    - 
  - claude
    - better output, but aborted generating several times
- kotest with properties (comparison to jqwik):
  - is way more readable
  - found edge cases much faster
  - is slightly faster in execution

### Test List:
- [x] parse valid gender
- [x] reject any non digits -> done in all relevant fields
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
- [x] reject control keys outside of valid range -> not necessary
- [x] reject control keys with non digit chars -> not necessary
- [x] parse matching control key?
- [x] accept all valid eids

## TODOs
- [x] use a builder to construct eids from candidates and individual fields?
- [x] find more succinct ways of asserting?
  - [x] parsed.shouldBeRight()
  - [x] parsed shouldHaveStringRepresentation eidString
  - [x] parsed shouldBeLeft someError