# Advent of Craft 2024 - Day 5

## Before implementing

- Skimmed through the Canon TDD article, but never read it fully and never tried -> love the opportunity

### Observations about Canon TDD:
- Not used to working with a pre-written test list - curious how that turns out
- Does not prescribe baby steps, as far as I can tell. Will try whole test as step-width
- I like how interface/implementation split is highlighted. Will try to leak as little implementation into the red phase as possible

### EID task:
- I loved the other days focussing on refactoring or testing aspects without much prod implementation, but greenfield is great for a change :-D
- Main task: *Validation* of Elf Ids (EID)
  - Components of EID: Sex, Year of Birth, Serial # / Birth order, Control Key
  - Several interesting questions about the EID come to my mind, but most have nothing to do with validation. Will list some here, for my own and your amusement ;-)
    - What about non-ternary gender?
    - Can Elves be older than 100 years? In that case, 2 digits for year of birth would not suffice to ensure uniqueness
      - Can elves die at all? 
    - Are there Elf-Twins? How would birth order be handled in this case
    - Can two Elves be born at the same time?
    - Is the birth number per year, or is the population of elves limited to 999?
    - Are EIDs freed up on death, with some delay, or not at all? The latter would present a huge problem in 100 years!
  - Back to validation (with a test list):
    - Valid EID for each Sex
    - Invalid EID due to sex > 3
    - Invalid EID due to sex = 0
    - Valid EID for some or all years (property based?)
      - No invalid cases for years (at least, if digits of EID are restricted to numbers)
    - (Design choice: Are EIDs Strings or numbers? Or a number for each field?)
    - Valid EIDs with Valid Serial numbers (Some? all?, probably property based)
    - Invalid EID due to serial number = 0
    - Valid EIDs with control key matching previous digits modulo 97
      - Might first need to test-drive forming the number of first 6 digits
      - concept not entirely clear yet, checking example EID:
        - 198007 % 97 = 30 => not just modulo
        - complement to 97 => 97 - x => 97 - 30 = 67 ☑
      - Some examples of calculating the control key
        - maybe individual test for modulo 
        - maybe individual test for complement
        - might also be candidates for "fake it till you make it" / triangulation
    - Invalid EIDs due to control key not matching
  - Will give github copilot a try, as ai assistant was very disappointing

## Implementation

- New First test discovered: Sample EID is valid
  - Choice: EID from individual fields numbers or string?
    - individual numbers? feels complicated
    - string? seems simpler, but opens up a plethora of failure modes
      - take string now and introduce parsing code? seems complicated as well
    - whole number? simplest external interface, but with fear of complicating implementation
    - => lean into interface / impl separation and use whole number
- Test x: Compute control key from first six digits (new, deferred)
  - Which one? In order to craft more tests, being able to compute valid control keys would be nice
  - Design question: What would be the input for computing the control key?
    - Option: Split EID into data and validation?
    - Option: Function for extracting first six digits from whole EID number?
      - => Test for that first!
  - New test discovered: Invalid EIDs due to more than 8 digits
  - New test discovered: Invalid EIDs due to less than 8 digits
- Test 2: Extract first six digits from full EID (new)
  - observation: fluidly switching to testing an aspect I see on EID, rather than on the validator
  - observation: reasoning about eid parts will need accessors for them -> new tests
- Test 3: Compute control key from first six digits
  - decided to experiment with nested tests in DescribeSpec
  - split tests into EID and EidValidator ☑
- Test 4: EID: get control key
- Test 5: Invalid EIDs due to control key not matching

- Open tests:
  - EID: get gender
  - EID: get birth year
  - EID: get serial number
  - Valid EID for each Sex
  - Invalid EID due to sex > 3
  - Invalid EID due to sex = 0
  - Valid EID for some or all years (property based?)
    - No invalid cases for years (at least, if digits of EID are restricted to numbers)
  - (Design choice: Are EIDs Strings or numbers? Or a number for each field?)
  - Valid EIDs with Valid Serial numbers (Some? all?, probably property based)
  - Invalid EID due to serial number = 0
  - Invalid EIDs due to more than 8 digits
  - Invalid EIDs due to less than 8 digits
