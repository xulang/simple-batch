package site.ymkj.batch.spring.boot.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import site.ymkj.batch.core.config.BatchConfig;

@ConfigurationProperties(prefix = "ymkj",ignoreUnknownFields = true)
public class AutoConfigruationProperties {

 private BatchConfig batchConfig;

  public BatchConfig getBatchConfig() {
    return batchConfig;
  }

  public void setBatchConfig(BatchConfig batchConfig) {
    this.batchConfig = batchConfig;
  }
}