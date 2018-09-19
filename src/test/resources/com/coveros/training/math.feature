Feature: 
  As a user who has trouble with doing math in their head, I want to add some numbers so I know the sums

  Scenario: Sums of happy path numbers
    When I add number_a to number_b, I get a sum:
      | 2    |   3     |  5  |
      | 9    |   6     |  15 |
      | 2    |   -3    | -1  |
