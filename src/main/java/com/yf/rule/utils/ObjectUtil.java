package com.yf.rule.utils;

import com.bstek.urule.ClassUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yf.rule.core.Code;
import com.yf.rule.core.ResultObject;
import com.yf.rule.entity.Customer;
import com.yf.rule.entity.Order;

import java.io.*;
import java.util.*;

public class ObjectUtil {

    //region "XML"
    public static String toXml(Class<?> cls, File file) throws IOException{
		ObjectUtil.saveXml(cls, file);
		return ObjectUtil.readXml(file);
	}
	public static void saveXml(Class<?> cls, File file){
		ClassUtils.classToXml(cls, file);
	}
	public static String readXml(File file) throws IOException{
		if(!file.exists()){
			throw new FileNotFoundException(file.getName() + ":未找到");
		}
		if(!file.canRead()){
			throw new IOException(file.getName() + ":不可读");
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while((line = reader.readLine())  != null){
			sb.append(line);
		}
		reader.close();
		return sb.toString();
	}
    //endregion

    //region "Json反序列化"
	public static Object jsonDeserializeObject(String className, String jsonObject, ObjectMapper mapper)throws IOException, ClassNotFoundException{
        if(mapper == null) mapper = new ObjectMapper();
	    Class<?> clazz = Class.forName(className);
        return mapper.readValue(jsonObject, clazz);
	}
	public static Map<String, Object> jsonDeserializeMap(String jsonMap, ObjectMapper mapper) throws IOException{
		if(mapper == null) mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(jsonMap, Map.class);
		return map;
	}
    public static List<Object> jsonDeserialize(String json, ObjectMapper mapper) throws IOException, ClassNotFoundException{
        if(mapper == null) mapper = new ObjectMapper();
        List<Object> list = new ArrayList<>();

        JsonNode root = mapper.readTree(json);
        switch (root.getNodeType()){
            case OBJECT:
            {
                JsonNode type = root.findValue("__type");
                Object object = jsonDeserializeObject(type.textValue(), root.toString(), mapper);
                list.add(object);
                break;
            }
            case ARRAY:
            {
                Iterator<JsonNode> nodes = root.elements();
                while (nodes.hasNext()){
                    JsonNode node = nodes.next();
                    List<Object> objects = jsonDeserialize(node.toString(), mapper);
                    list.add(objects);
                }
                break;
            }
        }

        return list;
    }
    //endregion

    //region "辅助方法"

    public static Object tryGet(String key, Map<String, Object> map, Object defaultValue){
	    return map != null && map.containsKey(key) ? map.get(key) : defaultValue;
    }
	public static boolean isCollectionType(Class<?> clazz){
        return  clazz.isArray()
                || clazz.isAssignableFrom(ArrayList.class)
                || clazz.isAssignableFrom(List.class)
                || clazz.isAssignableFrom(Collection.class)
                || clazz.isAssignableFrom(Iterable.class);
    }
    public static String result(int code, String msg, Object data){
        ObjectMapper mapper = new ObjectMapper();
        String result;
        try{
            result = mapper.writeValueAsString(new ResultObject(code, msg, data));
        }catch (Exception e){
            try{
                result = mapper.writeValueAsString(new ResultObject(code, e.getMessage(), null));
            }catch (JsonProcessingException ex){
                result = "{\"code\":"+ Code.SUCCESS.getValue() + ",\"msg\":\"" + ex.getMessage() + "\",\"data\":null}";
            }
        }
        return result;
    }
    //endregion
}
