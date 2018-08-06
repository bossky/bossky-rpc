package org.bossky.rpc.impl;

import org.bossky.rpc.Invoke;
import org.bossky.rpc.Request;
import org.bossky.rpc.Response;
import org.bossky.rpc.Transport;

/**
 * 简单的调用器实现,基于http
 * 
 * @author daibo
 *
 */
public class SimpleInvoke implements Invoke {
	/** 数据传送器 */
	protected Transport m_Transport;

	@Override
	public Response call(Request request) {
		// try {
		// return m_Transport.post(request);
		// } catch (IOException e) {
		// throw new InvokeException(e);
		// }
		return null;
	}

}
