# Captured thoughts

## TL/DR - Summarizing thoughts after refactoring
- tings i like about my solution
  - now more obvious that location & dates are involved
  - hopefully improved understandability of interplay between remaining time to christmas, required resting time & travel time to base
  - slightly extended test suite, capturing improved understanding of the problem
  - how introducing "wrappers" to fight primitive obsession turned out
- things i dislike about my solution
  - seems hugely overengineered
  - overall number of parameters not reduced (when taking constructor into account)
- other observations:
  - problem seems tiny, but contains loads of
    - refactoring opportunities
    - opportunities for head-scratching and misunderstandings
  - initial code left some aspects of problem & solution unclear. Example: Is travel time also dependent from reindeer?
    - refactored code explicitly shows, that there is no such dependency
      - is this a good thing?
      - is it coupling too tightly by having more knowledge about the services it depends on?

## More thoughts after looking at some other solutions
- Seems i only solved part of the initial problem (ability to swap parameters by mistake) and moved it to the constructor
  - can no longer swap reindeer name & location, but can still swap today & christmas day
  - need to think about a better solution that neither reduces testability (new LocalDate()) nor increases complexity ("ForGettingToday")
- i should also branch for each day, to make the changeset more digestable in form of a MR to main

## Notes made during session
- first hunch: constructor-inject logger, as it probably is
  - useful for many methods in communication
  - will be the same for all methods and calls
  - won't change at runtime
- guesses about business rules
  - reindeer name strongly hints at a reindeer entity
  - currentLocation also seems to relate to the reindeer
  - numberOfDaysForComingBack
    - is probably related to currentLocation
    - might also be dependent on speed of the reindeer (no hint for or against that in the code)
  - numberOfDaysBeforeChristmas is probably only dependent on the current date
- refactoring step: extract reindeer with name & location
- observation: two main concepts besides reindeers: time (in days) & location
- refactoring: try to capture time as a concept
  - introduce today and christmasDay as LocalDates to replace numberOfDaysBeforeChristmas
    - assumption: Communicator will be reconstructed regularly
- idea: introduce "TravelTimeService" as a dependency to "replace" numberOfDaysForComingBack
  - would need baseLocation
  - would need some kind of test stub to inject value for numberOfDaysForComingBack
  - would feel wrong to pass in strings as locations
    - refactoring: Introduce Location wrapper around string to better represent intention of those strings
- understood from composed message content: resting time is the time reindeers need to rest at base between returning & christmas
  - refactor: make that clear through constructor parameter name & explanatory variable