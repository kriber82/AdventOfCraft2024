# Day 14

## TL/DR - Thoughts afterward

- Neither web search, nor LLMs brought up approval testing as a potential solution
- Struggling again to get a java testing library to work in kotlin, wondering if it's worth the hassle...
- I think approval testing is a nice fit here. Considerations:
  - Depends on Child.toString() returning a complete & stable representation of the object.
    - For normal classes, I wouldn't want to count on that.
    - For a data class with only data class children, I think that might work
  - Will lead to test failures on valid rename refactorings -> acceptable as those should be obvious to fix 

## Before looking at the code:

- Test that mapping back & forth produces an equal result
- Approval testing, if we have a simple & stable way of converting the Child class to a String
- Property based / fuzz testing would only ensure that mapping works for more variants of the same fields 

## Looking at the code:
- I don't like the answers provided by GPT 4o and Llama 3.1 8B here
- will try approval testing, as the mapping back from the domain object to DTO does not yet exist

## Implementing the tests:
- approvals seemingly don't work with kotest
  - adding junit dependency doesn't help
  - running in a junit test doesn't help
    - Didn't find children.children.ChildMapperApprovalTests under /Users/kristianbergmann/Documents/SW-Craft/katas/AdventOfCraft2024/exercise/kotlin/day14
      com.spun.util.FormattedException: Didn't find children.children.ChildMapperApprovalTests under /Users/kristianbergmann/Documents/SW-Craft/katas/AdventOfCraft2024/exercise/kotlin/day14
    - problem was bad package declaration (package children, but default folder)
  - removing kotest doesn't help
  - why does kotlin jvm 2.1.0 not work on my mac?
  - approvals kotlin sample project works, when dowgrading kotest jvm to 1.8.0
  - out of ideas for now
  - moving tests out of default package
- Assuming the current implementation and tests were correct.
  - Did some quick exploratory testing to ensure this is the case 
  - Could us mutation testing to be extra sure, but won't today