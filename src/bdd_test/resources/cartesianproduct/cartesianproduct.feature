Feature: Allow a user to generate a cartesian product from lists

    As a tester
    I want to calculate all combinations of items from multiple lists
    so that I can create a decision table to test all inputs

#    Narrative: a testing technique is to come up with all combinations of inputs
#    to a system in order to verify that all possibilities are accounted for.  Once
#    you have more than a couple inputs to consider, calculating all the combinations
#    is a tedious practice, which could easily lead to missing certain combinations.
#    This calculator will produce the correct result.

    Scenario: happy path - a typical set of lists
        Given lists as follows:
            | list    |
            | a,b     |
            | 1,2,3,4 |
            | e,f     |
        When we calculate the combinations
        Then the resulting combinations should be as follows:
            """
            (a, 1, e), (a, 1, f), (a, 2, e), (a, 2, f),
            (a, 3, e), (a, 3, f), (a, 4, e), (a, 4, f),
            (b, 1, e), (b, 1, f), (b, 2, e), (b, 2, f),
            (b, 3, e), (b, 3, f), (b, 4, e), (b, 4, f)
            """