package site.ymkj.batch.core;

public interface IEnum<V, R extends Enum> {
	/**
	 * 获得枚举存储值
	 * 
	 * @return
	 */
	V value();

	/**
	 * 依据存储之返回枚举
	 * 
	 * @param v
	 * @return
	 */
	R get(V v);

}