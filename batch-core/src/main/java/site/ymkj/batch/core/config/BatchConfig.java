package site.ymkj.batch.core.config;

import lombok.Data;

/**
 * 批处理配置
 */
@Data
public class BatchConfig {
  /**
   * 最大线程数
   */
  private int threadMaxSize = 30;

  /**
   * 是否允许线程退出
   */
  private boolean allowThreadTimeOut = true;
}
