package site.ymkj.batch.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import site.ymkj.batch.core.entity.BatchContext;
import site.ymkj.batch.core.entity.BatchEntity;
import site.ymkj.batch.core.entity.BatchResult;
import site.ymkj.batch.core.entity.BatchStatusEnum;
import site.ymkj.batch.core.entity.ProcessArgs;
import site.ymkj.batch.core.entity.StageResult;
import site.ymkj.batch.core.entity.StageResultStatusEnum;

@Slf4j
public abstract class AbstractProcessor<C extends ProcessArgs> implements IProcessor<C> {

  private List<IStage> stages = new ArrayList<>();

  private Map<String, IStage> stageMap = new HashMap<>();

  private List<StageResult> stageResults;

  /**
   * 运行参数
   */
  private C processArgs;

  /**
   * 跑批上下文
   */
  private BatchContext batchContext;

  /**
   * 泛型的类型
   */
  private Class<C> processArgsClazz;

  private IBatchManager batchManage;

  private String name;

  /**
   * 批处理当前状态
   */
  private BatchStatusEnum batchStatusEnum;

  private Future<List<StageResult>> future;

  public AbstractProcessor(C processArgs) {
    this.processArgsClazz = (Class<C>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    this.processArgs = processArgs;
    this.batchContext = new BatchContext();
    Map<String, Object> initContext = initBatchContext();
    if (initContext != null) {
      this.batchContext.putAll(initContext);
    }
    batchContext.setBatchManage(batchManage);
    batchContext.setProcessor(this);
    initStage();
    //初始化阶段结果集合
    this.stageResults = new ArrayList<>();
    for (int i = 0; i < stages.size(); i++) {
      StageResult stageResult = new StageResult();
      stageResult.setOrder(i);
      stageResult.setName(stages.get(i).name());
      stageResult.setStageResultStatus(StageResultStatusEnum.CREATED);
      stageResult.setProcessor(this);
      stageResult.setBatchManage(batchManage);
      stageResults.add(stageResult);
    }
    // TODO uuid太长，这里用时间代替，有一定的风险
    this.name = type() + "-" + System.currentTimeMillis();
    this.batchStatusEnum = BatchStatusEnum.NEW;
  }

  /**
   * 由子类提供初始化上下文信息
   *
   * @return
   */
  protected abstract Map<String, Object> initBatchContext();

  /**
   * 初始化阶段
   */
  protected abstract void initStage();

  @Override
  public void addStage(IStage stage) {
    stages.add(stage);
    stageMap.put(stage.name(), stage);
  }

  @Override
  public List<StageResult> call() throws Exception {
    BatchEntity batchEntity = new BatchEntity();
    batchEntity.setStatus(BatchStatusEnum.PROCESSING);
    setBatchStatusEnum(BatchStatusEnum.PROCESSING);
    batchManage.saveResult(this, batchEntity);
    try {
      List<StageResult> newStageResults = process(stageResults);
      batchEntity.setStage(JSON.toJSONString(newStageResults));
      batchEntity.setStatus(BatchStatusEnum.DONE);
      setBatchStatusEnum(BatchStatusEnum.DONE);
      batchManage.saveResult(this, batchEntity);
      return newStageResults;
    } catch (Exception e) {
      batchEntity.setStatus(BatchStatusEnum.ERROR);
      batchManage.saveResult(this, batchEntity);
      setBatchStatusEnum(BatchStatusEnum.ERROR);
      log.error("批处理执行失败", e);
    }
    return null;
  }

  @Override
  public List<StageResult> process(List<StageResult> lastStageResults) {
    if (BatchStatusEnum.DONE == batchStatusEnum || BatchStatusEnum.ERROR == batchStatusEnum) {
      throw new BatchException("当前批处理状态不可以重新运行");
    }
    //同步
    for (StageResult stageResult : lastStageResults) {
      if (StageResultStatusEnum.SUCCESS.equals(stageResult.getStageResultStatus())) {
        continue;
      }
      long startTime = System.currentTimeMillis();
      IStage<C> stage = stageMap.get(stageResult.getName());
      try {
        stageResult.setStageResultStatus(StageResultStatusEnum.DOING);
        stageResult.setStartTime(startTime);
        log.info("开始执行阶段:{}", stage);
        Object object = stage.doStage(getProcessArgs(), stageResult, getBatchContext());
        log.info("执行成功:{}", stage);
        stageResult.setStageResultStatus(StageResultStatusEnum.SUCCESS);
        stageResult.setStageReturn(object);
      } catch (Exception e) {
        log.error("执行失败:{}", stage);
        stageResult.setStageResultStatus(StageResultStatusEnum.ERROR);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        stageResult.setExceptionMsg(stringWriter.toString());
        stageResult.setStageResultStatus(StageResultStatusEnum.ERROR);
        log.error("阶段执行失败", e);
        throw e;
      } finally {
        long endTime = System.currentTimeMillis();
        stageResult.setEndTime(endTime);
        stageResult.setUseTime(endTime - startTime);
      }
    }
    return lastStageResults;
  }

  /**
   * 是否全部成功
   *
   * @param stageResults
   * @return
   */
  public boolean isAllSuccess(List<StageResult> stageResults) {
    for (StageResult stageResult : stageResults) {
      if (!StageResultStatusEnum.SUCCESS.equals(stageResult.getStageResultStatus())) {
        return false;
      }
    }
    return true;
  }

  /**
   * 是否有错误发生
   *
   * @param stageResults
   * @return
   */
  public boolean isContainError(List<StageResult> stageResults) {
    for (StageResult stageResult : stageResults) {
      if (StageResultStatusEnum.ERROR.equals(stageResult.getStageResultStatus())) {
        return true;
      }
    }
    return false;
  }

  /**
   * 从子类获得自此执行的上下文
   *
   * @return
   */
  public BatchContext getBatchContext() {
    return batchContext;
  }

  protected void setBatchContext(BatchContext batchContext) {
    this.batchContext = batchContext;
  }

  protected void setProcessArgs(C processArgs) {
    this.processArgs = processArgs;
  }

  public Class<C> getConfigArgsClass() {
    return processArgsClazz;
  }

  @Override
  public BatchResult queryProcessResult() {
    return new BatchResult(batchStatusEnum, stageResults);
  }

  public void setStageResults(List<StageResult> stageResults) {
    this.stageResults = stageResults;
  }

  @Override
  public C getProcessArgs() {
    return processArgs;
  }

  @Override
  public String name() {
    return name;
  }

  public void setBatchManage(IBatchManager batchManage) {
    this.batchManage = batchManage;
    for (StageResult stageResult : stageResults) {
      stageResult.setBatchManage(batchManage);
      stageResult.setProcessor(this);
    }
    batchContext.setBatchManage(batchManage);
    batchContext.setProcessor(this);
  }

  public IBatchManager getBatchManage() {
    return batchManage;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBatchStatusEnum(BatchStatusEnum batchStatusEnum) {
    this.batchStatusEnum = batchStatusEnum;
  }

  public void setFuture(Future<List<StageResult>> future) {
    this.future = future;
  }

  @Override
  public Future<List<StageResult>> getFuture() {
    return future;
  }
}
