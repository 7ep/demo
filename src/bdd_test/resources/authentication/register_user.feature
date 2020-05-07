Feature: A user may register themselves to the system.

    As a potential user of the system,
    I want to be able to register myself,
    so that I can subsequently login to the system

#    Narrative: A user wants to access the system, but they can only do it if they have
#    an account on it.  To register an account, they simply provide
#    a username and a password.  The password should be sufficiently complex, and the
#    username must not have already been registered.  Later, when they want to
#    authenticate themselves, they must enter that same username and password.

    Scenario: A user registers themselves to the system with a good password
        Given a user "adam" is not currently registered in the system
        When they register with that username and use the password "lpcvwwrkwsnvh"
        Then they become registered

    Scenario Outline: A user might try different passwords, but we are making sure they are excellent before we allow it.
        Given a user is in the midst of registering for an account
        When they try registering with the password <password>
        Then the system returns that the password has insufficient entropy
        Examples:
            | password                |
            | typical_password_123    |
            | aaaaaaaaaa              |
            | password123             |
            | really_totally_long     |

    Scenario Outline: A user is unable to register due to blatantly bad password
        Given a user "adam" is not currently registered in the system
        When they enter their username and provide a poor password of <password>
        Then they fail to register and the system indicates a response: <response>
        Examples:
            | password | response       |
            | a        | too short      |
            | 123      | too short      |
            | aaaaa    | too short      |

    Scenario: A user is unable to register due to the username already existing
        Given a username of "adam" is registered
        When a user tries to register with that same name
        Then the system indicates a failure to register
