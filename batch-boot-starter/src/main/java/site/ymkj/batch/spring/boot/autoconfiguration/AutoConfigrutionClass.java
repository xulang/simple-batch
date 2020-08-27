package site.ymkj.batch.spring.boot.autoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.ymkj.batch.core.DefaultBatchManager;
import site.ymkj.batch.core.IBatchManager;
import site.ymkj.batch.core.IBatchStore;
import site.ymkj.batch.core.config.BatchConfig;
import site.ymkj.batch.core.store.MemoryStore;

@Configuration
@EnableConfigurationProperties(AutoConfigruationProperties.class)
public class AutoConfigrutionClass {


  @Autowired
  private AutoConfigruationProperties autoConfigruationProperties;

  @Autowired
  private IBatchStore batchStore;

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(IBatchStore.class)
  @ConditionalOnClass(DefaultBatchManager.class)
  public IBatchManager batchManager() {
    if (autoConfigruationProperties.getBatchConfig() != null) {
      return new DefaultBatchManager(batchStore, autoConfigruationProperties.getBatchConfig());
    } else {
      return new DefaultBatchManager(batchStore, new BatchConfig());
    }
  }

  @ConditionalOnMissingBean
  @Bean
  @ConditionalOnClass(MemoryStore.class)
  public IBatchStore batchStore() {
    return new MemoryStore();
  }
}