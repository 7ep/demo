These are the backups of the library database, used during integration
testing.  Notice the naming scheme - each starts with the version of the
database, which originates from the Flyway scripts - see that folder.

We would always want to keep the version of our backups the same as the
version of the database migration scripts because our integration tests
assume a correct and current schema. That is, unless, however, we are specifically
testing that a database script successfully transitions the schema without
corrupting data, which is a valid and common concern during development.