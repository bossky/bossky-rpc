package org.bossky.rpc;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 序列化器
 * 
 * @author daibo
 *
 */
public interface Serializer {
	/**
	 * 序列化
	 * 
	 * @param request
	 * @param out
	 * @throws IOException
	 */
	public void serialize(Request request, OutputStream out) throws IOException;

}
