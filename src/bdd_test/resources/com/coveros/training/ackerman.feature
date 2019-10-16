Feature: Calculation of Ackermann's function

    As a cool computer kid
    I want to calculate the result of Ackermann's function for various values
    So that I can see an early example of a total computable function

    Narrative:
        from Wikipedia (https://en.wikipedia.org/wiki/Ackermann_function)
        In computability theory, the Ackermann function, named after Wilhelm Ackermann, is one of the simplest
        and earliest-discovered examples of a total computable function that is not primitive recursive. All primitive
        recursive functions are total and computable, but the Ackermann function illustrates that not all total
        computable functions are primitive recursive.

        After Ackermann's publication[2] of his function (which had three nonnegative integer arguments), many
        authors modified it to suit various purposes, so that today "the Ackermann function" may refer to any
        of numerous variants of the original function.

        from Rosetta Code (https://rosettacode.org/wiki/Ackermann_function)
        The Ackermann function is a classic example of a recursive function, notable especially because it
        is not a primitive recursive function. It grows very quickly in value, as does the size of its call tree.

    Scenario Outline: I should be able to calculate a few results per Ackermann's formula
        When I calculate Ackermann's formula using <m> and <n>
        Then the Ackermann result is <result>
        Examples:
          |  m    |    n        |     result    |
          |  0    |    0        |       1       |
          |  1    |    1        |       3       |
          |  1    |    2        |       4       |