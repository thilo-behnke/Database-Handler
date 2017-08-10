# Database-Handler

A database handler written in Java 8.

I initially created this program to import data from txt files into a SQL database so that I can practice my SQL skills with enough test data.
In the end I spend more time on using Java 8 features such as streams and lambdas to reduce the code.

I further experimented with different creational patterns to make sure objects are created in a certain way.
In this case it came down to an enum-centric structure.

### Features
- Create and drop tables, insert database entities, query database entities, import txt files
- JUnit test coverage
- Supported databases: PostgreSQL

### Planned Features
- Make certain parts of the program more generic (e.g. FileReader)
- Add Swing interface