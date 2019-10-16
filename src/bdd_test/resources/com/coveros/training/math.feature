Feature: Adding of numbers

    As a user who has trouble with doing math in their head,
    I want to add some numbers
    so I know the sums

    Scenario Outline: Add two numbers <num1> and <num2>
        Given my website is running and can do math
        When I add <num1> to <num2>
        Then the result should be <total>
        Examples:
            | num1 | num2 | total |
            | 2    | 3    | 5     |
            | 9    | 6    | 15    |
            | 2    | -3   | -1    |

