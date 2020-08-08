package site.ymkj.batch.core.entity;

import site.ymkj.batch.core.IEnum;

/**
 * 阶段执行状态
 */
public enum StageResultStatusEnum implements IEnum<String, StageResultStatusEnum> {
  /**
   * 成功
   */
  SUCCESS,
  /**
   * 失败
   */
  ERROR,
  /**
   * 正在执行
   */
  DOING,
  /**
   * 新建
   */
  CREATED;

  @Override
  public String value() {
    return this.name();
  }

  @Override
  public StageResultStatusEnum get(String v) {
    for (StageResultStatusEnum stageResultStatusEnum : StageResultStatusEnum.values()) {
      if (stageResultStatusEnum.name().equals(v)) {
        return stageResultStatusEnum;
      }
    }
    return null;
  }
}
