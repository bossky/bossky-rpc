package org.bossky.rpc;

import java.util.Collection;
import java.util.Set;

/**
 * 请求
 * 
 * @author bo
 *
 */
public interface Request {
	/**
	 * 调用id
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 设置id
	 * 
	 * @param id
	 */
	public void setId(String id);

	/**
	 * 方法
	 * 
	 * @return
	 */
	public String getMethod();

	/**
	 * 设置方法名
	 * 
	 * @param method
	 */
	public void setMethod(String method);

	/**
	 * 添加参数
	 * 
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, Object value);

	/**
	 * 移除参数
	 * 
	 * @param name
	 * @return
	 */
	public Parameter removeParameter(String name);

	/**
	 * 获取参数
	 * 
	 * @param name
	 * @return
	 */
	public Parameter getParameter(String name);

	/**
	 * 获取参数
	 * 
	 * @param name
	 * @return
	 */
	public int getParameterInt(String name);

	/**
	 * 获取参数
	 * 
	 * @param name
	 * @return
	 */
	public double getParameterDouble(String name);

	/**
	 * 获取参数
	 * 
	 * @param name
	 * @return
	 */
	public String getParameterString(String name);

	/**
	 * 获取参数
	 * 
	 * @param name
	 * @return
	 */
	public <E> E getParameterValue(String name);

	/**
	 * 获取所有参数名
	 * 
	 * @return
	 */
	public Set<String> getParameterNames();

	/**
	 * 获取所有参数
	 * 
	 * @return
	 */
	public Collection<Parameter> getParameters();

}
