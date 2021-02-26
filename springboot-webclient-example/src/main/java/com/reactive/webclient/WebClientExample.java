package com.reactive.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;

public class WebClientExample {

    private static final Logger logger = LoggerFactory.getLogger(WebClientExample.class);

    public static void main(String[] args) throws InterruptedException {
        WebClient webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(2 * 1024 * 1024))
                .build();

        Mono<String> body = webClient.get()
                .uri("http://www.baidu.com")
                .retrieve()
                .bodyToMono(String.class);

        CountDownLatch done = new CountDownLatch(1);

        Disposable disposable = body.subscribe(
                logger::info,
                throwable -> {
                    System.err.println(throwable.getMessage());
                    done.countDown();
                },
                done::countDown);

        // Wait for a signal to exit, then clean up
        done.await();
        disposable.dispose();
    }
}
