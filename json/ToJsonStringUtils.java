package org.hangzhou.tool.tojson;

import java.util.List;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 转化成json字符串的工具
 * @author wang
 */
public class ToJsonStringUtils{

	/**
	 * 将po对象转换成JsonString字符串
	 * @param road 实体类对象
	 * @return 实体类对应的JsonString字符串
	 */
	public String entiyToJsonString(Object obj){
		JSONObject json=this.entiyToJSONObject(obj);
		String result=this.jsonObjectToJsonString(json);
		return result;
		
	}
	
	/**
	 * 将po对象转换成JSONObject对象
	 * @param road 实体类对象
	 * @return po对象对应的JSONObject对象
	 */
    private JSONObject entiyToJSONObject(Object obj){
    	JSONObject json=JSONObject.fromObject(obj);
    	return json;
    }
	
	/**
	 * 将JSONObject转换成JsonString字符串
	 * @param json JSONObject对象
	 * @return po对象对应的Json字符串
	 */
	private String jsonObjectToJsonString(JSONObject json){
		return json.toString();
	}
	
	/**
	 * 将list转换成String字符串
	 * @param roads list对象
	 * @return list对象对应的字符串
	 */
	@SuppressWarnings("rawtypes")
	public String listToJsonString(List roads){
		JSONArray json=this.listToJSONArray(roads);
		String result=this.jsonArrayToJsonString(json);
		return result;
	}
	
	/**
	 * 将datelist转换成String字符串
	 * 日期为java.util.Date类型
	 * @param roads list对象
	 * @param jsonConfig date属性转换的模板
	 * @return list对象对应的字符串
	 */
	@SuppressWarnings("rawtypes")
	public String listUtilDateToJsonString(List objs){
		JsonConfig jsonConfig = new JsonConfig();                                                          
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new UtilDateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray json=this.listToJSONArray(objs, jsonConfig);
		String result=this.jsonArrayToJsonString(json);
		return result;
	}
	
	/**
	 * 将timestamplist转换成String字符串
	 * 日期为java.sql.Timestamp类型
	 * @param roads list对象
	 * @param jsonConfig date属性转换的模板
	 * @return list对象对应的字符串
	 */
	@SuppressWarnings("rawtypes")
	public String listTimeStampToJsonString(List objs){
		JsonConfig jsonConfig = new JsonConfig();                                                          
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new TimestampProcessor ("yyyy-MM-dd HH:mm:ss"));
		JSONArray json=this.listToJSONArray(objs, jsonConfig);
		String result=this.jsonArrayToJsonString(json);
		return result;
	}
	
	/**
	 * 将list转换成JSONArray对象
	 * @param roads list对象
	 * @return list对应的JSONArray对象
	 */
	@SuppressWarnings("rawtypes")
	private JSONArray listToJSONArray(List roads){
		return JSONArray.fromObject(roads);
	}
	
	/**
	 * 将datelist转换成JSONArray对象方法的重载
	 * @param roads list对象
	 * @param jsonConfig date属性转换的模板
	 * @return list对应的JSONArray对象
	 */
	@SuppressWarnings("rawtypes")
	private JSONArray listToJSONArray(List roads,JsonConfig jsonConfig){
		return JSONArray.fromObject(roads,jsonConfig);
	}
	
	/**
	 * 将datemap转换成JSONObject对象方法的重载
	 * @param roads list对象
	 * @param jsonConfig date属性转换的模板
	 * @return list对应的JSONArray对象
	 */
	@SuppressWarnings("rawtypes")
	private JSONArray mapToJSONObject(Map roads,JsonConfig jsonConfig){
		return JSONArray.fromObject(roads, jsonConfig);
	}
	
	/**
	 * 将JSONArray对象转换成String字符串
	 * @param json JSONArray对象
	 * @return JSONArray对应的字符串
	 */
	private String jsonArrayToJsonString(JSONArray json){
		return json.toString();
	}
	
	/**
	 * 将map集合转换成字符串
	 * 里面存储基本数据类型
	 * @param map map集合
	 * @return map集合对应的字符串
	 */
	@SuppressWarnings("rawtypes")
	public String mapToString(Map map){
		JSONObject json=this.mapToJSONObject(map);
		String result=this.jsonObjectToJsonString(json);
		return result;
	}
	
	/**
	 * 将map集合转换成字符串
	 * map集合中含有map可以使用此方法进行转化
	 * @param map map集合
	 * @return map集合对应的字符串
	 */
	@SuppressWarnings("rawtypes")
	public String mapToString(Map map,int number){
		String result=JSON.toJSONString(map,true);
		return result;
	}
	
	/**
	 * 将map集合转换成字符串
	 * map集合中含有map可以使用此方法进行转化
	 * @param map map集合
	 * @return map集合对应的字符串
	 */
	@SuppressWarnings("rawtypes")
	public String mapTimestampToString(Map map){
		JsonConfig jsonConfig = new JsonConfig();                                                          
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new TimestampProcessor ("yyyy-MM-dd HH:mm:ss"));
		JSONArray json=this.mapToJSONObject(map, jsonConfig);
		String result=this.jsonArrayToJsonString(json);
		return result;
	}
	
	/**
	 * 将map集合转换成JSONObject对象
	 * @param map map集合
	 * @return map集合对应的JSONObject对象
	 */
	@SuppressWarnings("rawtypes")
	private JSONObject mapToJSONObject(Map map){
		return JSONObject.fromObject(map);
	}
	
	/**
	 * 将String字符串转换成json字符串
	 * @return
	 */
	public String stringToJsonString(String string){
		return (String) JSON.parse(string);
	}

	
}
