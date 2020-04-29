Tested with Visual Studio 2019 Community edition.

dependencies:
    Visual Studio 2019
    Chrome (https://www.google.com/chrome/)
    Chromedriver (https://chromedriver.chromium.org/downloads)

See README.md in the root directory for further detail

To run tests:

    First, make sure the Demo application is already running, check at http://localhost:8080/demo/

    Then, to run these tests,
        1. Open the solution with Visual Studio 2019
        2. On the toolbar, select Test -> Run -> All tests

    Alternate:
        On the command line, in the testproject directory, run:

            dotnet test