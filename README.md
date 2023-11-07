<img src="assets/API-Banner.svg" alt="Made with Java" width="100%">
<br>
<img src="https://img.shields.io/badge/Made%20with-java-orange?style=for-the-badge&logo=Oracle" alt="Made with Java">


ConnectionAPI is a library, where you can perform rest request or do a socket connection for example

It will get bigger over time, but slowly, because it is just a side project

## Rest

### General Request

Requesting a sync raw JSON

```java
final JSONObject jsonObject = Rest.requestSync(new URL("<Your URL>"), HttpMethode.<Your Methode>)
        .asRawValue();
...
```

Requesting an async raw JSON

```java
Rest.request(new URL("<Your URL>"), HttpMethode.<Your Methode>)
        .whenComplete((restRequest, throwable) -> ...);
```

#### You can also get the JSON as a predefined Java Object

Java Object example class

```java
public record Data(
        Integer id,
        String name,
        String email
) {
}
```

Requesting the JSON as the Data object sync

```java
final Data data = Rest.requestSync(new URL("<Your URL>"), HttpMethode.<Your Methode>)
        .asJavaObject(Data.class)
        .get();
...
```

Requesting the JSON as the Data object async

```java
Rest.request(new URL("<Your URL>"), HttpMethode.<Your Methode>)
        .whenComplete((restRequest, throwable) -> {
            try {
                final Data data = restRequest.asJavaObject(Data.class).get();
                ...
            } catch (JsonProcessingException exception) {
                exception.printStackTrace();
            }
        });
```
