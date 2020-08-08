package site.ymkj.batch.core.entity;

import site.ymkj.batch.core.IEnum;

/**
 * 批处理状态
 */
public enum BatchStatusEnum implements IEnum<Integer, BatchStatusEnum> {
  /**
   * 新建
   */
  NEW(0, "新建"),
  /**
   * 处理中
   */
  PROCESSING(1, "处理中"),
  /**
   * 已完成
   */
  DONE(2, "已完成"),
  /**
   * 发生错误
   */
  ERROR(3, "错误终止");

  BatchStatusEnum(int status, String msg) {
    this.status = status;
    this.msg = msg;
  }

  private int status;

  private String msg;

  public int getStatus() {
    return status;
  }

  public String getMsg() {
    return msg;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public Integer value() {
    return status;
  }

  @Override
  public BatchStatusEnum get(Integer v) {
    for (BatchStatusEnum batchStatusEnum : BatchStatusEnum.values()) {
      if (batchStatusEnum.value().equals(v)) {
        return batchStatusEnum;
      }
    }
    return null;
  }
}
