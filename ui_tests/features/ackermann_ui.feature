Feature: A user may calculate ackermann's function through the UI

    As a user of the system
    I want to be able to calculate ackermann's function in the UI
    So that I am easily able to do so

  # This user story relates to behavior from a UI-centric point of view.  See the other Ackermann feature for more detail.

    Scenario: Calculating Ackermann's function in the UI
        When I calculate ackermann's function with an m of 3 and an n of 2
        Then Ackermann's function indicates that the result is 29

