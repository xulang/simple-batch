package site.ymkj.batch.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.extern.slf4j.Slf4j;
import site.ymkj.batch.core.entity.BatchContext;
import site.ymkj.batch.core.entity.BatchEntity;
import site.ymkj.batch.core.entity.BatchStatusEnum;
import site.ymkj.batch.core.entity.ProcessArgs;
import site.ymkj.batch.core.entity.StageResult;

/**
 * 批处理管理器
 */
@Slf4j
public class DefaultBatchManage implements IBatchManager {

  private IBatchStore batchStore;

  /**
   * 批处理集合
   */
  private Map<String, IProcessor> processorMap = new HashMap<>();

  private ExecutorService executors;

  public DefaultBatchManage(IBatchStore batchStore) {
    this.batchStore = batchStore;
    executors = new ThreadPoolExecutor(30, 30, 60L, TimeUnit.SECONDS,
        new LinkedBlockingDeque<Runnable>(), new ThreadPoolExecutor.AbortPolicy());
    ((ThreadPoolExecutor) executors).allowCoreThreadTimeOut(true);
  }

  @Override
  public String submitBatch(IProcessor processor) {
    processor.setBatchManage(this);
    // 新增数据库记录
    BatchEntity batchEntity = new BatchEntity();
    batchEntity.setArgs(JSON.toJSONString(processor.getProcessArgs()));
    batchEntity.setContext(JSON.toJSONString(processor.getBatchContext()));
    batchEntity.setStage(JSON.toJSONString(processor.queryProcessResult().getStageResults()));
    batchEntity.setCreateTime(new Date());
    batchEntity.setLastUpdateTime(new Date());
    batchEntity.setName(processor.name());
    batchEntity.setProcessor(processor.getClass().getName());
    batchEntity.setStatus(BatchStatusEnum.NEW);
    batchStore.insertOne(batchEntity);
    // 加入当前的管理
    processorMap.put(processor.name(), processor);
    Future<List<StageResult>> future = executors.submit(processor);
    if (processor instanceof AbstractProcessor) {
      ((AbstractProcessor) processor).setFuture(future);
    }
    return processor.name();
  }

  @Override
  public IProcessor getProcessorByName(String name) {
    IProcessor processor = processorMap.get(name);
    if (processor == null) {
      // 当前查询的任务是历史任务，没有在当前机器，去数据库查询历史任务信息
      log.debug("当前查询的任务:{} 是历史任务，没有在当前机器，去数据库查询历史任务信息", name);
      BatchEntity batchEntity = batchStore.getByName(name);
      if (batchEntity == null) {
        throw new BatchException("无法依据该name:" + name + "找到执行记录");
      }
      processor = conventFromEntityToProcessor(batchEntity);
      processorMap.put(name, processor);
    }
    return processor;
  }

  /**
   * 保存状态
   *
   * @param processor
   * @param batchEntity
   */
  @Override
  public void saveResult(IProcessor processor, BatchEntity batchEntity) {
    batchEntity.setName(processor.name());
    batchStore.saveByName(processor.name(), batchEntity);
  }

  /**
   * 重启时，重新扫描
   *
   * @throws Exception
   */
  public void refresh() throws Exception {
    // 从数据库加载可以执行的批处理
    List<BatchEntity> toRunRecords = batchStore.queryToRun();
    for (BatchEntity toRunRecord : toRunRecords) {
      if (processorMap.get(toRunRecord.getName()) != null) {
        log.warn("该任务已经存在，不需要重新加载");
        continue;
      }
      IProcessor processor = conventFromEntityToProcessor(toRunRecord);
      if (processor != null) {
        // 批处理任务入队
        log.debug("批处理任务入队：{}", processor);
        try {
          executors.submit(processor);
          processorMap.put(processor.name(), processor);
        } catch (Exception e) {
          // 任务提交失败，线程池已满
          log.error("线程池已满，任务提交失败！等待下次扫描");
        }
      } else {
        log.warn("处理器初始化失败！");
      }
    }
  }

  public IProcessor conventFromEntityToProcessor(BatchEntity batchEntity) {
    String processorClazzStr = batchEntity.getProcessor();
    try {
      Class<AbstractProcessor> processorClazz = (Class<AbstractProcessor>) Class.forName(processorClazzStr);
      Constructor<AbstractProcessor>[] constructors = (Constructor<AbstractProcessor>[]) processorClazz
          .getConstructors();
      AbstractProcessor processor = constructors[0].newInstance(
          (ProcessArgs) JSON.parseObject(batchEntity.getArgs(), constructors[0].getParameterTypes()[0]));
      log.debug("开始初始化处理器属性：");
      processor.setBatchContext(JSON.parseObject(batchEntity.getContext(), BatchContext.class));
      processor.setStageResults(JSON.parseObject(batchEntity.getStage(), new TypeReference<List<StageResult>>() {
      }));
      processor.setBatchManage(this);
      processor.setName(batchEntity.getName());
      processor.setBatchStatusEnum(batchEntity.getStatus());
      return processor;
    } catch (ClassNotFoundException e) {
      log.error("找不到该批处理器：{}", processorClazzStr, e);
    } catch (IllegalAccessException e) {
      log.error("初始化该批处理器失败:,{}", processorClazzStr, e);
    } catch (InstantiationException e) {
      log.error("初始化该批处理器失败:,{}", processorClazzStr, e);
    } catch (InvocationTargetException e) {
      log.error("初始化该批处理器失败:,{}", processorClazzStr, e);
    }
    return null;
  }
}
