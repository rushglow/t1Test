package org.batukhtin.t1test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class T1TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(T1TestApplication.class, args);
    }

}
