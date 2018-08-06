package org.bossky.rpc.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bossky.common.util.Misc;
import org.bossky.mapper.Mapped;
import org.bossky.mapper.Mappeds;
import org.bossky.mapper.Mapper;
import org.bossky.mapper.MapperSet;
import org.bossky.mapper.Meta;
import org.bossky.mapper.MetaType;
import org.bossky.mapper.json.JsonMapped;
import org.bossky.rpc.Deserializer;
import org.bossky.rpc.Request;

/**
 * 基于json进行内部通信的的反序列化器
 * 
 * @author daibo
 *
 */
public class InnerJsonDeseralizer implements Deserializer {
	/** 映射表集合 */
	private MapperSet mapperSet;

	public InnerJsonDeseralizer(MapperSet mapperSet) {
		this.mapperSet = mapperSet;

	}

	@Override
	public void deserialize(Request request, InputStream in) throws IOException {

	}

	@SuppressWarnings("unchecked")
	public <E> E deserialize(String value) {
		JsonMapped mapped = new JsonMapped(value);
		return (E) fromMapped(mapped);
	}

	private static final String MAPPER = "_mapper";
	private static final String MAP_KEY = "_mapkey";
	private static final String MAP_VALUE = "_mapvalue";

	private Object fromMapped(Mapped mapped) {
		if (null == mapped) {
			return null;
		}
		Mapper<?> mapper = getMapper(mapped);
		Object result = mapper.newInstance();
		for (Meta m : mapper.getMetas()) {
			MetaType type = m.getType();
			String key = m.getName();
			if (type.isPrimitive()) {
				result = m.setValue(result, getPrimitiveElement(type, key, mapped));
			} else if (type.isObject()) {
				result = m.setValue(result, getObjectElement(type, key, mapped));
			} else if (type.isMultiElement()) {
				result = m.setValue(result, getMultiElement(type, key, mapped));
			}
		}
		return result;
	}

	/**
	 * 获取原子元素
	 * 
	 * @param type
	 * @param key
	 * @param mapped
	 * @return
	 */
	private Object getPrimitiveElement(MetaType type, String key, Mapped mapped) {
		if (type == MetaType.BYTE) {
			String value = mapped.getString(key);
			return Byte.valueOf(value);
		} else if (type == MetaType.CHARACTER) {
			String value = mapped.getString(key);
			if (value.isEmpty()) {
				return Misc.EMPTYCHAR;
			} else {
				return value.charAt(0);
			}
		} else if (type == MetaType.BOOLEAN) {
			return mapped.getBoolean(key);
		} else if (type == MetaType.SHORT) {
			return mapped.getShort(key);
		} else if (type == MetaType.INTEGER) {
			return mapped.getInteger(key);
		} else if (type == MetaType.LONG) {
			return mapped.getLong(key);
		} else if (type == MetaType.FLOAT) {
			return mapped.getFloat(key);
		} else if (type == MetaType.DOUBLE) {
			return mapped.getDouble(key);
		} else if (type == MetaType.STRING) {
			return mapped.getString(key);
		} else if (type == MetaType.DATE) {
			return mapped.getDate(key);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * 获取对象元素
	 * 
	 * @param type
	 * @param key
	 * @param mapped
	 * @return
	 */
	private Object getObjectElement(MetaType type, String key, Mapped mapped) {
		return fromMapped(mapped.getMapped(key));
	}

	/**
	 * 获取多元对象元素
	 * 
	 * @param type
	 * @param key
	 * @param mapped
	 * @return
	 */
	private Object getMultiElement(MetaType type, String key, Mapped mapped) {
		Mappeds child = mapped.getMappeds(key);
		if (null == child) {
			return null;
		}
		if (type == MetaType.LIST) {
			List<Object> list = new ArrayList<Object>(child.size());
			for (int i = 0; i < child.size(); i++) {
				list.add(fromMapped(child.getMapped(i)));
			}
			return list;
		} else if (type == MetaType.MAP) {
			Map<Object, Object> map = new HashMap<Object, Object>(child.size());
			for (int i = 0; i < child.size(); i++) {
				Mapped entryMapped = child.getMapped(i);
				map.put(fromMapped(entryMapped.getMapped(MAP_KEY)), fromMapped(entryMapped.getMapped(MAP_VALUE)));
			}
			return map;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * 获取映射表
	 * 
	 * @param value
	 * @return
	 */
	protected Mapper<?> getMapper(Mapped mapped) {
		return mapperSet.hitMapper(mapped.getString(MAPPER));
	}
}
