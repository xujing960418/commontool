package org.hangzhou.tool.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.hangzhou.tool.objectFactory.ToolFactory;

public class ExcelUtils {

	public static void main(String[] args) {
		
	}
	
	
	/**
	 * 根据时间，生成各季度文件名称
	 * @param time 时间
	 * @return
	 */
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
		filename+=".xls";
		return filename;
	}
	
	/**
	 * 获取生成文件路径
	 * @param filename 文件名
	 * @return
	 */
	private static String getFilePath(String filename){
		 File a =new File("");
		 String excel_downloadpath=a.getAbsolutePath()+File.separator+ExcelUtils.excelFileDownloadPath+
					File.separator+filename;
		 return excel_downloadpath;
	}
	
	private static String excelFileDownloadPath="source/excel-download";

	/**
	 * 生成excel文件
	 * @param headname 表头名称
	 * @param fieldname 属性名
	 * @param fieldtype 属性类型
	 * @param data 数据
	 * @param entity 实体类的class
	 * @param time 时间
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static File write(List<String> headname, List<String> fieldname,List<String> fieldtype,List data,Class entity, String time){
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
		
		String filename=ExcelUtils.getFileName(time);
		String filePath=ExcelUtils.getFilePath(filename);
		File file=new File(filePath);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		// 创建写工作簿对象
		HSSFWorkbook workbook= new HSSFWorkbook();
		//添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
        HSSFSheet sheet = workbook.createSheet(filename);
		//设置表头
		setTitle(workbook, sheet, headname);
        //设置单元格并赋值
        setData(sheet, data, fieldGetMethods);
        
        FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			workbook.write(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	/**
	 * 设置表头
	 * @param workbook
	 * @param sheet
	 * @param headname
	 */
    private static void setTitle(HSSFWorkbook workbook, HSSFSheet sheet, List<String> headname) {
        try {
            HSSFRow row = sheet.createRow(0);
            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
            for (int i = 0; i <= headname.size(); i++) {
                sheet.setColumnWidth(i, 15 * 256);
            }
            //设置为居中加粗,格式化时间格式
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            style.setFont(font);
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
            //创建表头名称
            HSSFCell cell;
            for (int j = 0; j < headname.size(); j++) {
                cell = row.createCell(j);
                cell.setCellValue(headname.get(j));
                cell.setCellStyle(style);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 设置单元格并赋值
     * @param sheet
     * @param data
     */
    @SuppressWarnings("rawtypes")
	private static void setData(HSSFSheet sheet, List data, List<Method> fieldGetMethods) {
        try{
            int rowNum = 1;
            for (int i = 0; i < data.size(); i++) {
                HSSFRow row = sheet.createRow(rowNum);

                for (int j = 0; j < fieldGetMethods.size(); j++) {
            		row.createCell(j, Cell.CELL_TYPE_STRING).setCellValue(fieldGetMethods.get(j).invoke(data.get(i)).toString());
                }
                rowNum++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
