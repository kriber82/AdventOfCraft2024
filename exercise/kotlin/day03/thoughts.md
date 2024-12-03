# Day 3 thoughts

## Before viewing the code
- fuzzing is new to me, so i will try to concentrate on that first in order to optimize for learning
- smell in code snippet from description: property recommended age is set via string identifier and retrieved via specific getter. This should be improved upon, if there is time left.
- found fuzzing library [jazzer](https://github.com/CodeIntelligenceTesting/jazzer) for JVM languages, which is also integrated into googles [OSS fuzz](https://github.com/google/oss-fuzz). Will give jazzer a try.  

## First steps:

- inspect code: SantaWorkshopService.preparedGifts seems weird, as it is set only - add tests or remove, if time permits? 