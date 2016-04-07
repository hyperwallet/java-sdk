hyperwallet-java
================

```
<dependency>
    <groupId>com.hyperwallet</groupId>
    <artifactId>rest-r3-java-sdk</artifactId>
    <version>0.1.1</version>
</dependency>
```

For general usage, create a new ```Hyperwallet(username, password)``` - this is threadsafe - and call its top level methods
directly.  You can optionally pass in a program code as a third parameter and it will be used as a default if one is
not provided in direct calls.

```java
HyperwalletUser user = hyperwallet.getUser("usr-7d8d50f7-abc1-49a1-9616-29575314f385");

user.setFirstName("Bob").setLastName("Hope");

user = hyperwallet.updateUser(user);
```

Any commands that return a list will also include metadata; they do this by returning a ```HyperwalletList<?>``` object.  The actual
results will be contained within the ```data``` sub-structure.

Setters support chaining, and any errors will be shown by throwing a ```HyperwalletException``` which will contain an errorCode and an
errorMessage.

You can use JavaBean conventions or simply edit the public attributes of the objects directly, whichever best fits your project style.

Since we assume that you're going to be using this in a much larger project, all classes are prefaced with the word
Hyperwallet to avoid confusion.

To run the (trivial) tests, simply set the environment variables ```HYPERWALLET_USERNAME``` and ```HYPERWALLET_PASSWORD``` to appropriate values.