package org.hangzhou.tool.dbf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.hangzhou.tool.objectFactory.ToolFactory;

import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFUtils;
import com.linuxense.javadbf.DBFWriter;

public class DbfUtils {

	public static void main(String[] args) {
//		try {
//			DbfUtils.read("e:/testCreateDbf/1554968365220476.dbf");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
//		List<Emp> data=new ArrayList<Emp>();
//		data.add(new Emp("1000","John",new Double(5000.00)));
//		data.add(new Emp("1001","Lalit",new Double(3400.00)));
//		data.add(new Emp("1002","Rohit",new Double(7350.00)));
//		DbfUtils.write(
//				Arrays.asList("emp_code","emp_name","salary"), 
//				Arrays.asList("String","String","Double"), 
//				data, 
//				Emp.class);
		
//		System.out.println(DbfUtils.getFilename());
		
		DbfUtils.copyModelFile("333.dbf");
	}
	
	/**
	 * 根据反射生成dbf文件
	 * 生成dbf文件属性特殊
	 * 暂时未解决属性
	 * @param fieldname
	 * @param fieldtype
	 * @param data
	 * @param entity
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused"})
	private static File write(List<String> fieldname,List<String> fieldtype,List data,Class entity) {
		 
		if(fieldname.size()!=fieldtype.size()){
			System.out.println("输入的属性类型与名称数量不符");
		}
		//存储需要存储至dbf中的属性的get方法
		List<Method> fieldGetMethods=new ArrayList<Method>();
		
		DBFField fields[] = new DBFField[fieldname.size()];
		for(int i=0; i<fieldname.size(); i++){
			DBFField field=new DBFField();
			field.setName(fieldname.get(i));
			switch (fieldtype.get(i)){
				case "Integer":
					field.setType(DBFDataType.NUMERIC);
					field.setLength(10);
					field.setDecimalCount(0);
					break;
				case "Double":
					field.setType(DBFDataType.NUMERIC);
					field.setLength(20);
					field.setDecimalCount(2);
					break;
				case "float":
					field.setType(DBFDataType.FLOATING_POINT);
					field.setLength(20);
					field.setDecimalCount(2);
					break;
				case "long":
					field.setType(DBFDataType.NUMERIC);
					field.setLength(20);
					field.setDecimalCount(0);
					break;
				case "date":
					field.setType(DBFDataType.DATE);
					break;
				case "timestamp":
					field.setType(DBFDataType.TIMESTAMP);
					break;
				case "String": 
					field.setType(DBFDataType.CHARACTER);
					field.setLength(50);
					break;
				case "boolean":
					field.setType(DBFDataType.LOGICAL);
					break;
			}
			fields[i]=field;
			
			//获取每个属性的get方法
			try {
				String name=fieldname.get(i);
				String methodname="get"+name.substring(0, 1).toUpperCase() + name.substring(1);
				Method method=entity.getMethod(methodname);
				fieldGetMethods.add(method);

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		DBFWriter writer = null;
		FileOutputStream fos = null;
		File f = new File(DbfUtils.getFilename());
		try {
			f.createNewFile();
			// 开始写
			fos = new FileOutputStream(f);
			writer = new DBFWriter(fos);
			writer.setFields(fields);
 
			// now populate DBFWriter
			for(int i=0; i<data.size(); i++){
				
				Object rowData[] = new Object[fieldname.size()];
				for(int j=0; j<fieldGetMethods.size(); j++){
					rowData[j]=fieldGetMethods.get(j).invoke(data.get(i));
				}
				writer.addRecord(rowData);
			}
 
			DBFUtils.close(writer);
			System.out.println("The dbf file product success!");

		} catch (FileNotFoundException e) {
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
		} finally {
			DBFUtils.close(writer);
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return f;
	}
	
	
	/**
	 * 随机生成文件的名字
	 * @return
	 */
	private static String getFilename(){
		String str="";
		File a=new File("");
		str+=a.getAbsolutePath();
		str+=dbfFileDownloadPath;
		//取当前时间的长整形值包含毫秒 
		long millis = System.currentTimeMillis(); 
		//
		//加上三位随机数
		Random random = new Random(); 
		int end3 = random.nextInt(999); 
		//如果不足三位前面补0 
		str = millis + String.format("%03d", end3);
		str+=".dbf";
		System.out.println(str);
		
		return str;
					
					
	}
//=======================================================================================================================	
	
	/**
	 * 根据dbf文件模板生成dbf文件
	 * @param fieldname
	 * @param fieldtype
	 * @param data
	 * @param entity
	 * @param time
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static File write(List<String> fieldname,List<String> fieldtype,List data, Class entity, String time) {
		 
		if(fieldname.size()!=fieldtype.size()){
			System.out.println("输入的属性类型与名称数量不符");
		}
		//存储需要存储至dbf中的属性的get方法
		List<Method> fieldGetMethods=new ArrayList<Method>();
		
		for(int i=0; i<fieldname.size(); i++){
			//获取每个属性的get方法
			try {
				String name=fieldname.get(i);
				String methodname="get"+name.substring(0, 1).toUpperCase() + name.substring(1);
				Method method=entity.getMethod(methodname);
				fieldGetMethods.add(method);

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String filename=DbfUtils.getFileName(time);
		DBFWriter writer = null;
		File f = new File(DbfUtils.copyModelFile(filename));
		System.out.println(f.getAbsolutePath());
		try {
			// 开始写
			writer = new DBFWriter(f);
 
			// now populate DBFWriter
			for(int i=0; i<data.size(); i++){
				
				Object rowData[] = new Object[fieldname.size()];
				for(int j=0; j<fieldGetMethods.size(); j++){
					rowData[j]=fieldGetMethods.get(j).invoke(data.get(i));
				}
				writer.addRecord(rowData);
			}
 
			System.out.println("The dbf file product success!");

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return f;
	}

	private static String dbfFileDownloadPath="source/dbf-download";
	private static String dbfFileUploadModelPath="source/dbf-upload-model/hangzhou.dbf";
	
	/**
	 * 复制文件
	 * @param filename
	 * @return
	 */
	private static String copyModelFile(String filename){
		 File a =new File("");
		 String dbf_modelpath="";
		 dbf_modelpath=a.getAbsolutePath()+File.separator+DbfUtils.dbfFileUploadModelPath;
         File dbf_modelfile=new File(dbf_modelpath);
         System.out.println(dbf_modelfile.getAbsolutePath());
         String dbf_downloadpath="";
         dbf_downloadpath=a.getAbsolutePath()+File.separator+DbfUtils.dbfFileDownloadPath+
        		 										File.separator+filename;
         File def_downloadfile=new File(dbf_downloadpath);
         System.out.println(def_downloadfile.getAbsolutePath());
   
         try {
			FileUtils.copyFile(dbf_modelfile, def_downloadfile);
        	 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return dbf_downloadpath;
	}

	@SuppressWarnings("static-access")
	private static String getFileName(String time){
		String filename="";
		LocalDate date=LocalDate.parse(time);
		filename+=date.getYear()+"年";
		Date date1= Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

		int quarter=ToolFactory.getInstance().getDateUtils().getQuarterOfYear(date1);
		switch (quarter){
			case 1:filename+="1季度";break;
			case 2:filename+="2季度";break;
			case 3:filename+="3季度";break;
			case 4:filename+="4季度";break;
		}
		filename+=".dbf";
		return filename;
	}
//=============================================================================================================================
	/**
	 * 读取dbf文件
	 * @param path
	 * @throws FileNotFoundException
	 */
	public static void read(String path) throws FileNotFoundException{
		DBFReader reader = null;
		FileInputStream fis = null;
		File f=new File(path);
		 
		// 开始读
		fis = new FileInputStream(f);
		reader = new DBFReader(fis);

		Object[] objects = null;
		for (; (objects = reader.nextRecord()) != null;) {
			System.out.println(Arrays.toString(objects));
		}
		DBFUtils.close(reader);
		DBFUtils.close(reader);

	}
}
