package org.bossky.rpc.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.bossky.rpc.Parameter;
import org.bossky.rpc.Request;

/**
 * 简单的响应实现
 * 
 * @author daibo
 *
 */
public class SimpleRequest implements Request {
	/** id */
	@Resource
	protected String id;
	/** 方法 */
	@Resource
	protected String method;
	/** 参数集合 */
	@Resource
	protected Map<String, Parameter> parameterMap;

	public SimpleRequest() {
		parameterMap = new HashMap<String, Parameter>();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public void addParameter(String name, Object value) {
		addParameter(new SimpleParameter(name, value));
	}

	@Override
	public Parameter removeParameter(String name) {
		return parameterMap.remove(name);
	}

	public Parameter addParameter(Parameter param) {
		return parameterMap.put(param.getName(), param);
	}

	@Override
	public Parameter getParameter(String name) {
		return parameterMap.get(name);
	}

	@Override
	public int getParameterInt(String name) {
		Parameter p = getParameter(name);
		if (null == p) {
			return 0;
		}
		Object value = p.getValue();
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		return 0;

	}

	@Override
	public double getParameterDouble(String name) {
		Parameter p = getParameter(name);
		if (null == p) {
			return 0;
		}
		Object value = p.getValue();
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		return 0;

	}

	@Override
	public String getParameterString(String name) {
		Parameter p = getParameter(name);
		if (null == p) {
			return null;
		}
		Object value = p.getValue();
		return null == value ? null : String.valueOf(value);

	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E getParameterValue(String name) {
		Parameter p = getParameter(name);
		if (null == p) {
			return null;
		}
		Object value = p.getValue();
		return (E) value;
	}

	@Override
	public Set<String> getParameterNames() {
		return parameterMap.keySet();
	}

	@Override
	public Collection<Parameter> getParameters() {
		return parameterMap.values();
	}

}
