# Overview
The DbAssist fixes for different versions of Hibernate are contained in the directories named in the following manner:

_DbAssist-[[Core Hibernate O/RM version](https://mvnrepository.com/artifact/org.hibernate/hibernate-core)]_

If the Hibernate version is preceded by `-hbm` prefix, it supports only Hibernate setup with HBM files (the case occurs for older Hibernate versions). If there is no `-hbm` prefix, the DbAssist fix version supports both JPA Annotations (with/without Spring Boot) and HBM files. For more information, refer to this [section](https://github.com/montrosesoftware/DbAssist#compatibility).

# DbAssist

DbAssist provides the fix for the unexpected date time shift. This issue does not occur at all on condition that all the components which are used in the project (servers, clients etc.) are set up in the same time zone. If at least one component has a different time zone (for example, we want to store dates in a database as UTC0, but leave the application servers in local time zone), then the unexpected time shift is experienced whenever read/write operation are performed. 

The project also introduces `ConditionsBuilder` class which enables the user to easily create complex logical combinations of conditions in the SQL query.

For more information about the issue and the detailed explanation of the problem you can refer to the following sources:
* the article on this [blog](TODO link to blog).
* DbAssist project [wiki page](https://github.com/montrosesoftware/DbAssist/wiki) 

## Installation of the fix

### Add the dependency

In order to fix the issue with date shift, you need to determine first if you want to use JPA annotations or .hbm files to map your entities. Depending on your choice, add the following dependency to your project's pom file and pick the correct version from the table in Compatibility section.

```xml
<dependency>
    <groupId>com.montrosesoftware</groupId>
    <artifactId>DbAssist-5.2.2</artifactId>
    <version>1.0-RELEASE</version>
</dependency>
```

### Apply the fix

The fix is slightly different for both entity mapping methods:

#### In HBM case:

You do **not** modify the `java.util.Date` type of dates fields in your entity class. However, you need to change the way how they are mapped in the `.hbm` file of your entities. You can do it by using our custom type, `UtcDateType`:

`ExampleEntity.hbm.xml`
```xml
<property name="createdAtUtc" type="com.montrosesoftware.dbassist.types.UtcDateType" column="created_at_utc"/>
```

`ExampleEntity.java` (not modified)
```java
public class ExampleEntity {

    private int id;
    private String name;
    private Date createdAtUtc;
   
    //setters and getters
}
```

#### In JPA case:

In case of JPA Annotations set up with Spring Boot, just add the `@EnableAutoConfiguration` annotation before the application class. If using plain Hibernate with `preferences.xml`, we need to add a single line of code in the [configuration](https://github.com/montrosesoftware/DbAssist/wiki#in-jpa-case) file.

The exception is when we are using Hibernate's `Specification` class to specify `WHERE` conditions. In order to fix it we have two options, which are described in details on the [wiki page](https://github.com/montrosesoftware/DbAssist/wiki#in-jpa-case)

## Maven repository

Please find below the most recent versions of the artifacts of the DbAssist project: 

| Artifact name     | Recent version       | 
| :---------------- |:--------------------:| 
| `DbAssist-hbm-3.3.2`  |[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-hbm-3.3.2/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-hbm-3.3.2) |
| `DbAssist-hbm-3.6.10` |[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-hbm-3.6.10/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-hbm-3.6.10) |
| `DbAssist-4.2.21`     |[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-4.2.21/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-4.2.21) |
| `DbAssist-4.3.11`     |[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-4.3.11/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-4.3.11) |
| `DbAssist-5.0.10`     |[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-5.0.10/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-5.0.10) |
| `DbAssist-5.1.1`      |[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-5.1.1/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-5.1.1) |
| `DbAssist-5.2.2`      |[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-5.2.2/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-5.2.2) |
| `DbAssist-jpa-commons`|[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-jpa-commons/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.montrosesoftware/DbAssist-jpa-commons)|

## Compatibility

### Hibernate
The list of supported Hibernate versions and their fix counterparts is in the table below:

| Hibernate version | HBM                  | JPA    |
| :---------------- |:--------------------:| :-----:|
| 3.3.2.GA          | `DbAssist-hbm-3.3.2` | N/A |
| 3.6.10.Final      | `DbAssist-hbm-3.6.10`| N/A |
| 4.2.21.Final      | `DbAssist-4.2.21`    | `DbAssist-4.2.21`|
| 4.3.11.Final      | `DbAssist-4.3.11`    | `DbAssist-4.3.11`|
| 5.0.10.Final      | `DbAssist-5.0.10`    | `DbAssist-5.0.10`|
| 5.1.1.Final       | `DbAssist-5.1.1`     | `DbAssist-5.1.1` |
| 5.2.2.Final       | `DbAssist-5.2.2`     | `DbAssist-5.2.2` |

### JDBC SQL Driver
* `4.0`
* `4.1`
* `4.2`

## Usage of `DbAssist-jpa-commons` library

```java
ConditionsBuilder cb = new ConditionsBuilder();

//prepare conditions
Condition c1 = cb.lessThan("id", 15);
Condition c2 = cb.equal("name", "Mont");
...
Condition c5 = ...

//construct logical expression
Condition hc =
or(
        and(c1, c2),
        or(c3, and(c4, c5))
);

//apply the conditions hierarchy to the conditions builder
cb.apply(hc);

List<User> users = uRepo.find(cb);
```

Result:
```sql
WHERE (c1 AND c2) OR c3 OR (c4 AND c5)
```

More examples and the tutorial for DbAssist library is available on the [wiki page](https://github.com/montrosesoftware/DbAssist/wiki)

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## License

The MIT License (MIT)
Copyright (c) 2016 Montrose Software

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
