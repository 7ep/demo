Feature: A user may authenticate to the system through the UI

    As a user of the system
    I want to be able to authenticate myself
    So that I can use its features securely

    # This user story relates to behavior from a UI-centric point of view.
    # Note that we want as few UI tests as possible.  Logic should be tested lower down, this
    # is just to test the UI

    Scenario: Registering a new user
        Given I am not registered
        When I register with a valid username and password
        Then it indicates I am successfully registered

    Scenario: Login with valid user
        Given I am registered as a user
        When I login
        Then the system allows secure access

