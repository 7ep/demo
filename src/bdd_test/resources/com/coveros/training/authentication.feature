Feature: A user may authenticate to system.

    As a user of the system,
    I want to be able to authenticate myself,
    so I can access its capabilities.

    Narrative: A user wants to access the system, but they can only do it if they have
    an account on it.  To register an account, they simply provide
    a username and a password.  The password should be sufficiently complex, and the
    username must not have already been registered.  Later, when they want to
    authenticate themselves, they must enter that same username and password.

    @login
    Scenario: Entering proper credentials authenticates a user to the system.
        Given "adam" is registered in the system with the password "LpcVWwRkWSNVH"
        When when a user authenticates with "adam" and "LpcVWwRkWSNVH"
        Then The system decides that they are authenticated.

    @login
    Scenario Outline: Entering invalid credentials fails to authenticate a user to the system.
        Given "adam" is registered in the system with the password "LpcVWwRkWSNVH"
        When when a user authenticates with "<username>" and "<password>"
        Then The system decides that they are not authenticated, because <note>
        Examples:
            | username | password      | note                                        |
            | adam     | lpcvwwrkwsnvh | the password is correct, but all lower-case |
            | adamee   | LpcVWwRkWSNVH | we used an incorrect username               |
            | adam     |               | we used an empty password                   |
            | adam     | LpcVWwR       | we used a shortened version of the password |
            | ALICE    | LpcVWwRkWSNVH | the username was made all upper-case        |

    @registration
    Scenario: A user registers themselves to the system with a good password
        Given a user "adam" is not currently registered in the system
        When they register with that username and use the password "lpcvwwrkwsnvh"
        Then they become registered

   # The following is a scenario that is extra documentation for the team.
   # It helps  make concrete the reasons why the system requires more difficult passwords.

   # we are ignoring this feature for now because it turns out that the library
   # we are using, Nbvcxz, is turtle slow.  Once I figure out a way to
   # handle that problem, this feature can be re-vitalized.
    @registration
    Scenario Outline: A user might try different passwords, but we are making sure they are excellent before we allow it.
        Given a user is in the midst of registering for an account
        When they try registering with the password <password>
        Then the system returns that the password has insufficient entropy, taking this long to crack: <time_to_crack>
        Examples:
            | password                | time_to_crack |
            | typical_password_123    | 1 hours       |
            | aaaaaa                  | instant       |
            | password123             | instant       |
            | really_totally_long_wut | 564 centuries |


    @registration
    Scenario Outline: A user is unable to register due to blatantly bad password
        Given a user "adam" is not currently registered in the system
        When they enter their username and provide a poor password of <password>
        Then they fail to register and the system indicates a response: <response>
        Examples:
            | password | response       |
            | a        | too short      |
            | 123      | too short      |
            | aaaaa    | too short      |
            |          | empty password |


    @registration
    Scenario: A user is unable to register due to the username already existing
        Given a username of "adam" is registered
        When a user tries to register with that same name
        Then the system indicates a failure to register