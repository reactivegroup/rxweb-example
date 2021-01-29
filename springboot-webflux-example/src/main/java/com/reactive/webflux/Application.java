package com.reactive.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <pre>
 *                   users-command
 *                     +-------+
 *          COMMAND    | WRITE |
 *          HTTP ,>-~->~  SIDE ~>-~->~.
 *              /      | :8080 |       \
 *    _O_ -~->~'       +-------+        `
 *     |                                |
 *    / \ -<-~-.       +-------+        ,
 *              \      |  READ |       /
 *          HTTP `<-~-<~  SIDE <-~-~<-'
 *          QUERY      | :8080 |
 *          STREAM     +-------+
 *                    users-query
 * </pre>
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
