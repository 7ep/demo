Feature: 
  As a user of the system, I want to be able to register an account for myself on it, so I can access its features securely.

  Scenario: Using a happy path username and password
    When I register with these names and passwords:
      | alice | password1 |
      | bob   | password2 |
    Then a new account is created for me
