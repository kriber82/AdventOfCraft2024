## TL;DR My personal highlights

- Pitest (Mutation testing for java)
  - can help spot logical loopholes in tests
  - does not spot all logical  loopholes (i.e. "off by a little" in floating point numbers)
  - can be hard to set up
- about spotting edge cases that lead to bugs
  - tests with purely random generated data (or datafaker) have a very bad chance of catching those bugs  
  - dedicated fuzz testing (i.e. with jazzer) does way better in this regard, but is hard to set up
  - dedicated property based testing (jqwick) also works well and was a breeze to set up
- datafaker is nice for generating reality-like data nonetheless

## Thougths afterwards

- ChatGPT is not particularly helpful for setting up pitest in a Maven project.
- Pitest (Mutation Testing for Java) can help improve the logical coverage of code through tests.
  - Code coverage is also helpful. However, executing a line in a test does not necessarily mean that the content of the line is thoroughly tested.
  - Example: For `if (a < 5)`, tests with `a=2` and `a=10` generate full branch coverage. Changes to the constant `5` in the condition or off-by-one errors (e.g., using `<=` instead of `<`) are not detected by these tests. Pitest can detect this type of gap in the tests.
- Regarding pitest
  - Even pitest does not find all test gaps:
    - Example: The same test gap is not detected for `if (a < 5.0)` (double instead of int) with tests `a=2.0` and `a=10.0`. I assume this is because there are no clear predecessors/successors for 5.0 in this case.
  - Pitest requires `junit-jupiter-engine` as a dependency; `mvn test` and the test run from IDEA work without it.
  - The error messages in pitest are not particularly user-friendly.
- About finding potential crashes through tests with "random" input:
  - fully randomized tests, e.g., with `@ParameterizedTest` and `@MethodSource` (and potentially Datafaker) don't work too well here
  - dedicated fuzz-testing libraries (e.g., Jazzer) are significantly better suited 
- About Datafaker:
  - it appears to be a good candidate for tests with realistic but random data in Java
  - seems to be aimed at providing reality-like fake data for tests
  - might help to prevent business logic problems stemming from overly specific test cases
  - isn't well-suited for finding rare crash conditions 
- Jqwik could be a good candidate for property-based testing in Java.
  - It attempts, for example, to simplify a found test-breaking input to get as close as possible to a minimal reproducing sample.
  - Test with slightly complicated div by zero cases were detected in reasonable time

## working notes
- totalToys == 0
- age < 0
- setup of pitest not straightforward
  - failed due to junit-jupiter missing in dependencies
  - maven tests ran despite missing dep
  - failure output of pitest hard interpret
- can call maven commands in intellij via button on top (despite no local installation of mvn)
- pitest results:
  - private constructor is not covered, but we'll leave that as is, as it prevents constructing the static class
  - added some tests at the age boundaries for categorize gift
- will try [datafaker](https://www.datafaker.net/) to add random data tests
  - datafaker is pretty simple to set up and use with parameterized tests
  - readability for more than one parameter via @MethodSource is not great
  - suspected "division by 0" is not a problem for doubles
  - finding edge cases like div by zero would take huge amounts of time with randomized values
    - datafaker seems to be aimed more at anonymizing data, and maybe property based testing
    - are fuzzing libs better at detecting corner cases?  
- review of pitest findings:
  - ! can alter doubles in ensureToyBalance without a test failing
- toying with jazzer
  - found div by zero problems in several variations (+/- small constants, +/- other var) rather quickly
    - to introduce div by zero, i changed `(double) toysCount / totalToys` to variations of `(double) (toysCount / totalToys)`
- more toying with pitest, trying to get it to detect the bad detection of double constant changes
  - using STRONGER and ALL mutations did not help
  - found no docs of more double mutators in arcmutate (paid extension), but didn't bother to try
  - gave up and created additional testcases without a tool telling me to
- tried again with jqwik
  - dead simple to setup
  - nice detection of several non-trivial div by zero bugs 