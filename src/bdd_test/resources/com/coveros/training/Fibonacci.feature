Feature: Calculation of Fibonacci numbers

    As an aspiring mathematically-oriented beekeeper
    I want to be able to calculate Fibonacci numbers
    So that I can count the idealized number of honeybees in my hive

    Narrative:
        from Wikipedia (https://en.wikipedia.org/wiki/Fibonacci_number)
        Fibonacci numbers are strongly related to the golden ratio: Binet's formula expresses the nth Fibonacci
        number in terms of n and the golden ratio, and implies that the ratio of two consecutive Fibonacci numbers
        tends to the golden ratio as n increases.

        Fibonacci numbers are named after Italian mathematician Leonardo of Pisa, later known as Fibonacci. They appear
        to have first arisen as early as 200 BC in work by Pingala on enumerating possible patterns of poetry formed
        from syllables of two lengths. In his 1202 book Liber Abaci, Fibonacci introduced the sequence to Western
        European mathematics,[6] although the sequence had been described earlier in Indian mathematics.[7][8][9]

        Fibonacci numbers appear unexpectedly often in mathematics, so much so that there is an entire journal
        dedicated to their study, the Fibonacci Quarterly. Applications of Fibonacci numbers include computer
        algorithms such as the Fibonacci search technique and the Fibonacci heap data structure, and graphs called
        Fibonacci cubes used for interconnecting parallel and distributed systems.

        They also appear in biological settings, such as branching in trees, the arrangement of leaves on a stem,
        the fruit sprouts of a pineapple, the flowering of an artichoke, an uncurling fern and the arrangement
        of a pine cone's bracts.

    Scenario Outline: Calculating some Fibonacci numbers
        When I calculate the <nth> Fibonacci number
        Then the Fibonacci result is <result>
        Examples:
        |   nth                    |    result       |
        |           0              |      0          |
        |           1              |      1          |
        |           2              |      1          |
        |           3              |      2          |
        |           20             |      6765       |