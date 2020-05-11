package cn.soa.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import cn.soa.dao.EquipmentInfoMapper;
import cn.soa.entity.ProblemInfoForExcel;
import cn.soa.service.inter.ReportSI;
import cn.soa.service.inter.UserManagerSI;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入读取excel工具类
 * @author Jiang,Hang
 * @date 2019年7月20日
 */
@Slf4j
@Component
public class ImportExcelUtil {
	
	@Autowired
	private ReportSI reportS; 
	@Autowired
	private EquipmentInfoMapper equipInfoMapper;
	
	private static final String[] HEADER_NAMES = {"上报人","属地单位","问题区域","所属专业","设备位号",
			"问题类别","不安全行为","具体行为","问题描述"};
	private static final String[] PROBLEM_LOCATION = {"化验","电站","机械","仪表","净化工段","电工","HSE办公室",
			"设备办","生产办","财务经营办","综合办"};

	 /**
     * 
     * @Title: validateExcel 
     * @Description: 验证EXCEL文件 
     * @return boolean 
     */
	public static boolean validateExcel(String fileName) {
		if(fileName == null) {
			log.error("------------文件名{}不是excel格式", fileName);
			return false;
		}
		return fileName.matches("^.+\\.(?i)(xlsx)$");
	}
	
	 /**
     * @Title: isExcelTemplate
     * @Description: 验证EXCEL文件使用的是否是提供的excel模板文件
     * @return boolean 
     */
	public static boolean isExcelTemplate(XSSFSheet sheet) {
		
		XSSFRow header = sheet.getRow(1);
		if(header == null) {
			log.error("--------上传的excel文件不是模板文件");
			return false;
		}
		short maxCol = header.getLastCellNum();
		if(maxCol < 9) {
			log.error("--------上传的excel文件不是模板文件");
			return false;
		}
		for(int i=0;i<HEADER_NAMES.length;i++) {
			XSSFCell cell = header.getCell(i);
			if(!HEADER_NAMES[i].equals(cell.getStringCellValue())) {
				log.error("--------上传的excel文件不是模板文件");
				return false;
			}
		}
		log.info("--------验证EXCEL文件是否为模板文件成功");
		return true;
	}
	
	/**
     * 
     * @Title: validateRow 
     * @Description: 验证某一行是否为空
     * @return boolean 
     */
	public static boolean validateRow(XSSFRow row, int rownum) {

		for(int j=0;j<9;j++) {
			XSSFCell cell = row.getCell(j);
			if(cell == null) {
				throw new RuntimeException("第"+rownum+"行数据不符合要求");
			}
			
			String cellValue = "";
			if (cell.getCellTypeEnum() == CellType.STRING) {
				cellValue = cell.getStringCellValue().trim();
			}
			if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				cellValue = Double.toString(cell.getNumericCellValue());
			}
			
			if(!cellValue.equals("")) {
				return false;
			}
		}
		return true;
	}
	
	/**
     * @Title: readExcelValue
     * @Description: 读取Excel信息
     * @return List<T> 对象集合 
     */
	public List<MultiValueMap<String, String>> readExcelValue(XSSFWorkbook workbook, short sheetIndex, String resavepeople, String deptName){
		List<MultiValueMap<String, String>> list = new ArrayList<>();
		
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		//得到数据最后一行数
		int lastRowNum = sheet.getLastRowNum();
		
		end:
		for(int i=2; i<=lastRowNum; i++) {
		   // 得到Excel的列数
			XSSFRow row = sheet.getRow(i);
			System.err.println("row"+i+"="+row.getLastCellNum());
			if(row == null || row.getLastCellNum() < 9) {
				throw new RuntimeException("第"+(i+1)+"行数据不符合要求");
			}
			 // 循环Excel的列
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			for(int j=0;j<9;j++) {
				XSSFCell cell = row.getCell(j);
				if(cell == null) {
					throw new RuntimeException("第"+(i+1)+"行数据不符合要求");
				}
				
				String cellValue = "";
				//log.info("cellValue="+cellValue);
				if (cell.getCellTypeEnum() == CellType.STRING) {
					cellValue = cell.getStringCellValue().trim();
				}
				if (cell.getCellTypeEnum() == CellType.NUMERIC) {
					cellValue = Double.toString(cell.getNumericCellValue());
				}
				
				if(j != 6 && j != 7 && cellValue.isEmpty()) {	
					if(j == 0 && validateRow(row, i+1)) {
						lastRowNum = i-1;
						break end;
					}
					throw new RuntimeException("第"+(i+1)+"行数据不符合要求");
				}
				
				if(j == 0) {
					String[] userList = null;
					if(cellValue.contains(",")) {
						userList = cellValue.split(",");
					}else{
						userList = cellValue.split("，");
					}
					String result = reportS.verifyApplyPeople(userList);
					if(result != null) {
						throw new RuntimeException("第"+(i+1)+"行："+result);
					}
				}
				
				if(j == 1) {
					if(!Arrays.toString(PROBLEM_LOCATION).contains(cellValue)) {
						throw new RuntimeException("第"+(i+1)+"行属地单位填写不正确");
					}
				}
				
				if(j == 4) {
					if(!cellValue.contains("其他") && !validateRfid(cellValue)) {
						throw new RuntimeException("第"+(i+1)+"行设备位号填写不正确");
					}
				}
				
				if((j == 5 || j == 6) && cellValue.contains("_")) {
					cellValue = cellValue.replace("_", "/");
				}
				
				switch(j) {
				case 0:
					map.add("applypeople", cellValue);
					break;
				case 1:
					map.add("problemtype", cellValue);
					break;
				case 2:
					map.add("welName", cellValue);
					break;
				case 3:
					map.add("profession", cellValue);
					break;
				case 4:
					map.add("rfid", cellValue);
					break;
				case 5:
					map.add("problemclass", cellValue);
					break;
				case 6:
					map.add("remarkfive", cellValue);
					break;
				case 7:
					map.add("remarksix", cellValue);
					break;
				case 8:
					map.add("problemdescribe", cellValue);
				}
				
			}
			map.add("operateName", "上报");
			map.add("applydate", new Date().toString());
			map.add("depet", deptName);
			map.add("resavepeople", resavepeople);
			map.add("problemstate", "UNFINISHED");
			list.add(map);
		}
		if(lastRowNum < 2) {
			throw new RuntimeException("Excel表里没有数据");
		}
		System.out.println(list);
		
		return list;
	}
	
	/**
     * @Title: validateRow
     * @Description: 验证设备位号是否合法
     * @return 验证结果 true or false
     */
	private boolean validateRfid(String rfid) {
		Integer rows = equipInfoMapper.findNumByRfid(rfid);
		if(rows == null || rows == 0) {
			return false;
		}
		return true;
	}
}
