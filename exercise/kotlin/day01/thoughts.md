# Thoughts contributing to my solution

- first hunch: constructor-inject logger, as it probably is
  - useful for many methods in communication
  - will be the same for all methods and calls
  - won't change at runtime
- guesses about business rules
  - reindeer name strongly hints at a reindeer entity
  - currentLocation also seems to relate to the reindeer
  - numberOfDaysForComingBack
    - is probably be related to currentLocation
    - might also be dependent on speed of the reindeer (no hint for or against that in the code)
  - numberOfDaysBeforeChristmas is probably only dependent on the current date
- refactoring steps:
  - extract reindeer with name & location
