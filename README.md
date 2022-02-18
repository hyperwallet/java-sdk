[![Build Status](https://travis-ci.org/hyperwallet/java-sdk.png?branch=master)](https://travis-ci.org/hyperwallet/java-sdk)
[![Coverage Status](https://coveralls.io/repos/github/hyperwallet/java-sdk/badge.svg?branch=master)](https://coveralls.io/github/hyperwallet/java-sdk?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.hyperwallet/sdk.svg)]()

Hyperwallet REST SDK (Beta)
===========================

A library to manage users, transfer methods and payments through the Hyperwallet v3 API.

Version 2.0.0 and higher are for use with Hyperwallet v4 API only. See [here|https://docs.hyperwallet.com/content/updates/v1/rest-api-v4] to learn about the differences between versions and the update process required to use REST API v4.


Prerequisites
------------

Hyperwallet's Java server SDK requires at minimum JDK (Java Development Kit) version 1.7 and above.

Installation
------------

**Maven**
```
<dependency>
    <groupId>com.hyperwallet</groupId>
    <artifactId>sdk</artifactId>
    <version>1.9.0</version>
</dependency>
```

**Gradle**
```
compile 'com.hyperwallet:sdk:1.9.0'
```

Documentation
-------------

Documentation is available at http://hyperwallet.github.io/java-sdk.


API Overview
------------

To write an app using the SDK

* Register for a sandbox account and get your username, password and program token at the [Hyperwallet Program Portal](https://portal.hyperwallet.com).
* Add dependency `com.hyperwallet:sdk:1.9.0` to your `pom.xml` (or `build.gradle`).

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

**Payload Encryption**

Hyperwallet’s Payload Encryption is an implementation of Javascript Object Signing and Encryption (JOSE) and JSON Web Tokens (JWT), and provides an alternative to IP allowlisting as the second factor of authentication. Please see https://docs.hyperwallet.com/content/api/v3/overview/payload-encryption for more details.

To enable payload encryption, we need the following two keysets available:
* Hyperwallet's public keyset - https://api.paylution.com/jwkset (PRODUCTION) and https://uat-api.paylution.com/jwkset (UAT)
* Client private keyset

Create a HyperwalletEncryption object providing the two keyset locations, the JWS/JWE algorithms you want to use, and the encryption method.
  ```java
  HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryptionBuilder()
      .encryptionAlgorithm(JWEAlgorithm.ECDH_ES)
      .encryptionMethod(EncryptionMethod.A256CBC_HS512)
      .signAlgorithm(JWSAlgorithm.ES256)
      .hyperwalletKeySetLocation("src/main/resources/hw-uat.json")
      .clientPrivateKeySetLocation("src/main/resources/client-private-keyset.json")
      .build();
  ```
Initialize the Hyperwallet Client with the created HyperwalletEncryption object
  ```java
  Hyperwallet client = new Hyperwallet("restapiuser@4917301618", "mySecurePassword!", "prg-645fc30d-83ed-476c-a412-32c82738a20e", hyperwalletEncryption);  
  ```
API requests will now be signed using the private key matching the selected JWS algorithm, and encrypted using Hyperwallet's public key matching the selected JWE algorithm.


**Proxy Support**

Hyperwallet's API client supports a connection through a proxy. To enable, an appropriate proxy configuration must be provided. It can either be provided as a Proxy object or as a String and Integer representing the URL and Port of the proxy.

A proxy can be configured after creating an instance of the Hyperwallet Client.
  ```java
  client.setHyperwalletProxy("proxyURL", 9090);
  ```

To enable Proxy Authorization, proper credentials and System configurations must be provided. In order to connect using Basic Auth, Basic must be an allowed tunneling method within your Java System Properties. By default, it is listed as a disabled tunneling scheme for Java 8. To enable it, follow any of the options provided below:

* Manually remove Basic from the `jdk.http.auth.tunneling.disabledSchemes` property inside of JAVA_HOME/jre/lib/net.properties
* Override System properties with JVM options by providing the below option
  ```bash
  -Djdk.http.auth.tunneling.disabledSchemes=""
  ```
* Set the System property within your code before initializing the Hyperwallet API client

  ```java
  System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
  Hyperwallet client = new Hyperwallet("restapiuser@4917301618", "mySecurePassword!", "prg-645fc30d-83ed-476c-a412-32c82738a20e");
  ```
  
An example of a fully configured Proxy is provided below:

  ```java
  System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
  Hyperwallet client = new Hyperwallet("restapiuser@4917301618", "mySecurePassword!", "prg-645fc30d-83ed-476c-a412-32c82738a20e");
  client.setHyperwalletProxy("proxyURL", 3128);
  client.setHyperwalletProxyUsername("proxyUsername");
  client.setHyperwalletProxyPassword("proxyPassword");
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
