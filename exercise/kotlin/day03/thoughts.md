# Day 3 thoughts

## TL/DR: My thoughts afterward
- learned a lot about fuzzing, which i have not used previously
- learned a little too much about jazzer as a fuzzing library ;-)
- it's hard for me to assess the validity of software design and severity of smells on these small snippets
  - seemingly, I'm judging software designs and associated smells depending on the context, they are embedded in
  - => the infamous "it depends" holds true for my sw-design senses as well
- first steps with IDEAs AI Assistant:
  - pretty good, but seemingly inconsistent in proposing refactorings
  - didn't seem very helpful for executing refactorings that span more than one class :-(
  - free ChatGPT was way more helpful in pinpointing and explaining niche problems with jazzer
- fuzzing helped uncover some of the more absurd aspects of storing recommendedAge in attributes
  - i detected the initial smell on recommendedAge before fuzzing -> would probably have refactored that anyway
  - fuzzing as a sw-design tool questionable
- i can see the value of fuzzing in catching edge-cases, especially on public APIs
  - might be especially interesting for security concerns


## Before viewing the code
- fuzzing is new to me, so i will try to concentrate on that first in order to optimize for learning
- smell in code snippet from description: property recommended age is set via string identifier and retrieved via specific getter. This should be improved upon, if there is time left.
- found fuzzing library [jazzer](https://github.com/CodeIntelligenceTesting/jazzer) for JVM languages, which is also integrated into googles [OSS fuzz](https://github.com/google/oss-fuzz). Will give jazzer a try.  

## Working notes:

- inspect code: SantaWorkshopService.preparedGifts seems weird, as it is set only - add tests or remove, if time permits?
- install/use jazzer:
  - to my surprise junit 5 worked out of the box
  - setup not too hard
  - test execution seems weird, as fuzzing part is shown as ignored in idea, unless something fails
- how to handle weight?
  - replace exception with a different mechanism?
  - ensure fuzzer only generates valid values for weight?
- testing with jazzer fails with *** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding" with message can't create name string at s\src\java.instrument\share\native\libinstrument\JPLISAgent.c line: 838
  - asking ai tools suggests, that JDK issues or low heap memory could be causes trying to increase heap -Xmx2048m -> no improvement
  - trying to switch to java -> happens there as well
    - setting maxExecutions on FuzzTest helps -> could indeed be a heap size problem -> won't fix now, as 1000000 executions seems reasonably high for trying
  - it seems using [recommended JVM options](https://github.com/CodeIntelligenceTesting/jazzer/blob/main/docs/common.md#recommended-jvm-options) helps preventing the crash (not tested in isolation of other changes) 
- fuzz testing "should retrieve an attribute to a gift"
  - with arbitrary string input to recommended age, parsing to int will go wrong
- "gradle test" seems to execute only one of the fuzz tests. Why?
  - ChatGPT: The behavior you're encountering—where only one of the fuzz tests is executed—happens because Jazzer is designed to run one fuzzing test per JVM instance. This is a common design choice for fuzzing frameworks to avoid interference between test runs and to ensure that each fuzz test has complete control over the environment.
  - My workaround: Introduce a single @FuzzTest-annotated method that calls the individual tests (not among ChatGPTs proposed solutions)
  - open point: would this prevent non-fuzzed tests in test classes after the fuzz-test-class? 
- Trying to refactor recommendedAge with IDEAs AI Assistant was rough:
  - initial suggestion for the refactoring was a good one, imho
  - refactoring execution was bumpy to say the least, especially for fixing usages I had to revert to manual changes.
  - would probably have been smoother applying a chain of IDE refactorings by hand
- Retracing refactoring by hand
  - only noticed now: all other fields are immutable, should probably also apply to recommendedAge, making even more tests obsolete
  - most of the refactoring could be done with automated steps. Some local changes in Gift & final pruning of addAttribute calls needed to be done by hand
- will keep the exception in prepareGift and attributes in Gift, as context information for assessing their validity is missing
