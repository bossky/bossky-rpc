package org.bossky.rpc.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bossky.mapper.Mapped;
import org.bossky.mapper.Mappeds;
import org.bossky.mapper.Mapper;
import org.bossky.mapper.MapperSet;
import org.bossky.mapper.Meta;
import org.bossky.mapper.MetaType;
import org.bossky.mapper.SimpleMapperSet;
import org.bossky.mapper.json.JsonMapped;
import org.bossky.rpc.Parameter;
import org.bossky.rpc.Request;
import org.bossky.rpc.Serializer;

/**
 * 基于json进行内部通信的的序列化器
 * 
 * @author daibo
 *
 */
public class InnerJsonSerializer implements Serializer {
	/** 映射表集合 */
	private MapperSet mapperSet;
	/** 对象映射器常量 */
	private static final String MAPPER = "_mapper";
	private static final String MAP_KEY = "_mapkey";
	private static final String MAP_VALUE = "_mapvalue";

	public InnerJsonSerializer(MapperSet mapperSet) {
		this.mapperSet = mapperSet;

	}

	protected String serialize(Object value) {
		return toMapped(getMapper(value), value).toString();
	}

	/**
	 * 转换成mapped
	 * 
	 * @param mapper
	 * @param object
	 * @return
	 */
	private Mapped toMapped(Mapper<?> mapper, Object object) {
		JsonMapped mapped = new JsonMapped();
		mapped.put(MAPPER, mapper.getName());
		for (Meta m : mapper.getMetas()) {
			String key = m.getName();
			Object value = m.getValue(object);
			if (null == value) {
				continue;// 空值不放入
			}
			MetaType type = MetaType.valueOf(value.getClass());
			if (type.isPrimitive()) {
				mapped.put(key, toPrimitiveElement(type, value));
			} else if (type.isObject()) {
				mapped.put(key, toObjectElement(type, value, mapped.createChildMapped()));
			} else if (type.isMultiElement()) {
				mapped.put(key, toMultiElement(type, value, mapped.createChildMappeds()));
			} else {
				throw new UnsupportedOperationException();
			}
		}
		return mapped;
	}

	/**
	 * 转换原子元素
	 * 
	 * @param type
	 * @param key
	 * @param value
	 * @param mapped
	 * @param mappeds
	 */
	private Object toPrimitiveElement(MetaType type, Object value) {
		if (type == MetaType.BYTE) {
			return String.valueOf(value);
		} else if (type == MetaType.CHARACTER) {
			return String.valueOf(value);
		} else if (type == MetaType.SHORT) {
			return (Short) value;
		} else if (type == MetaType.INTEGER) {
			return (Integer) value;
		} else if (type == MetaType.LONG) {
			return (Long) value;
		} else if (type == MetaType.FLOAT) {
			return (Float) value;
		} else if (type == MetaType.DOUBLE) {
			return (Double) value;
		} else if (type == MetaType.BOOLEAN) {
			return (Boolean) value;
		} else if (type == MetaType.STRING) {
			return String.valueOf(value);
		} else if (type == MetaType.DATE) {
			Date date = (Date) value;
			return date;
		} else {
			throw new UnsupportedOperationException(type.name() + "类型不支持");
		}
	}

	/**
	 * 转换对象元素
	 * 
	 * @param type
	 * @param key
	 * @param value
	 * @param mapped
	 */
	private Object toObjectElement(MetaType type, Object value, Mapped mapped) {
		Mapper<?> mapper = getMapper(value);
		mapped.put(MAPPER, mapper.getName());
		for (Meta m : mapper.getMetas()) {
			String key = m.getName();
			Object childvalue = m.getValue(value);
			if (null == value) {
				continue;// 空值不放入
			}
			MetaType childtype = m.getType();
			if (childtype.isPrimitive()) {
				mapped.put(key, toPrimitiveElement(childtype, childvalue));
			} else if (childtype.isObject()) {
				mapped.put(key, toObjectElement(childtype, childvalue, mapped.createChildMapped()));
			} else if (childtype.isMultiElement()) {
				mapped.put(key, toMultiElement(childtype, childvalue, mapped.createChildMappeds()));
			} else {
				throw new UnsupportedOperationException();
			}
		}
		return mapped;
	}

	/**
	 * 存入多元对象元素
	 * 
	 * @param type
	 * @param key
	 * @param value
	 * @param mapped
	 */
	@SuppressWarnings("unchecked")
	private Object toMultiElement(MetaType type, Object value, Mappeds child) {
		if (type == MetaType.LIST) {
			List<Object> collections = (List<Object>) value;
			for (Object childValue : collections) {
				if (null == childValue) {
					child.putNull();
					continue;
				}
				MetaType childType = MetaType.valueOf(childValue.getClass());
				if (childType.isPrimitive()) {
					// child.put(toPrimitiveElement(childType, childValue));
					child.put(toObjectElement(childType, childValue, child.createChildMapped()));
				} else if (childType.isObject()) {
					child.put(toObjectElement(childType, childValue, child.createChildMapped()));
				} else if (childType.isMultiElement()) {
					child.put(toMultiElement(childType, childValue, child.createChildMappeds()));
				} else {
					throw new UnsupportedOperationException("暂不支持" + childType);
				}
			}
			return child;
		} else if (type == MetaType.MAP) {
			Map<Object, Object> map = (Map<Object, Object>) value;
			for (Map.Entry<Object, Object> entry : map.entrySet()) {
				Object entrykey = entry.getKey();
				Object entryvalue = entry.getValue();
				Mapped mapped = child.createChildMapped();
				if (null == entrykey) {
					mapped.putNull(MAP_KEY);
				} else {
					MetaType entrykeytype = MetaType.valueOf(entrykey.getClass());
					if (entrykeytype.isPrimitive()) {
						// mapped.put(MAP_KEY, toPrimitiveElement(entrykeytype,
						// entrykey));
						mapped.put(MAP_KEY, toObjectElement(entrykeytype, entrykey, mapped.createChildMapped()));
					} else if (entrykeytype.isObject()) {
						mapped.put(MAP_KEY, toObjectElement(entrykeytype, entrykey, mapped.createChildMapped()));
					} else if (entrykeytype.isMultiElement()) {
						mapped.put(MAP_KEY, toMultiElement(entrykeytype, entrykey, mapped.createChildMappeds()));
					} else {
						throw new UnsupportedOperationException(entrykeytype.name() + "类型不支持");
					}
				}
				if (null == entryvalue) {
					mapped.putNull(MAP_VALUE);
				} else {
					MetaType entryvaluetype = MetaType.valueOf(entryvalue.getClass());
					if (entryvaluetype.isPrimitive()) {
						// mapped.put(MAP_VALUE,
						// toPrimitiveElement(entryvaluetype, entryvalue));
						mapped.put(MAP_VALUE, toObjectElement(entryvaluetype, entryvalue, mapped.createChildMapped()));
					} else if (entryvaluetype.isObject()) {
						mapped.put(MAP_VALUE, toObjectElement(entryvaluetype, entryvalue, mapped.createChildMapped()));
					} else if (type.isMultiElement()) {
						mapped.put(MAP_VALUE, toMultiElement(entryvaluetype, entryvalue, mapped.createChildMappeds()));
					} else {
						throw new UnsupportedOperationException(type.name() + "类型不支持");
					}
				}
				child.put(mapped);
			}
			return child;
		} else {
			throw new UnsupportedOperationException(type.name() + "类型不支持");
		}
	}

	/**
	 * 获取映射表
	 * 
	 * @param value
	 * @return
	 */
	protected Mapper<?> getMapper(Object value) {
		return mapperSet.hitMapper(MetaType.getMapperName(value.getClass()));
	}

	@Override
	public void serialize(Request request, OutputStream out) throws IOException {

	}

	public static void main(String[] args) {
		SimpleMapperSet mapperSet = new SimpleMapperSet();
		mapperSet.register(SimpleRequest.class);
		mapperSet.register(SimpleParameter.class);
		InnerJsonSerializer serializer = new InnerJsonSerializer(mapperSet);
		SimpleRequest request = new SimpleRequest();
		request.setId(System.currentTimeMillis() + "");
		// request.setMethod("helloworld");
		// request.addParameter("age", 18);
		// request.addParameter("name", "bossky");
		// request.addParameter("createTime", new Date());
		request.addParameter("key", Collections.singletonMap("Hello", "World"));
		String value = serializer.serialize(request);
		System.out.println(value);
		InnerJsonDeseralizer deseralizer = new InnerJsonDeseralizer(mapperSet);

		request = deseralizer.deserialize(value);
		System.out.println(request.id);
		System.out.println(request.method);
		for (Parameter p : request.getParameters()) {
			System.out.println(p.getName() + "=" + p.getValue());
		}

	}

}
