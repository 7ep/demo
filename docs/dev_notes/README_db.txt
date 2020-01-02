The database we are using in the project is H2.  It's perfect for our
purposes for the following reasons:

* Stable and widely used
* Small - no install needed, can be pulled in as a dependency
* Can use Postgresql SQL syntax, so switching to Postgresql wouldn't be too hard
* Comes with all the tools you need, including a web-based db viewer

Nevertheless, there are subtleties to using H2. For example, we use it
in both in-memory and file-based modes.  

We use file-based when we need something persistent between processes.
When Tomcat is running, its process keeps going and we can access its
data through a web app on that same Tomcat.  However if we run unit
tests, the JUnit runner will wrap up and all the data is lost.  If we
want to manipulate data related to a particular test, or to create
backups of new test data, this is the easiest way.

The issue with file-based database is simple - where do we store the
database?  Users will be using this application on multiple
architectures and in multiple scenarios.  If we are running on Tomcat
and use in-memory, we simply don't have to worry about where the
database file is located.

If we are running unit tests, the database file is located in
build/db/training.mv.db
