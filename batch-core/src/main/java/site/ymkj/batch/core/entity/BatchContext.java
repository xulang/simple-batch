package site.ymkj.batch.core.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import site.ymkj.batch.core.AbstractAutoSave;

/**
 * 批处理共享上下文
 */
public class BatchContext extends AbstractAutoSave {
  private Map<String, Object> data = new HashMap<>();

  public Object get(String key) {
    return data.get(key);
  }

  public void put(String key, Object object) {
    data.put(key, object);
    save();
  }

  public void putAll(Map<String, Object> map) {
    data.putAll(map);
    save();
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  @Override
  public BatchEntity batchEntity() {
    BatchEntity batchEntity = new BatchEntity();
    batchEntity.setContext(JSON.toJSONString(this));
    return batchEntity;
  }
}
