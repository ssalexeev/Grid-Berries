## Difference between Statement and PreparedStatement

Statement  hasn't sql pre-compilation that make it more slower then PreparedStatement. 
Otherwise, Statement  has high risk of SQL-Injection in turn PreparedStatement exclude it and 
is absolutely safe. 
In PreparedStatement  users use input binding with "?" placeholders which automatically escapes inputs
In additionally, PreparedStatement is faster for repeated execution which different parameters due to plan caching.
Finally, Statement is about static DDL but PreparedStatement is about DML/Queries with user parameters.

## Simple SQL-Injection

```Java
Statement stmt = connection.createStatement();
String userId = "1 OR 1=1"; // Example payload

// Vulnerable to SQL Injection; database must parse this exact SQL string from scratch
String sql = "SELECT * FROM users WHERE id = " + userId;
ResultSet rs = stmt.executeQuery(sql);
```

**Result**: user gets all users-table data

**PreparedStatement example:**
```java
// Query structure is pre-compiled by the database using '?' placeholders
String sql = "SELECT * FROM users WHERE id = ?";
PreparedStatement pstmt = connection.prepareStatement(sql);

// Parameter is bound safely as data, NOT executable SQL code
pstmt.setInt(1, 1);
ResultSet rs = pstmt.executeQuery();
```


