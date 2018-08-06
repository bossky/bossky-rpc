package org.bossky.rpc;

import java.io.IOException;

/**
 * 调用传送器
 * 
 * @author daibo
 * 
 */
public interface Transport {

	/**
	 * 连接超时值
	 * 
	 * @param timeout
	 *            超时值（毫秒）
	 */
	public void setConnectTimeout(int timeout);

	/**
	 * 连接超时值（毫秒）
	 */
	public int getConnectTimeout();

	/**
	 * 读/等待结果超时值
	 * 
	 * @param timeout
	 *            超时值（毫秒）
	 */
	public void setReadTimeout(int timeout);

	/**
	 * 读/等待结果超时值（毫秒）
	 */
	public int getReadTimeout();

	/**
	 * 传送调用请求
	 * 
	 * @param serializer
	 *            序列化器
	 * @param request
	 *            请求
	 * @throws IOException
	 */
	public Response post(Serializer serializer, Request request) throws IOException;
}
