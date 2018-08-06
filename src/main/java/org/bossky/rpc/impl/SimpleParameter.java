package org.bossky.rpc.impl;

import javax.annotation.Resource;

import org.bossky.rpc.Parameter;

/**
 * 简单的参数
 * 
 * @author daibo
 *
 */
public class SimpleParameter implements Parameter {
	/** 名称 */
	@Resource
	protected String name;
	/** 值 */
	@Resource
	protected Object value;

	protected SimpleParameter() {

	}

	protected SimpleParameter(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name + "=" + value;
	}

}
