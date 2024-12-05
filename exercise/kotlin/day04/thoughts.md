# Day 4 Thoughts

## Before reading the code

- Looking forward to the opportunity to check out mockk or similar!
- I wonder what the rest of the initial code for this kata will look like
- Expecting a smoother ride than yesterday, due to the familiar topic
- Initial idea: Start with my goto approach (mostly fakes) and use a library after familiarizing myself with the code

## Reading the code

- Interesting, the interfaces have no prod implementations
- New idea: Start with a library in order to simulate using this approach "for real"
- Test-Double libs research: Mockk seems to be preferable, in projects using kotlins features

## Implementation

- IDEA AI Assistant
  - has no access to project files by default
    - good for security reasons
    - sad as I was hoping for a highly integrated solution with less friction due to IDE-integration
  - did not find an option to adding more than one file to the context, so the code generation capabilities with the test class as context were very limited
    - using Routine.kt as the context for generating @BeforeEach of SUT with mockk dummies worked well
      - friction of moving the generated code over to the other file)
      - test code was generated using junit instead of kotest (probably due to existing test code missing in context)
- When running tests from within IDEA: Unsupported class file major version 65 for mockk generated mocks (I hate those types of problems, yet another reason to use hand-written fakes)
  - invalidated assumption: not happening with "gradle test" in IDEA terminal... -> need to set project SDK in IDEA...
    - was just not visible due to --info missing on gradle call
  - IDEA had project JDK set to 17, not 21
  - upgrading mockk to version to "1.13.3" made the error message more explicit (explicitly saying JVM 21 not supported)
    - no idea, why i did not discover the actual latest version (1.13.13) right away, which would have fixed the error...
  - downgrading jvmToolchain & jvmTarget to 20 worked (but only in conjunction with the mockk upgrade to 1.13.3)
- verification with mockk
  - syntax is simple
  - for the simple interactions in Routine.start(), this works rather well
  - when using automated IDE refactorings, the mocks will increase friction in some cases, as their setup will be repeated throughout the code base
  - verifying temporal coupling between organizeMyDay and continueDay was rather effortless
  - verifying continueDay is the last call seems messy with mockk. wonder if there is a better way to express this with manually created fakes
- testing with manually created fakes
  - will try to replicate same test cases for easier comparison 
  - setup of dummy objects generates some boilerplate code, but was very quick
  - verification using custom methods on fakes creates some more boilerplate, but seems very readable and is done quickly
  - AI generating the test code for organize day with high-level prompts did not generate good test code
    - AI generating fake implementation using hand-written test code was OK-ish
      - needed several manual adjustments to match my expectations
      - additionally anticipated the need for assertContinueDayWasCalled and assertNoOtherInteractions
        - names are not in line with other assert methods on fakes
        - implementation for assertNoOtherInteractions was missing
        - implementation of assertContinueDayWasCalled did not anticipate the test for temporal coupling implied by the test name
  - AI Assistant failed miserably at adjusting implementation for assertContinueDayWasCalledAfterOrganizingDay
    - generation stopped mid-file
    - two generation runs yielded wildly different implementations
    - generation took quite some time
    - first attempt was based on creating more booleans for tracking the call order :-(
    - trying with chatGPT
      - worked and used a list of method calls, as i would probably have done it 
      - prompt to replace the existing bools using the list of calls worked as expected
      - friction copying code back and forth :-(
  - fake for ScheduleService gathered quite some complexity
    - on the flip-side the readability of the test cases is great
- ensuring continueDay is the last interaction:
  - fake complexity rises to a level, where using a library seems worthwhile
  - having "CallsOrder.assertIsLastInteraction" is great for test readability
    - how easily could something similar be achieved with mockk?
  - using chatGPT (free) to modify code within a single file, based on high-level prompts works surprisingly well
    - see commit log for prompts