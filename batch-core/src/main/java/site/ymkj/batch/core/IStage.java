package site.ymkj.batch.core;

import site.ymkj.batch.core.entity.BatchContext;
import site.ymkj.batch.core.entity.ProcessArgs;
import site.ymkj.batch.core.entity.StageResult;

/**
 * 批处理阶段
 */
public interface IStage<C extends ProcessArgs>{
  /**
   * 阶段名称
   * @return
   */
  String name();

  /**
   * 执行阶段，实现需要保转事务和可重做
   * @param args 执行参数
   * @param lastStageResult 上次执行结果，可能为空
   * @param batchContext 批处理共享上下文
   * @return
   */
  Object doStage(C args, StageResult lastStageResult, BatchContext batchContext);
}
