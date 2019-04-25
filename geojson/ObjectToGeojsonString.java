package org.hangzhou.tool.togeojson;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.hangzhou.tool.requestresponse.RequestParameterUtils;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 将实体类转化成geojson字符串
 * @author wang
 *
 */
public class ObjectToGeojsonString {

	
	/**
	 * 将存储实体类的集合，转化成geojson字符串
	 * @param obj 需要转化成实体类的集合
	 * @param entity 实体类的class文件
	 * @param fieleName 属性名字
	 * @param fieldType 属性类型
	 * @param geometry geometry类型
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String objectToGeojsonString(List obj,
						Class entity,
						String fieleName,
						String fieldType,String geometry){
		List<String> fieldname=RequestParameterUtils.getInstance().splitString(fieleName);
//		System.out.println(fieldname);
		List<String> fieldtype=RequestParameterUtils.getInstance().splitString(fieldType);
//		System.out.println(fieldtype);
		if(obj==null){
			return "[]";
		}else if(obj.size()==0){
			return "[]";
		}else{
			String result = null;
			//处理拼接在geojson中的属性名字以及类型
			if(fieldname.size()==fieldtype.size()){
//				System.out.println("属性名字以及类型格式正确！");
			}else{
				System.out.println("属性名字以及类型格式不对！");
				return "";
			}
			try {
				//获取该实体类的class
				Class clazz=entity;
				String geojsonContent="";
				//获取需要加入geojson中的属性的get方法
				List<Method> fieldGetMethods=new ArrayList<Method>();
				StringBuilder content=new StringBuilder();
				for(int i=0;i<fieldname.size();i++){
					content.append(fieldname.get(i));
					content.append(":");
					content.append(fieldtype.get(i));
//					@SuppressWarnings("unused")
//					Class fieldtypeclazz;
//					if(fieldtype.get(i).equals(geometry)){
//						fieldtypeclazz=String.class;
//					}else{
//						if(fieldtype.get(i).equals("String")){
//							fieldtypeclazz=String.class;
//						}
//						if(fieldtype.get(i).equals("Integer")){
//							fieldtypeclazz=Integer.class;
//						}
//						if(fieldtype.get(i).equals("Double")){
//							fieldtypeclazz=Double.class;
//						}
//					}
					content.append(",");
					String name=fieldname.get(i);
					String methodname="get"+name.substring(0, 1).toUpperCase() + name.substring(1);
					Method method=clazz.getMethod(methodname);
//					System.out.println("方法名字："+method.getName());
					fieldGetMethods.add(method);
//					System.out.println(fieldGetMethods);
				}
				geojsonContent=content.toString();
//				System.out.println("反射获取的方法对象："+fieldGetMethods);
//				System.out.println("拼接的对象："+geojsonContent);
				SimpleFeatureType TYPE = DataUtilities.createType("Link",geojsonContent);
				// SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);  
				FeatureJSON fjson = new FeatureJSON(new GeometryJSON(13)); 
				//DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal",TYPE);
				DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
				//featureCollection.add(f)
				for (int i=0 ; i<obj.size() ; i++) {
					SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
					for(Method method:fieldGetMethods){
						featureBuilder.add(method.invoke(obj.get(i)));
					}
//					featureBuilder.add(a1.getThe_location());
//					featureBuilder.add(a1.getId());
//					featureBuilder.add(a1.getAreaname()); 
//					featureBuilder.add(a1.getGenre()); 
//					featureBuilder.add(a1.getAddress());
					SimpleFeature feature = featureBuilder.buildFeature(null);
					featureCollection.add(feature);
				}   
				//SimpleFeature feature = featureBuilder.buildFeature(null);
				StringWriter writer = new StringWriter();
				fjson.writeFeatureCollection(featureCollection, writer);
				result=writer.toString();
			} catch (SchemaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;			
		}
	}
	
	
//	public String listToGeoJsonString(List<AutoDataPoints> objs){
//		String result = null;
//		try {
//			SimpleFeatureType TYPE = DataUtilities.createType("Link",
//					   "geometry:Point,"+
//					   "autopoint_id:Integer,"+
//						"point_name:String"
//
//			);
//			// SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);  
//			FeatureJSON fjson = new FeatureJSON(new GeometryJSON(13)); 
//			
//			
//			//DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal",TYPE);
//			DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
//			
//			//featureCollection.add(f)
//			for (AutoDataPoints a1 : objs) {
//				SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
//				featureBuilder.add(a1.getThe_location());
//				featureBuilder.add(a1.getAutopoint_id());
//				featureBuilder.add(a1.getPoint_name());
//				SimpleFeature feature = featureBuilder.buildFeature(null);
//				featureCollection.add(feature);
//			}   
//			
//			//SimpleFeature feature = featureBuilder.buildFeature(null);
//			StringWriter writer = new StringWriter();
//			fjson.writeFeatureCollection(featureCollection, writer);
//			result=writer.toString();
//		} catch (SchemaException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}
	

}


