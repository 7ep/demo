Feature: As a potential user of the system, I want to be able to register an account for myself, so I can access its features.

Narrative: A user wants to access the system, but they can only do it if they have
  an account on it.  In order to obtain an account, they must provide a username
  and a password.  The username must not have already been registered, and the
  password needs to be sufficiently complex, or else they will be an easy target
  for some hacker.

  Scenario: A user registers themselves to the system
    Given a user "alice" is not currently registered in the system
    When they register with that username and use the password, "password123"
    Then they become registered

  Scenario Outline: A user is unable to register due to bad password
    Given a user is not currently registered in the system
    When they provide a poor password:
    | "pass" |
    | "123"  |
    Then they fail to register and the system indicates the failure

  Scenario: A user is unable to register due to the username already existing
    Given a username of "alice" is registered
    When a user tries to register with that same name
    Then the system indicates a failure to register
