Feature: As a user of the system, I want to be able to authenticate myself, so I can access its capabilities.

  Narrative: A user wants to access the system, but they can only do it if they have
  an account on it.  To register an account, they simply provide
  a username and a password.  The password should be sufficiently complex, and the
  username must not have already been registered.  Later, when they want to
  authenticate themselves, they must enter that same username and password.

  @login
  Scenario Outline: Entering proper credentials authenticates a user to the system.
    Given "alice" is registered in the system with the password "password123"
    When when a user authenticates with <username> and <password>
    Then The system decides that they are authenticated.
    Examples:
    | username      |    password      |
    | alice         | password123      |

  @login
  Scenario Outline: Entering invalid credentials fails to authenticate a user to the system.
    Given "alice" is registered in the system with the password "password123"
    When when a user authenticates with <username> and <password>
    Then The system decides that they are not authenticated.
    Examples:
      | username      |    password      |
      | alice         | bad_password     |
      | aliceee       | password123      |

  @registration
  Scenario: A user registers themselves to the system
    Given a user "alice" is not currently registered in the system
    When they register with that username and use the password, "password123"
    Then they become registered

  @registration
  Scenario: A user is unable to register due to bad password
    Given a user "alice" is not currently registered in the system
    When they provide a poor password:
      | aaa |
      | pass |
      | 123  |
    Then they fail to register and the system indicates the failure

  @registration
  Scenario: A user is unable to register due to the username already existing
    Given a username of "alice" is registered
    When a user tries to register with that same name
    Then the system indicates a failure to register


# Using the syntax above means we don't need to repeat ourselves like below:
#  Scenario: A user authenticates themselves to the system successfully
#    Given a user "alice" is registered in the system with the password, "password123"
#    When they login with that username and password
#    Then they are authenticated
#
#  Scenario: A user enters an invalid password for a valid username
#    Given a user "alice" is registered in the system with the password, "password123"
#    When they login with that username but a password of "bad_password"
#    Then they are not authenticated
#
#  Scenario: A user enters an invalid username when authenticating
#    Given a user "alice" is registered in the system with the password, "password123"
#    When they accidentally enter "aliceee" with their correct password
#    Then they are not authenticated