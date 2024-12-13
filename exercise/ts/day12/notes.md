## TL;DR - My highlights

- Object Calisthenics:
  - is a very nice SW-design challenge
  - acted as a design catalyst for me, leading to an unexpected and very pleasing chain of refactorings (read on to learn more)
  - Builder pattern was very helpful to limit refactoring impact on tests
  - Continue.dev with GPT 4o keeps impressing me, wonder at which points it'll start to fail miserably 

## After finishing

- Object Calisthenics
  - Question to other SW-Crafters: Would you think Object Calisthenics allow switch statements? 
  - is quite a challenging set of constraints
  - seems like a catalyst for SW-design choices to me
  - triggered a very unexpected chain of refactorings, first pushing things down the object tree, to pull them way up in the end:
    - After extracting a `Desires` class, containing a child's wishes and behavior, the present-selection part of `Santa.chooseToyForChild` had a strong feature envy smell, prompting us to extract that part to `Desires`
    - The `Desires` class didn't feel right from the start
      - Decided to introduce independent repositories for wishlists and behaviors in a [Scratch Refactoring](https://xp123.com/scratch-refactoring/)
    - Ended up keeping the scratch refactoring as the design finally felt right and allowed to remove the `ChildRepository` alltogether
- Builder pattern helps enormously to ease refactoring of setup-code for tests
- Contiue.dev with GPT-4o was crazy-good with high-level refactorings
  - will probably fail with bigger projects
  - IDE integration is way better than in browser (ease of setting context)
  - IDE integration is still lacking (UI bugs & accepting of inline edits is fragile to say the least)

## During work

- trying mob.sh -> didn't work due to missing updates on system -> manual git handover
- ChildrenRepository sounds ethically incorrect
- Is child name unique?
- mix undefined & Error
- early returns in long method
- behaviour should be enum -> no more undefined
- object calisthenics [source](https://williamdurand.fr/2013/06/03/object-calisthenics/#tldr)
  - Only One Level Of Indentation Per Method
  - Don’t Use The ELSE Keyword
  - Wrap All Primitives And Strings
  - First Class Collections
  - One Dot Per Line
  - Don’t Abbreviate
  - Keep All Entities Small
  - No Classes With More Than Two Instance Variables
  - No Getters/Setters/Properties
- TODO set up mob.sh
- why no return types in TS?
- this. does not count towards OC, right?
- what would this look like in TS idiomatic?
- should we throw in our santa method -> the tests say it's expected
- Contiue.dev with GPT-4o is crazy:
  - Prompt: make Child.behaviour not only introduced enum, but also resolved other TODOs (make childrenRepository a first-class collection)
  - Prompt: make Child.behaviour an enum, but don't resolve any TODOs (did as expected)
- !!! Interesting: Desires lends itself very well for getting the granted toy by behavior
  - Also strongly hinted at by the feature envy in Santa.chooseToyForChild
- I'm unsure about SantaBuilder...
- Desires.chooseToy makes much sense from an encapsulation perspective
  - But, to me it feels the logic of choosing a present based an the child's behavior is misplaced in Child or any of the classes it owns
  - Behavior (before Christmas) and Wishlist could be argued not to be inherent properties of a child. Therefore, it might be sensible to have one or two separate services or repos for them
    - Also, it might be a good idea to separate them from each other, as observing and evaluating behavior would probably have a different stakeholder than processing wishlists and storing them
    - Thinking of services that might provide Behavior and Wishlists sparks the idea of modelling them as ports & adapters
  - As our whole application / domain is focussed all around Christmas, on the other hand, Behavior and Wishlist could be modeled as inherent properties of a child
  - So many options...
- !!! Ok, this is wild: After introducing the BehaviorRepository and WishlistRepository, Santa no longer needs a ChildRepository (which felt weird to have in the first place...)
  - Technically, even the Child class becomes unnecessary
- I wonder if switches are allowed in Object Calisthenics...
  - Could replace the switch by making Behavior a strategy. But that would create a strong coupling between the two, which I don't like.
  - Another option: Switching on the Behavior enum, to create a strategy, but that would not get rid of the switch 
  - Next option: Implementing something resembling a chain of responsibility, but that seems like overkill 
- Observation: Technically, usages of the SantaBuilder are violating OneDotPerLine. But it could be written in several lines & is way more readable like this. 