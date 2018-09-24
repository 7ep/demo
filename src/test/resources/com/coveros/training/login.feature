Feature: As a user of the system, I want to be able to login to my account, so I can access its features.

  Narrative: A user wants to access the system, but they can only do it if they have
  an account on it.  In order access their account, they must provide a username
  and a password.  The username must have already been registered.

  Scenario Outline: Entering proper credentials authenticates a user to the system.
    Given "alice" is registered in the system with the password "password123"
    When when a user authenticates with <username> and <password>
    Then The system decides that they are authenticated.
    Examples:
    | username      |    password      |
    | alice         | password123      |

  Scenario Outline: Entering invalid credentials fails to authenticate a user to the system.
    Given "alice" is registered in the system with the password "password123"
    When when a user authenticates with <username> and <password>
    Then The system decides that they are not authenticated.
    Examples:
      | username      |    password      |
      | alice         | bad_password     |
      | aliceee       | password123      |



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