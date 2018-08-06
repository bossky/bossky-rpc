package org.bossky.rpc.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bossky.common.util.IoUtil;
import org.bossky.rpc.Request;
import org.bossky.rpc.Response;
import org.bossky.rpc.Serializer;
import org.bossky.rpc.Transport;

/**
 * 基于http的传输器
 * 
 * @author daibo
 *
 */
public class HttpTransport implements Transport {

	protected URL url;

	protected int connectTimeout;

	protected int readTimeout;

	/**
	 * 链接超时时间,单位毫秒
	 */
	@Override
	public void setConnectTimeout(int timeout) {
		this.connectTimeout = timeout;
	}

	/**
	 * 链接超时时间,单位毫秒
	 */
	@Override
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 读取超时时间,单位毫秒
	 */
	@Override
	public void setReadTimeout(int timeout) {
		this.readTimeout = timeout;
	}

	/**
	 * 读取超时时间,单位毫秒
	 */
	@Override
	public int getReadTimeout() {
		return readTimeout;
	}

	@Override
	public Response post(Serializer serializer, Request request) throws IOException {
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setConnectTimeout(connectTimeout);
		http.setReadTimeout(readTimeout);
		http.setDoInput(true);
		http.setDoOutput(true);
		http.setUseCaches(false);
		OutputStream out = http.getOutputStream();
		serializer.serialize(request, out);
		IoUtil.closeIo(out);
		// TODO
		return null;
	}

}
