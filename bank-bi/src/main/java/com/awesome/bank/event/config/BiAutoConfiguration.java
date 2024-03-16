package com.awesome.bank.event.config;

import com.awesome.bank.event.BiEventConsumer;
import com.awesome.bank.event.BiEventPublisher;
import com.awesome.bank.event.BiEventSubscriber;
import com.awesome.bank.event.impl.DefaultBiEventConsumer;
import com.awesome.bank.event.impl.DefaultBiEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(BiProperties.class)
public class BiAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public BiEventConsumer biEventConsumer(BiEventPublisher biEventPublisher, BiProperties properties) {
    LOG.info("Creating bi event consumer bean.");
    int size = properties.getEventsSizeLimit();

    return new DefaultBiEventConsumer(biEventPublisher, size);
  }

  /**
   * Instantiate BiEvent publisher bean.
   * <p>
   * Collect the subscribers' implementation in spring context, and inject the collection to the
   * publisher bean.
   */
  @Bean
  @ConditionalOnMissingBean
  public BiEventPublisher biEventPublisher(List<BiEventSubscriber> subscribers) {
    LOG.info("Creating bi event publisher bean.");
    DefaultBiEventPublisher defaultBiEventPublisher = new DefaultBiEventPublisher();
    subscribers.stream()
            .peek(sub -> LOG.debug("[{}] subscribes to publisher", sub.getClass().getSimpleName()))
            .forEach(defaultBiEventPublisher::subscribe);
    return defaultBiEventPublisher;
  }

}
