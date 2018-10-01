Feature: As a user of the system, I want to be able to authenticate myself, so I can access its capabilities.

  Narrative: A user wants to access the system, but they can only do it if they have
  an account on it.  To register an account, they simply provide
  a username and a password.  The password should be sufficiently complex, and the
  username must not have already been registered.  Later, when they want to
  authenticate themselves, they must enter that same username and password.

  @login
  Scenario: Entering proper credentials authenticates a user to the system.
    Given "alice" is registered in the system with the password "LpcVWwRkWSNVH"
    When when a user authenticates with "alice" and "LpcVWwRkWSNVH"
    Then The system decides that they are authenticated.

  @login
  Scenario Outline: Entering invalid credentials fails to authenticate a user to the system.
    Given "alice" is registered in the system with the password "LpcVWwRkWSNVH"
    When when a user authenticates with "<username>" and "<password>"
    Then The system decides that they are not authenticated, because <note>
    Examples:
      | username      |    password     | note                                          |
      | alice         | lpcvwwrkwsnvh   | the password is correct, but all lower-case   |
      | aliceee       | LpcVWwRkWSNVH   | we used an incorrect username                 |
      | alice         |                 | we used an empty password                     |
      | alice         | LpcVWwR         | we used a shortened version of the password   |
      | ALICE         | LpcVWwRkWSNVH   | the username was made all upper-case          |

  @registration
  Scenario: A user registers themselves to the system with a good password
    Given a user "alice" is not currently registered in the system
    When they register with that username and use the password "lpcvwwrkwsnvh"
    Then they become registered

  @registration
  Scenario Outline: A user might try different passwords, but we are making sure they are excellent before we allow it.
    Given a user is in the midst of registering for an account
    When they try registering with the password <password>
    Then the system returns that the password has insufficient entropy, taking this long to crack: <time_to_crack>
    Examples:
    | password                | time_to_crack               |
    | typical_password_123    | 1 hours                     |
    | aaaaaa                  | instant                     |
    | password123             | instant                     |
    | really_totally_long_wut | 564 centuries               |

  @registration
  Scenario Outline: A user is unable to register due to blatantly bad password
    Given a user "alice" is not currently registered in the system
    When they enter their username and provide a poor <password>
    Then they fail to register and the system indicates the <response>
    Examples:
      | password              |   response            |
      | a                     |  too_short            |
      | 123                   |  too_short            |
      | aaaaa                 |  too_short            |
      |                       |  empty_password       |
      | password123           |  insufficient_entropy |


  @registration
  Scenario: A user is unable to register due to the username already existing
    Given a username of "alice" is registered
    When a user tries to register with that same name
    Then the system indicates a failure to register