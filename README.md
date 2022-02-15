[![Build Status](https://travis-ci.org/hyperwallet/java-sdk.png?branch=master)](https://travis-ci.org/hyperwallet/java-sdk)
[![Coverage Status](https://coveralls.io/repos/github/hyperwallet/java-sdk/badge.svg?branch=master)](https://coveralls.io/github/hyperwallet/java-sdk?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.hyperwallet/sdk.svg)]()

Hyperwallet REST SDK v2.3.0
===========================

A library to manage users, transfer methods and payments through the Hyperwallet v4 API.

For Hyperwallet v3 API calls, please use the latest SDK version 1.x.x. See [here|https://docs.hyperwallet.com/content/updates/v1/rest-api-v4] to learn 
about the differences between versions and the update process required to use REST API v4.
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
    <version>2.3.0</version>
</dependency>
```

**Gradle**
```
compile 'com.hyperwallet:sdk:2.3.0'
```

Documentation
-------------

Documentation is available at http://hyperwallet.github.io/java-sdk.


API Overview
------------

To write an app using the SDK

* Register for a sandbox account and get your username, password and program token at the [Hyperwallet Program Portal](https://portal.hyperwallet.com).

* Add dependency `com.hyperwallet:sdk:2.3.0` to your `pom.xml` (or `build.gradle`).


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

Hyperwallet’s Payload Encryption is an implementation of Javascript Object Signing and Encryption (JOSE) and JSON Web Tokens (JWT), and provides an alternative to IP allowlisting as the second factor of authentication. Please see https://docs.hyperwallet.com/content/api/v4/overview/payload-encryption for more details.

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

Hyperwallet's API client provide's support for proxy configuration.

To enable proxy support, an appropriate proxy configuration must be provided. It can either be provided as a Proxy object or as a String and Integer representing the URL and Port of the proxy.

After creating an instance of the Hyperwallet Client, run a command similar to the one below
  ```java
  client.setHyperwalletProxy("proxyURL", 9090);
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
