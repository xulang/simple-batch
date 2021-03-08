package site.ymkj.batch.core.store;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import site.ymkj.batch.core.IBatchStore;
import site.ymkj.batch.core.entity.BatchEntity;
import site.ymkj.batch.core.entity.BatchStatusEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 内存存储
 */
@Slf4j
public class MemoryStore implements IBatchStore {

  private Map<String, BatchEntity> cache = new ConcurrentHashMap<>();

  @Override
  public void insertOne(BatchEntity batchEntity) {
    cache.put(batchEntity.getName(), batchEntity);
  }

  @Override
  public void saveByName(String name, BatchEntity batchEntity) {
    BatchEntity old = cache.get(name);
    try {
      copyNotNullProperty(batchEntity, old);
    } catch (Exception e) {
      log.error("拷贝属性出错", e);
    }
    cache.put(name, old);
  }

  public static String[] copyNotNullProperty(Object source, Object dest) {
    Field[] fields = source.getClass().getDeclaredFields();
    Set<String> names = new HashSet<>();
    for (Field field : fields) {
      if (Modifier.isFinal(field.getModifiers())) {
        continue;
      }
      field.setAccessible(true);
      try {
        Object value = field.get(source);
        if (value != null) {
          field.set(dest, value);
          names.add(field.getName());
        }
      } catch (IllegalAccessException e) {
        log.error("获取属性值失败", e);
      }
    }
    return names.toArray(new String[names.size()]);
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
