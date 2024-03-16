package com.awesome.bank.event.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "bi-events")
public class BiProperties {
  /** BI instrument configuration */
  private Integer eventsSizeLimit;

}
