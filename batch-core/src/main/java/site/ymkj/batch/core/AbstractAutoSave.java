package site.ymkj.batch.core;

import site.ymkj.batch.core.entity.BatchEntity;

/**
 * 属性变动，自动保存
 */
public abstract class AbstractAutoSave {

  private transient IBatchManage batchManage;

  protected transient IProcessor processor;

  public void setBatchManage(IBatchManage batchManage) {
    this.batchManage = batchManage;
  }

  public void save() {
    if (batchManage != null) {
      batchManage.saveResult(processor, batchEntity());
    }
  }

  public void setProcessor(IProcessor processor) {
    this.processor = processor;
  }

  public abstract BatchEntity batchEntity();
}
