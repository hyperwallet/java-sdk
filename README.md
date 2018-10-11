[![Build Status](https://travis-ci.org/hyperwallet/java-sdk.png?branch=master)](https://travis-ci.org/hyperwallet/java-sdk)
[![Coverage Status](https://coveralls.io/repos/github/hyperwallet/java-sdk/badge.svg?branch=master)](https://coveralls.io/github/hyperwallet/java-sdk?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.hyperwallet/sdk.svg)]()

Hyperwallet REST SDK (Beta)
===========================

A library to manage users, transfer methods and payments through the Hyperwallet API


Installation
------------

**Maven**
```
<dependency>
    <groupId>com.hyperwallet</groupId>
    <artifactId>sdk</artifactId>
    <version>0.4.0</version>
</dependency>
```

**Gradle**
```
compile 'com.hyperwallet:sdk:0.4.0'
```

Documentation
-------------

Documentation is available at http://hyperwallet.github.io/java-sdk.


API Overview
------------

To write an app using the SDK

* Register for a sandbox account and get your username, password and program token at the [Hyperwallet Program Portal](https://portal.hyperwallet.com).
* Add dependency `com.hyperwallet:sdk:0.4.0` to your `pom.xml` (or `build.gradle`).

* Create a instance of the Hyperwallet Client (with username, password and program token)
  ```java
  Hyperwallet client = new Hyperwallet("restapiuser@4917301618", "mySecurePassword!", "prg-645fc30d-83ed-476c-a412-32c82738a20e");
  ```
* Start making API calls (e.g. create a user)
  ```java

  HyperwalletUser user = new HyperwalletUser();
  user
    .clientUserId("test-client-id-1")
    .profileType(HyperwalletUser.UserProfileType.INDIVIDUAL)
    .firstName("Daffyd")
    .lastName("y Goliath")
    .email("testmail-1@hyperwallet.com")
    .addressLine1("123 Main Street")
    .city("Austin")
    .stateProvince("TX")
    .country("US")
    .postalCode("78701");

  try {
      HyperwalletUser createdUser = client.createUser(user);
  } catch (HyperwalletException e) {
      // Add error handling here
  }
  ```


Development
-----------

Run the tests using [`maven`](https://maven.apache.org/):

```bash
$ mvn test
```


Reference
---------

[REST API Reference](https://sandbox.hyperwallet.com/developer-portal/#/docs)


License
-------

[MIT](https://raw.githubusercontent.com/hyperwallet/java-sdk/master/LICENSE)
