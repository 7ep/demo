Feature: Authenticating to the system
  As a user of the system
  I want to be able to authenticate myself
  So that I can use its features securely.

  # This user story relates to behavior from a UI-centric point of view.

  Scenario: Registering a new user
    Given I am not registered
    When I register with a username of "Byron" and a password of "OAh8Wq8CajGZwURSXI8uSS"
    Then it indicates I am succesfully registered

  Scenario: Registering as an existing user (negative case)
    Given I am registered as "Byron"
    When I try to register again
    Then it indicates I am already registered

  Scenario: Registering with a poor password (negative case)
    Given I am not registered
    When I register with a username of "Byron" and a password of "simplybad"
    Then it indicates that I used a poor password

  Scenario: Login with valid user
    Given I am registered with a username of "Byron"
    When I login with proper credentials
    Then the system allows secure access

  Scenario: Login with invalid user (negative case)
    Given There is no user with the username "Robert"
    When I login with that username and the password "test"
    Then the system denies me access
