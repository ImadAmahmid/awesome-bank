package com.awesome.bank.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A spring boot application to test publication and consumption of messages through Kafka.
 */
@SpringBootApplication
public class KafkaDummyApp {
  public static void main(String... args) {
    SpringApplication.run(KafkaDummyApp.class, args);
  }
}
