package site.ymkj.batch.core;

import site.ymkj.batch.core.entity.BatchEntity;

/**
 *
 */
public interface IBatchManager {

  /**
   * 提交批处理，并持久化
   *
   * @param processor
   * @return 返回批处理id
   */
  String submitBatch(IProcessor processor);

  /**
   * 通过批处理名称找到批处理对象
   *
   * @param name
   * @return
   */
  IProcessor getProcessorByName(String name);

  /**
   * 保存状态
   *
   * @param processor
   * @param batchEntity
   */
  void saveResult(IProcessor processor, BatchEntity batchEntity);

  /**
   * 刷新任务
   *
   * @throws Exception
   */
  public void refresh() throws Exception;

}
