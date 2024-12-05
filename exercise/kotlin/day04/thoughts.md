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
-  