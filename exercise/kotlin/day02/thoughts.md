# Exercise Thoughts

## TL/DR: My personal retrospective
- fun exercise
- production code changes rather simple for me
- minor challenge: handling Optionals 
- major challenge: Getting my head around kotest and property tests
- things in need of further improvement:
  - where to put defaultReplacementWordsByDivisor and santaVersionReplacementWordsByDivisor?
  - refactor unit tests to target specific properties of the solution (i.e. "concatenates replacement words when input matches multiple replacements" rather than providing a long list of examples implying the solutions properties
  - type of input parameter should probably be wrapped in a type to prevent primitive obsession
    - ✅ pair in that type should probably also be replaced by a fizzbuzz specific type (could also suffice to just replace pair)
  
## Before viewing the code

Idea: Two variants with varying degrees of rule-injection freedom
1. Inject pairs of divisor and added word
   - Assumption: Rather good cohesion of game class, as basic game logic remains in class.
2. Inject whole game logic (abstract mapping from number to string), just keep looping over numbers in game class
   - Assumption: Greater freedom, but not much game logic remaining in game class

## While viewing code

- Challenge: Allowed strings in property based tests might suffer from combinatoric explosion, when adding more cases
- Challenge: Need to decompose FizzBuzz case to individual ifs with concatenation, as otherwise it will result in combinatoric explosion when adding more cases
- when with `is` feels hard to read, see if that can be made more expressive

## Changing Code
- preparatory refactoring: replace fizzbuzz case with individual divisability tests + string concatenation
- preparatory refactoring: replace explicit fizzbuzz case in property test with several more decomposed tests
  - idea 1: test that ensures result is either the number or a combination of replacement words
  - idea 2: test that ensures each replacement word is only contained once
  - idea 3: test that ensures replacement words are contained in correct order
  - when thinking about a regex to implement idea 1, i realized, that all 3 ideas could be realized with a rather simple regex
    - what i don't like here: as is common with regexes, this one needed a comment in order to explain its details
- solution part 1: refactor FizzBuzz, so that replacement words are injected along with their divisors