package site.ymkj.batch.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * batch
 *
 * @author
 */
public class BatchEntity implements Serializable {
  /**
   * 任务名称
   */
  private String name;

  /**
   * 任务状态，0新建，1处理中，2处理完成，9处理失败
   */
  private BatchStatusEnum status;

  /**
   * 任务执行参数
   */
  private String args;

  /**
   * 上下文
   */
  private String context;

  /**
   * 阶段结果集
   */
  private String stage;

  /**
   * bean处理器名称
   */
  private String processor;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date lastUpdateTime;

  private static final long serialVersionUID = 1L;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BatchStatusEnum getStatus() {
    return status;
  }

  public void setStatus(BatchStatusEnum status) {
    this.status = status;
  }

  public String getArgs() {
    return args;
  }

  public void setArgs(String args) {
    this.args = args;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getProcessor() {
    return processor;
  }

  public void setProcessor(String processor) {
    this.processor = processor;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append(" [");
    sb.append("Hash = ").append(hashCode());
    sb.append(", name=").append(name);
    sb.append(", status=").append(status);
    sb.append(", args=").append(args);
    sb.append(", context=").append(context);
    sb.append(", stage=").append(stage);
    sb.append(", processor=").append(processor);
    sb.append(", createTime=").append(createTime);
    sb.append(", lastUpdateTime=").append(lastUpdateTime);
    sb.append(", serialVersionUID=").append(serialVersionUID);
    sb.append("]");
    return sb.toString();
  }
}