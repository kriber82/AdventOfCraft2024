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
        - [ ] 1.1.4 Make different X for Y logics as similar as possible
          - [x] 1.1.4.1 Introduce discountItemsGiven & discountItemsPaid
          - [x] 1.1.4.2 Use discountItemsGiven & discountItemsPaid
          - [x] 1.1.4.3 Inline variables so that all variants have the same equation
          - [x] 1.1.4.4 Reorder terms in first variant equation to see if all equations are equal  
          - [x] 1.1.4.5 Rename terms to follow the same naming
          - [x] 1.1.4.6 Check potential bug due to additional * unitPrice in THREE_FOR_TWO
          - [ ] 1.1.4.7 Adapt THREE_FOR_TWO code to be able to use X_FOR_AMOUNT logic (introduce priceForAmount or similar)
          - [ ] 1.1.4.8 Extract common logic for X_FOR_AMOUNT
          - [ ] 1.1.4.9 Improve names for extracted code
          - [ ] 1.1.4.? ?
        - [ ] 1.1.? Extract similarities
        - [ ] 1.1.3 Separate X for Y logic from other discounts
    - [ ] 👍 1.2 Implement the `Two for one` discount computation
        - [ ] ...
    - [ ] 👍 1.3Refactor the existing code to use the `X for Y` discount computation method with the `Three for two` discount
- [ ] 2 Parking-Lot (any change with no direct impact on the main goal)
    - [ ] 2.1 Individual tests look very long
    - [ ] 2.2 Improve (internal) discount names to reflect new understanding of discount types better
    - [ ] 2.3 Talk to PO whether we can also improve external discount names & printouts

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