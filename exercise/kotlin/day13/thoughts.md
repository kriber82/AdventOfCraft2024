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
          - [ ] 1.1.4.3 Inline variables so that all variants have the same equation
          - [ ] 1.1.4.? ?
        - [ ] 1.1.? Extract similarities
        - [ ] 1.1.3 Separate X for Y logic from other discounts
    - [ ] 👍 1.2 Implement the `Two for one` discount computation
        - [ ] ...
    - [ ] 👍 1.3Refactor the existing code to use the `X for Y` discount computation method with the `Three for two` discount
- [ ] 2 Parking-Lot (any change with no direct impact on the main goal)
    - [ ] 2.1 Individual tests look very long

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