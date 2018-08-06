package org.bossky.rpc;

import java.io.IOException;
import java.io.InputStream;

/**
 * 反序列化器
 * 
 * @author daibo
 *
 */
public interface Deserializer {
	/**
	 * 反序列化
	 * 
	 * @param request
	 * @param out
	 * @throws IOException
	 */
	public void deserialize(Request request, InputStream in) throws IOException;
}
