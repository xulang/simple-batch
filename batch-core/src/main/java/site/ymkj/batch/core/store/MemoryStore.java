package site.ymkj.batch.core.store;

import site.ymkj.batch.core.IBatchStore;
import site.ymkj.batch.core.entity.BatchEntity;
import site.ymkj.batch.core.entity.BatchStatusEnum;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 内存存储
 */
public class MemoryStore implements IBatchStore {

  private Map<String, BatchEntity> cache = new ConcurrentHashMap<>();

  @Override
  public void insertOne(BatchEntity batchEntity) {
    cache.put(batchEntity.getName(), batchEntity);
  }

  @Override
  public void saveByName(String name, BatchEntity batchEntity) {
    cache.put(batchEntity.getName(), batchEntity);
  }

  @Override
  public BatchEntity getByName(String name) {
    return cache.get(name);
  }

  @Override
  public List<BatchEntity> queryToRun() {
    return cache.entrySet().stream().filter(entity -> entity.getValue().getStatus().equals(BatchStatusEnum.NEW) || entity.getValue().getStatus().equals(BatchStatusEnum.PROCESSING)).map(Map.Entry::getValue).collect(Collectors.toList());
  }
}
