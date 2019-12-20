# qovery-java-sdk

Get Qovery instance
```$java
Qovery qovery = new Qovery();

DatabaseConfiguration db = qovery.getDatabaseConfiguration("my-pql");

String host = db.getHost();
int port = db.getPort();
String username = db.getUsername();
String password = db.getPassword();
```

