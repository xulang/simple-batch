package site.ymkj.batch.core;

import java.util.List;

import site.ymkj.batch.core.entity.BatchEntity;

public interface IBatchStore {
	/**
	 * 存储一个批处理任务
	 * 
	 * @param batchEntity
	 */
	void insertOne(BatchEntity batchEntity);

	/**
	 * 通过批处理任务名保存
	 * 
	 * @param name
	 * @param batchEntity
	 */
	void saveByName(String name, BatchEntity batchEntity);

	/**
	 * 依据名称获取批处理任务信息
	 * 
	 * @param name
	 * @return
	 */
	BatchEntity getByName(String name);

	/**
	 * 返回 新建 和运行中的批处理记录
	 * 
	 * @return
	 */
	List<BatchEntity> queryToRun();
}
