# Day 10: Fix a bug - Thoughts

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
- Let's get rid of the unneccesary list first
- Next: Check if we can remove the third branch
- Next: Let's move the decision about floor steps out of the loop

## Open points

- [ ] Refactor tests to include instructions in test instead of files (for clarity and speed)