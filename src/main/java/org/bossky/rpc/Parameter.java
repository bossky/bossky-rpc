package org.bossky.rpc;

/**
 * 参数
 * 
 * @author bo
 *
 */
public interface Parameter {
	/**
	 * 参数名
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 参数值
	 * 
	 * @return
	 */
	public Object getValue();
}
