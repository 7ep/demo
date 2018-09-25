Feature: showing off behave

  Scenario: run a simple test
    Given we have behave installed
    When we implement a test
    Then behave will test it for us

  Scenario: run a simple ui test
    Given we have a web app available to run
    When we register to this web app with existing user
    Then it indicates already registered
