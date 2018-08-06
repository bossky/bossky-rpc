package org.bossky.rpc;

import java.io.IOException;

/**
 * 调用异常
 * 
 * @author daibo
 *
 */
public class InvokeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvokeException() {
		super();
	}

	public InvokeException(IOException e) {
		super(e);
	}

}
