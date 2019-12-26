Contents of the src directory
-----------------------------

application code directory
--------------------------
main             : Code for the application

test code directories
---------------------
bdd_test         : Tests that use Cucumber to test at the feature level
integration_test : Primarily tests that hit the database, but really any integration test
selenified_tests : Tests that use Selenified
test             : Unit tests
api_tests        : tests that target the API's
ui_tests         : tests that target the UI




For your information:

  Why do we split up the test directories this way?

  A few reasons.  I'll borrow some words from Petri Kainulainen
  (slightly modified from https://www.petrikainulainen.net/programming/maven/integration-testing-with-maven/)

    Adding integration tests to a build has traditionally been a bit painful. I suspect that the reason for
    this is that the standard directory layout has only one test directory (src/test).

    If we want to use the standard directory layout and add integration tests to our build, we have two options:

    First, we can add our integration tests to the same directory as our unit tests. This is an awful idea because
    integration tests and unit tests are totally different beasts and this approach forces us to mix them. Also, if
    we follow this approach, running unit tests from our IDE becomes a pain in the ass. When we run tests, our IDE
    runs all tests found from the test directory. This means that both unit and integration tests are run. If we
    are “lucky”, this means that our test suite is slower than it could be, but often this means that our integration
    tests fail every time. Not nice, huh?

    Second, we can add our integration tests to a new module. This is overkill because it forces us to transform our
    project into a multi-module project only because we want to separate our integration tests from our unit tests.
    Also, if our project is already a multi-module project, and we want to write integration tests for more than one
    module, we are screwed. Of course we can always create a separate integration test module for each tested module,
    but it would be less painful to shoot ourselves in the foot.

    It is pretty clear that both of these solutions suck.

    The requirements of our build are:

    - Integration and unit tests must have separate source directories. The src/integration-test/java directory
    must contain the source code of our integration tests and the src/test/java directory must contain the
    source code of our unit tests.

    - Integration and unit tests must have different resource directories. The src/integration-test/resources
    directory must contain the resources of our integration tests and the src/test/resources directory must
    contain the resources of our unit tests.

    - Only unit tests are run by default.

    - It must be possible to run only integration tests.

    - If an integration test fails, it must fail our build.

  We continue with this pattern for the BDD tests and the Selenified tests.