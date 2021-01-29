package com.reactive.rsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <pre>
 *
 *    TCP
 *  RSocket
 * ~->~.
 *      \  shared +-------+
 *       `-~->-~->~ :7000 |
 *       | single | users <
 *       ,-<-~-<-~- :7000 |
 *      / connect.+-------+
 * -~<-'
 *  RSocket
 *    TCP
 *
 * </pre>
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
