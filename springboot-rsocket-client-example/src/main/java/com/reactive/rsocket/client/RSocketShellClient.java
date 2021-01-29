package com.reactive.rsocket.client;

import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@ShellComponent
public class RSocketShellClient {

    private static final String CLIENT = "Client";
    private static final String REQUEST = "Request";
    private static final String FIRE_AND_FORGET = "FireAndForget";
    private static final String STREAM = "Stream";
    private static final String FLIGHT_STREAM = "FlightStream";

    private static Disposable disposable;

    private final RSocketRequester rsocketRequester;

    // Use an Autowired constructor to customize the RSocketRequester and store a reference to it in the global variable
    @Autowired
    public RSocketShellClient(RSocketRequester.Builder rsocketRequesterBuilder, RSocketStrategies strategies) {
        // (1) 生成并存储一个标识此客户端实例的唯一ID
        String client = UUID.randomUUID().toString();
        log.info("Connecting using client ID: {}", client);

        // (2) 使用RSocketstrategies和新ClientHandler实例创建一个新对象SocketAcceptor
        SocketAcceptor responder = RSocketMessageHandler.responder(strategies, new ClientHandler());

        // (3) 使用RSocketRequesterBuilder来注册新的SocketAcceptor
        this.rsocketRequester = rsocketRequesterBuilder
                .setupRoute("shell-client")
                .setupData(client)
                .rsocketStrategies(strategies)
                .rsocketConnector(connector -> connector.acceptor(responder))
                .connectTcp("localhost", 7000)
                .block();

        // (4) 确保通过处理RSocketonClose()事件正确处理断开连接
        this.rsocketRequester.rsocket()
                .onClose()
                .doOnError(error -> log.warn("Connection CLOSED"))
                .doFinally(consumer -> log.info("Client DISCONNECTED"))
                .subscribe();
    }

    @ShellMethod("Send one request. One response will be printed.")
    public void requestResponse() throws InterruptedException {
        log.info("Sending one request. Waiting for one response...");
        this.rsocketRequester
                .route("request-response")
                .data(new Message(CLIENT, REQUEST))
                .retrieveMono(Message.class)
                .subscribe(message -> {
                    log.info("Response was: {}", message);
                });
    }

    @ShellMethod("Send one request. No response will be returned.")
    public void fireAndForget() throws InterruptedException {
        log.info("\nFire-And-Forget. Sending one request. Expect no response (check server log)...");
        this.rsocketRequester
                .route("fire-and-forget")
                .data(new Message(CLIENT, FIRE_AND_FORGET))
                .send()
                .subscribe(aVoid -> log.info("Fire-And-Forget ACK"));
    }

    @ShellMethod("Send one request. Many responses (stream) will be printed.")
    public void stream() {
        log.info("Request-Stream. Sending one request. Waiting for unlimited responses (Stop process to quit)...");
        disposable = this.rsocketRequester
                .route("stream")
                .data(new Message(CLIENT, STREAM))
                .retrieveFlux(Message.class)
                .subscribe(message -> log.info("Response received: {}", message));
    }

    @ShellMethod("Stop streaming messages from the server.")
    public void s() {
        if (null != disposable) {
            disposable.dispose();
        }
    }

    class ClientHandler {

        @MessageMapping("client-status")
        public Flux<String> statusUpdate(String status) {
            log.info("Client Connection {}", status);
            return Flux.interval(Duration.ofSeconds(5))
                    .map(index -> String.valueOf(Runtime.getRuntime().freeMemory()));
        }

        @MessageMapping("client-range")
        public Flux<String> range(String status) {
            log.info("Client Connection {}", status);
            return Flux.range(0, 1000)
                    .map(index -> String.valueOf(Runtime.getRuntime().freeMemory()));
        }

        @MessageMapping("client-request")
        public Flux<String> backPressure(String status) {
            log.info("Client Connection {}", status);
            return Flux.generate(() -> 0L,
                    (state, sink) -> {
                        sink.next(state);
                        return state + 1;
                    })
                    .map(String::valueOf);
        }
    }
}
