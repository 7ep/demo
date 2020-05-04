Feature: A user may login to the system.

    As a registered user of the system,
    I want to be able to login,
    so I can access the system's capabilities.

    # See register_user.feature for the registration portion of authentication

    Scenario: Entering proper credentials authenticates a user to the system.
        Given "adam" is registered in the system with the password "LpcVWwRkWSNVH"
        When when a user authenticates with "adam" and "LpcVWwRkWSNVH"
        Then The system decides that they are authenticated.

    Scenario Outline: Entering invalid credentials fails to authenticate a user to the system.
        Given "adam" is registered in the system with the password "LpcVWwRkWSNVH"
        When when a user authenticates with "<username>" and "<password>"
        Then The system decides that they are not authenticated, because <note>
        Examples:
            | username | password      | note                                        |
            | adam     | lpcvwwrkwsnvh | the password is correct, but all lower-case |
            | adamee   | LpcVWwRkWSNVH | we used an incorrect username               |
            | adam     | LpcVWwR       | we used a shortened version of the password |
            | ALICE    | LpcVWwRkWSNVH | the username was made all upper-case        |