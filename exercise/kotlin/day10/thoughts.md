# Day 10: Fix a bug - Thoughts

## TL;DR - My personal highlights

- That was a fun one, and also pretty quick for me
- Hope the elves don't mind all the "improvements" I made to their code
- Extracting the floor change constants out of the loop with a chain of tiny, mainly automated refactorings was not as simple, as I think it should be. Probably I'm missing something here.
  - Ask the others, if they found a simple, mostly automated way to do this
  - Or try what ChatGPT would've done

## Before reading code

- Finally, I find time to work on Advent of Craft again. Hope I didn't miss relevant skills or knowledge from the skipped days
- Let's go bug hunting! 🐞

## Reading code

- Running tests first
    - Having the instructions in a file is neither readable, nor fast. Maybe fix that after production is up apain!
    - Let's have a look at that file 6
      - OMG, so many parantheses! 😱 Probably need to reduce the test to a minimal reproducible example
      - First: Let's understand what the code does (exploring both other test files and prod code)
- Reading Building.kt
  - Differing behaviour based on the presence of 🧝 in the instructions
    - In case 6, it's present -> probably first branch 
    - Third branch seems to be superfluous, as the input must either contain 🧝 or not contain it 
  - Readability is lacking. Feeling tempted to refactor, but would rather not do so on red
- Looking at test cases:
  - 6 is the only one with 🧝
    - bug probably related to its presence
    - Hunch: Maybe we need to ignore the 🧝 in the input
      - Let's try!
      - That's it! 🎉
- Let's get rid of the unnecessary list first
- Next: Check if we can remove the third branch
- Next: Let's move the decision about floor steps out of the loop
  - I'm tempted to introduce some kind of strategy for increasing and decreasing based on presence of the 🧝, but for two cases this seems to be overkill
  - The idea with using a map turned out nice, no more need for a strategy
- Next: Simplify for loop
- Next: Refactor tests to include instructions in test instead of files (for clarity and speed)
  - Hopefully this was not some kind of BDD test, which Santa wants access to
- Next: Split tests to reflect distinct cases of 🧝 presence
- Next: Add some simple test cases, for easy demonstrating of the floor changing rules
  - will not add test names, in the hope of not completely losing the elves
  - maybe I should ask the elves to pair-program tomorrow,
    - that way we might develop a shared understanding of how they'd like to have their code structured 

## Open points
- Delete the test files, if the elves are fine with the new tests