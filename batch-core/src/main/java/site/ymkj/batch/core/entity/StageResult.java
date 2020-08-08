package site.ymkj.batch.core.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import site.ymkj.batch.core.AbstractAutoSave;

/**
 * 每个阶段的返回
 */
public class StageResult extends AbstractAutoSave {

  /**
   * 阶段顺序
   */
  private int order;

  /**
   * 阶段名称
   */
  private String name;

  /**
   * 执行状态
   */
  private StageResultStatusEnum stageResultStatus;

  /**
   * 阶段返回
   */
  private Object stageReturn;

  /**
   * 异常信息
   */
  private String exceptionMsg;

  /**
   * 存储数据区域
   */
  private Map<String, Object> data;

  /**
   * 开始时间
   */
  private long startTime;

  /**
   * 结束时间
   */
  private long endTime;

  /**
   * 耗时
   */
  private long useTime;

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
    save();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    save();
  }

  public StageResultStatusEnum getStageResultStatus() {
    return stageResultStatus;
  }

  public void setStageResultStatus(StageResultStatusEnum stageResultStatus) {
    this.stageResultStatus = stageResultStatus;
    save();
  }

  public Object getStageReturn() {
    return stageReturn;
  }

  public void setStageReturn(Object stageReturn) {
    this.stageReturn = stageReturn;
    save();
  }

  public String getExceptionMsg() {
    return exceptionMsg;
  }

  public void setExceptionMsg(String exceptionMsg) {
    this.exceptionMsg = exceptionMsg;
    save();
  }

  public Map<String, Object> getData() {
    if (data == null) {
      data = new HashMap<>();
    }
    return data;
  }

  public void put(String key, Object value) {
    getData().put(key, value);
    save();
  }

  public Object get(String key) {
    return getData().get(key);
  }

  public void addAll(Map<String, Object> data) {
    getData().putAll(data);
    save();
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  @Override
  public BatchEntity batchEntity() {
    BatchEntity batchEntity = new BatchEntity();
    BatchResult batchResult = processor.queryProcessResult();
    batchResult.getStageResults().set(order, this);
    batchEntity.setStage(JSON.toJSONString(batchResult.getStageResults()));
    return batchEntity;
  }

  @Override
  public String toString() {
    return "StageResult{" +
        "order=" + order +
        ", name='" + name + '\'' +
        ", stageResultStatus=" + stageResultStatus +
        ", stageReturn=" + stageReturn +
        ", exceptionMsg='" + exceptionMsg + '\'' +
        ", data=" + data +
        '}';
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
    save();
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
    save();
  }

  public long getUseTime() {
    return useTime;
  }

  public void setUseTime(long useTime) {
    this.useTime = useTime;
    save();
  }
}
