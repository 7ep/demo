Feature: Calculation of Ackermann's formula

    As a mathematically-oriented control-system designer
    I want to create controllers to alter poles using Ackermann's formula
    So that the pole placement is efficient

    Narrative:
        From Wikipedia (https://en.wikipedia.org/wiki/Ackermann%27s_formula):
        Ackermann's formula is a control system design method for solving the pole allocation problem.
        One of the primary problems in control system design is the creation of controllers that will alter
        the dynamics of a system and alter the poles to a more suitable, and sometimes more stable, state.
        Such a problem can be tackled by many different methods; one such solution is the addition of a
        feedback loop in such a way that a gain is added to the input with which one can change the poles
        of the original system. If the system is controllable, an efficient method for pole placement is
        Ackermann's formula, which allows one to choose arbitrary poles within the system.

    Scenario Outline: I should be able to calculate a few results per Ackermann's formula
        When I calculate Ackermann's formula using <m> and <n>
        Then the Ackermann result is <result>
        Examples:
          |  m    |    n        |     result    |
          |  0    |    0        |       1       |
          |  1    |    1        |       3       |
          |  1    |    2        |       4       |