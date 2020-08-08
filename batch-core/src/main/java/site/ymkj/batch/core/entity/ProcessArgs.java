package site.ymkj.batch.core.entity;

/**
 * 任务执行参数
 */
public class ProcessArgs {
  /**
   * 批处理提交时间
   */
  private long startTime = System.currentTimeMillis();

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
}
