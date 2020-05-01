The contents of this folder provide code and documents to support a style of testing called Behavior-Driven Development (BDD).

The core aspects of this technique are:
- to use collaboration between at least the developers, testers, and business to build shared understanding
- to develop clear scenarios that help clarify user stories and better articulate the definition of done
- where possible, to consider automation for the scenarios
- builds on top of Test-Driven Development (TDD), emphasizes the essential capabilities

There are two folders here: java and resources.

Within the _resources_ directory, you will find the feature files.

In the _java_ directory, the step definitions - that is, the glue code that connects the prose in the feature
files to the actual code of the system.

Note that the different domains are separated into different directories - for example, all feature files and
step definitions related to the "library" functional domain are within the _library_ directory.