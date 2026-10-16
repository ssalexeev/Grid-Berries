## Difference between DriverManager and DataSource

## DriverManager
It's a legacy JDBC utility class. For new request it creates new connection that takes time and resources of system.
All DB properties is hardcoded and excluded to connection and application code, which is unsafe practice.
It usable only for small standalone projects and prototypes.

## DataSource
It's modern JDBC interface. It has different implementations for modern databases as PostgreSQL and Other.Additinolly, It
supports connection pooling from an internal Connection Pool. It's fast because connections are reused across operations.
Supports distributed transactions. Finally, properties configured centrally (for example, in spring properties)
