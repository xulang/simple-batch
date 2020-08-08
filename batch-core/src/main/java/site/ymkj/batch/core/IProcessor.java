package site.ymkj.batch.core;


import java.util.List;
import java.util.concurrent.Callable;

import site.ymkj.batch.core.entity.BatchContext;
import site.ymkj.batch.core.entity.BatchResult;
import site.ymkj.batch.core.entity.ProcessArgs;
import site.ymkj.batch.core.entity.StageResult;

/**
 * 批处理器
 */
public interface IProcessor<C extends ProcessArgs> extends Callable<List<StageResult>> {

  /**
   * 处理器类型
   * @return
   */
  String type();

  /**
   * 处理器名称，唯一
   *
   * @return
   */
  String name();

  /**
   * 增加步点
   */
  void addStage(IStage stage);

  /**
   * 执行方法
   *
   * @param lastStageResults 任务中断前的各阶段执行状态
   * @return
   */
  List<StageResult> process(List<StageResult> lastStageResults);

  /**
   * 查询批处理结果
   * @return
   */
  BatchResult queryProcessResult();

  /**
   * 获得执行上下文
   * @return
   */
  BatchContext getBatchContext();

  /**
   * 获得此次执行的参数
   *
   * @return
   */
  C getProcessArgs();


  void setBatchManage(IBatchManage batchManage);

}
