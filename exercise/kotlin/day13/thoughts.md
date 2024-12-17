# Day 13

## Before looking at code

- I like baby steps
- I like the mikado technique, but usually don't need to use it as I can detect workable baby steps in my head 
- Let's try without thinking ahead too much in order to practice

## Looking at the code

- Won't look to closely in order to not think too far ahead

## Implementing

- [ ] 👍 1 Deploy a generic method to compute the `X for Y` discount offers, covering `Three for two` and `Two for one` offers
    - [ ] 👍 1.1 Prepare the code for an easy addition of the `X for Y` discount type family
        - [x] 1.1.1 Merge "x" into other when branches
        - [x] 1.1.2 Push x into individual branches, to reduce dependencies between branches
        - [x] 1.1.4 Make different X for Y logics as similar as possible
          - [x] 1.1.4.1 Introduce `discountItemsGiven` & `discountItemsPaid`
          - [x] 1.1.4.2 Use `discountItemsGiven` & `discountItemsPaid`
          - [x] 1.1.4.3 Inline variables so that all variants have the same equation
          - [x] 1.1.4.4 Reorder terms in first variant equation to see if all equations are equal  
          - [x] 1.1.4.5 Rename terms to follow the same naming
          - [x] 1.1.4.6 Check potential bug due to additional `* unitPrice` in THREE_FOR_TWO
          - [x] 1.1.4.7 Adapt THREE_FOR_TWO code to be able to use X_FOR_AMOUNT logic
            - [x] 1.1.4.7.1 introduce priceForAmount or similar priceForGivenItems
            - [x] 1.1.4.7.2 Eliminate misleading discountItemsPaid in X_FOR_AMOUNT cases
            - [x] 1.1.4.7.3 Move priceForGivenItems out of if
        - [x] 1.1.5 Extract common logic for X_FOR_AMOUNT
            - [x] 1.1.5.1 pull `discount =` out of branch
        - [x] 1.1.6 Improve names for extracted code
        - [x] 1.1.7 Use extracted method where possible
        - [x] 1.1.8 Separate X for Y logic from other discounts
        - [x] 1.1.9 introduce discount strategy (replacing offer.offerType)
          - [x] get rid of offerType in Offer 
          - [x] combine TwoForAmountOffer and FiveForAmountOffer
          - [x] where to put helper method currently residing in offer?
          - [x] make offer an interface
    - [x] 👍 1.2 Implement the `Two for one` discount computation
        - [ ] ...
    - [x] 👍 1.3 Refactor the existing code to use the `X for Y` discount computation method with the `Three for two` discount
- [ ] 2 Parking-Lot (any change with no direct impact on the main goal)
    - [x] 2.1 Individual tests look very long
    - [x] 2.2 Improve (internal) discount names to reflect new understanding of discount types better
    - [ ] 2.3 Talk to PO 
      - [ ] We intend to deprecate SpecialOfferType -> OK?
      - [ ] We intend to improve discount descriptions on receipts -> OK?
      - [ ] We intend to remove ShoppingSleigh.addItem, as it adds complexity without much value -> OK?
    - [x] Reformulate discounts with polymorphism?
    - [x] add more expressive way of creating & adding offers to ChristmasElf 
      - [x] deprecate ChristmasElf.addSpecialOffer
      - [x] test new ChristmasElf.addOffer
    - [x] add tests for bundle offers (reduced price and reduced items to pay) that don't match bundle size (exists: fiveForAmountDiscount)
    - [x] understand difference between ShoppingSleight.getItems and ShoppingSleigh.getQuantities
      - [x] potentially refactor to unify concepts (did as much as possible without touching public interface)
    - [x] improve expressiveness of assertions -> tests now have way less assertions & 
    - [ ] introduce a concept for ProductInSleight (might simplify both prod & test code)
    - [ ] express tests in a way that better highlights the different tested featuers
      - [x] sleigh items & total 
      - [x] total
      - [x] receipt items
        - [x] without discounts
        - [x] unchanged by discounts
      - [x] discounts (each one individuially)
        - [x] old addSpecialOffer
        - [x] receipt output
        - [x] reduced price
      - [x] new addSpecialOffer (might be sufficient to test with one offer type)
      - [x] there can only be one special offer per product
      - [ ] maybe introduce FakeOffer for simpler testing of other aspects of the code
    - [ ] check rest of codebase for possible improvements

Starting with 1.1:
- Tests look exhaustive at first sight
- Individual tests look very long
- Searching again, how to toggle ai full line autocompletion in IDEA
  - Shift + Shift -> Enable Full Line Suggestions -> go to settings -> toggle or read hint
  - built-in local code completions can only be enabled without active copilot plugin
  - copilot has toggle for completions behind icon at bottom right of IDE
  - continue.dev: Shift + Shift -> Show Continue Autocomplete
    - also: https://docs.continue.dev/customize/deep-dives/autocomplete#full-example without apiBase
    - works in vs code now, but not in IDEA
- Analysis for 1.1
  - Discount logic is implemented in ShoppingSleigh
- Analysis for 1.3
  - Seems unnecessary for refactoring of X for Y -> deferred
- Analysis for 1.1.4.6
  - TWO_FOR_AMOUNT and FIVE_FOR_AMOUNT have a different semantic than TWO_FOR_THREE
    - X_FOR_AMOUNT: AMOUNT is placeholder for discounted price
    - TWO_FOR_THREE: THREE is the amount of items gained when paying for TWO items
  - TWO_FOR_THREE is a special case of X_FOR_AMOUNT, where AMOUNT is a multiple of the unit price
  - => new items
- 1.1.6:
  - Will stop recording whole tree, as value of the tree structure has been very limited so far, in this case
  - will keep recording things I want to remember for later as an unstructured list
- chose not to inherit ThreeForTwoOffer from ItemBundleForDiscountedPriceOffer, as the discounts feel different from the outside
- will call it a day, as main task is done - was fun, would like to continue, but it's getting late
- copilot wow moment: "Generate similar tests for TWO_FOR_AMOUNT and FIVE_FOR_AMOUNT:" 
  - with test code for "2 for 1" & "3 for 2"
  - added a few files as context
  - copilot created tests
    - semantically correct
    - needed a little syntactic improvement (around missing builder methods)