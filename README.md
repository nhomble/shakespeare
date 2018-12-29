shakespeare
===
A toy actor-model framework that makes no attempt to be better than akka.

```java
public class SpawnWaitExample {

    private static final Logger log = LoggerFactory.getLogger(SpawnWaitExample.class);

    public static void main(String... args) {
        Theatre theatre = Theatre.builder()
                .setName("name")
                .build()
                .start();

        theatre.spawn(WaitingActor.class, SpawnOptions.builder()
                .setAwaitConfirmation(true)
                .build());
        theatre.send(Message.builder()
                .setData("hello")
                .setTo(RootClientActor.REFERENCE.append(WaitingActor.class))
                .build());
        theatre.shutdown();
    }
}
```

```java
@Actor
public class WaitingActor {

    private static final Logger log = LoggerFactory.getLogger(WaitingActor.class);

    @OnInit
    void init() throws InterruptedException {
        log.info("start init");
        Thread.sleep(1_000);
        log.info("end init");
    }

    @OnMessage
    void handle(@Data String msg) {
        log.info("message={}", msg);
    }
}
```