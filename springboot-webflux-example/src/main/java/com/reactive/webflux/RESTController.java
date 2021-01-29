package com.reactive.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Http Rest controller.
 */
@Slf4j
@RestController
public class RESTController {

    /**
     * Echo mono.
     */
    @GetMapping("/mono")
    public Mono<String> version(String echo) {
        log.info("echo: {}", echo);
        return Mono.just("hello" + echo);
    }

    /**
     * String flux.
     */
    @GetMapping("/flux")
    public Flux<String> flux() {
        log.info("before: " + Thread.currentThread().getName());
        Flux<String> flux = Flux.generate(sink -> {
            sink.next("hello<br>");
        });
        log.info("after: " + Thread.currentThread().getName());
        flux = flux.limitRate(1)
                // .delayElements(Duration.ofSeconds(1))
                .doOnNext(s -> {
                    log.info("next: " + Thread.currentThread().getName());
                });
        return flux;
    }
}
