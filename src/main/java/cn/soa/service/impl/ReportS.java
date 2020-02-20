package cn.soa.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import cn.soa.dao.ProblemInfoMapper;
import cn.soa.dao.ProblemReportphoMapper;
import cn.soa.dao.ProblemTypeAreaMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemTypeArea;
import cn.soa.entity.ResultJson;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.ReportSI;
import cn.soa.utils.ImportExcelUtil;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ReportS implements ReportSI {
	
	@Autowired
	private ProblemInfoMapper reportMapper;
	@Autowired
	private ProblemInfoSI probelInfoS;
	@Autowired
	private ProblemReportphoMapper phoMapper;
	@Autowired
	private ProblemTypeAreaMapper problemTypeAreaMapper;
	@Autowired
	private ImportExcelUtil importExcelUtil;
	@Autowired
	private RestTemplate restTemplate;
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加/更新一条问题报告数据
	 * @return: String   生成的问题报告主键id   
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public String addOne(ProblemInfo problemInfo, String[] imgList) {
		
		String RepId = problemInfo.getTProblemRepId();
		String piid = problemInfo.getPiid();
		/*
		 * 先判断该问题报告数据是否已存在
		 * 存在就update，不存在则insert
		 */
		ProblemInfo result = null;
		if(piid != null && !"".equals(piid)) {
			result = reportMapper.findByPiid(piid);
		}else{
			result = reportMapper.findByRepId(RepId);
		}
		
		if(result == null) {
			problemInfo.setProcesstype("7");
			
			try {
				//获取最大编号，再自动+1
				Integer maxNum = reportMapper.findMaxProblemNum();
				maxNum = (maxNum == null ? 1 : maxNum+1);
				problemInfo.setProblemnum(maxNum);
				Integer rows = reportMapper.insertOne(problemInfo);
				RepId = problemInfo.getTProblemRepId();
				if(rows != 1) {
					return null;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}	
		}else {
			try {
				Integer rows = reportMapper.updateOne(problemInfo);
				
				if(imgList != null && imgList.length > 0) {
					phoMapper.deleteList(imgList);
				}
				if(rows != 1) {
					return null;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return RepId;
	}
	
	/**   
	 * @Title: getByResavepeopleOrPiid   
	 * @Description: 根据当前登录用户或piid查找问题报告数据
	 * @return: ProblemInfoVO  查到的问题报告数据 
	 */
	@Override
	public ProblemInfoVO getByResavepeopleOrPiid(String resavepeople, String piid) {
		 
		return reportMapper.findByResavepeopleOrPiid(resavepeople, piid);
	}
	
	@Override
	public String verifyApplyPeople(String[] userList) {
		
		String temp = null;
		String msg = null;
		for(int i=0;i<userList.length;i++) {
			String parentID = reportMapper.findApplyPeople(userList[i]);
			if(parentID == null) {
				return "上报人("+userList[i]+")不存在";
			}
			if(temp == null) {
				temp = parentID;
			}else {
				msg = (temp.equals(parentID) ? null:("多个上报人必须属于相同上报部门"));
			}
		}
		
		return msg;
	}
	
	/**   
	 * @Title: ggetProblemInfoByPage   
	 * @Description: 根据条件分页查询出问题上报信息列表
	 * @return: ProblemInfo  查到的问题报告数据列表 
	 */
	@Override
	public List<ProblemInfo> getProblemInfoByPage(ProblemInfo problemInfo, Integer page, Integer limit, String startTime,
			String endTime, String sortField, String sortType,String equiType) {
		
		sortField = findDataBaseFieldName(sortField);
		boolean isOutDate = probelInfoS.modifyProblemState();
		log.info("-----------查询所有问题状态为‘未超期’和未完成的的问题 结果:", isOutDate);
		
		if (equiType != null) {
			return reportMapper.findPorblemInfoByPageEqu(problemInfo, page, limit, startTime, endTime, sortField, sortType);
		}else {
			return reportMapper.findPorblemInfoByPage(problemInfo, page, limit, startTime, endTime, sortField, sortType);
		}
	}
	
	/**   
	 * @Title: ProblemCount   
	 * @Description: 根据条件查询出问题上报信息的条数
	 * @return: Integer   查询数据的条数
	 */
	@Override
	public Map<String, Object> ProblemCount(ProblemInfo problemInfo, String startTime, String endTime,String equiType) {
		
		if (equiType != null) {
			return reportMapper.PorblemCountEqu(problemInfo, startTime, endTime);
		}else {
			return reportMapper.PorblemCount(problemInfo, startTime, endTime);
		}
		
	}
	
	
	/**
	 * 查找问题属地对应区域
	 * @param  无
	 * @return List<ProblemTypeArea> 问题属地对应区域列表
	 */
	@Override
	public List<ProblemTypeArea> findProblemTypeArea() {
		
		return problemTypeAreaMapper.findAll();
	}
	
	/**   
	 * 根据问题上报主键id删除问题上报记录   
	 * @param repid 问题上报主键
	 * @return: int 删除数据的条数
	 */
	@Override
	public Integer deleteByReportid(String repid) {
		
		try {
			Integer row = reportMapper.deleteByRepid(repid);
			return row;
		}catch (Exception e) {
			log.error("删除数据reportID:{}失败"+repid);
			return null;
		}
	}
	
	/**
	 * Map数据结构存储属性字段与数据库字段的对应关系
	 * key - 实体类属性名   value - 数据库字段名
	 * @return
	 */
	private String findDataBaseFieldName(String key) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("applydate", "APPLYDATE");
		map.put("applypeople", "APPLYPEOPLE");
		map.put("welName", "WEL_NAME");
		map.put("problemclass", "PROBLEMCLASS");
		map.put("profession", "PROFESSION");
		map.put("problemtype", "PROBLEMTYPE");
		map.put("problemdescribe", "PROBLEMDESCRIBE");
		map.put("problemstate", "PROBLEMSTATE");
		
		return map.get(key);
	}
	
	/**
	 * 读取excel表批量问题上报
	 */
	@Override
	public String massProblemReport(InputStream is, String filename, String resavepeople, String depet) {
		
		List<Integer> errRecord = new ArrayList<Integer>();
		
		//1. 验证excel表是否合法  importExcelUtil
		boolean result1 = ImportExcelUtil.validateExcel(filename);
		if(!result1) {
			return "上传的文件不是模板excel表";
		}
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheetAt(0);
			boolean result2 = ImportExcelUtil.isExcelTemplate(sheet);
			if(!result2) {
				return "上传的文件不是模板excel表";
			}
			
		} catch (IOException e) {
			log.error("-------读取excel流失败");
			e.printStackTrace();
		}
		List<MultiValueMap<String, String>> list = null;
		try {
			list = importExcelUtil.readExcelValue(workbook, (short)0, resavepeople, depet);
			log.info("批量上报列表大小：{}", list.size());
		}catch (RuntimeException e) {
			e.printStackTrace();
			return e.getMessage();
		}
			
		//解决中文乱码
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		for(int i=0; i<list.size();i++) {
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String,String>>(list.get(i), headers);
			try {
				ResultJson<String> json = restTemplate.postForObject("http://10.89.90.118:10238/iot_process/process", request, ResultJson.class);
				if(json == null || json.getState() == 1) {
					log.error("----------第{}行数据问题上报失败", i+3);
					errRecord.add(i+3);
				}
			}catch(RestClientException e) {
				log.error("----------第{}行数据问题上报失败", i+3);
				errRecord.add(i+3);
				e.printStackTrace();
			}
		}
		
		if(errRecord.size() == 0) {
			log.info("--------问题批量上报成功，总共上报{}个问题", list.size());
			return "问题批量上报成功，总共上报"+list.size()+"个问题";
		}
		if(errRecord.size() == list.size()) {
			return "全部问题上报失败，请检查网络是否正常";
		}
		return "部分问题上报成功，其中第"+errRecord.toString()+"行数据问题上报失败";
	}
}

