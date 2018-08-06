package org.bossky.rpc;

/**
 * 调用器
 * 
 * @author bo
 *
 */
public interface Invoke {
	/**
	 * 调用
	 * 
	 * @param request
	 * @return
	 */
	public Response call(Request request);
}
