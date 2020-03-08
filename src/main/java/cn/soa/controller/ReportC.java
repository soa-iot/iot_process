package cn.soa.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoQuery;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemReportpho;
import cn.soa.entity.ProblemTypeArea;
import cn.soa.entity.ResultJson;
import cn.soa.entity.ResultJsonForTable;
import cn.soa.entity.UnsafeType;
import cn.soa.service.impl.ProblemInfoS;
import cn.soa.service.impl.ReportPhoS;
import cn.soa.service.inter.ReportSI;
import cn.soa.service.inter.UnsafeSI;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ReportC
 * @Description: 问题上报  - 业务控制层
 * @author zhugang
 * @date 2019年5月29日
 */
@RestController
@RequestMapping("/report")
@Slf4j
public class ReportC {
	
	@Autowired
	private UnsafeSI unsafeS;
	@Autowired
	private ReportSI reportS;
	@Autowired
	private ReportPhoS reportPhoS;
	@Autowired
	private UserManagerSI userManagerS;
	@Autowired
	private ProblemInfoS  problemInfoS;
	/**
	 * @Title: statisticalTaskProblempro   
	 * @Description: 问题统计功能 
	 * @return: ResultJson<List<UnsafeType>> 返回不安全行为数据列表   
	 */
	@GetMapping("/problemQuery")
	public ResultJsonForTable queryProblemInfo(@RequestParam(name = "info",required=false) ProblemInfo info,@RequestParam(name = "page",required=false) Integer page,@RequestParam(name = "pageSize",required=false) Integer pageSize,
			@RequestParam(name = "startTime",required=false) String startTime,@RequestParam(name = "endTime",required=false) String endTime
			){

			if(page==null||pageSize==null) {
				page=pageSize=-1;
			};
		return new ResultJsonForTable( 0, "", problemInfoS.count(info,startTime,endTime), problemInfoS.queryProblempro(info, page, pageSize,startTime,endTime));
	}
	
	/**
	 * @Title: statisticalTaskProblempro   
	 * @Description: 问题统计 = 有设备位号的 + 未完成的 
	 * @return: ResultJson<List<UnsafeType>> 返回不安全行为数据列表   
	 */
	@GetMapping("/problem/unfinish/position")
	public ResultJson<List<ProblemInfo>> findUnfinishAndNoPositionC(){
		return new ResultJson<List<ProblemInfo>>( 0, "", problemInfoS.findUnfinishAndNoPositionS());
	}
	
	/**   
	 * @Title: statisticalTaskProblempro   
	 * @Description: 问题统计功能 
	 */
	@GetMapping("/problemCount")
	public ResultJson<List<Map<String ,Object>>> statisticalTaskProblempro(String beginTime,String endTime){
		return new ResultJson<List<Map<String ,Object>>>(ResultJson.SUCCESS, null, problemInfoS.statisticalTaskProblempro(beginTime, endTime));
	};
	
	/**   
	 * @Title: problemTimeOverCountC   
	 * @Description:  查看问题超期数量 
	 * @return: ResultJson<List<Map<String,Object>>>        
	 */  
	@GetMapping("/problemTimeOverCount")
	public ResultJson<List<Map<String ,Object>>> problemTimeOverCountC(String beginTime,String endTime){
		return new ResultJson<List<Map<String ,Object>>>(ResultJson.SUCCESS, null, problemInfoS.findTimeStateS(beginTime, endTime));
	};
	
	/**   
	 * @Title: showUnsafeList   
	 * @Description: 查询出所有不安全行为数据 
	 * @return: ResultJson<List<UnsafeType>> 返回不安全行为数据列表   
	 */
	@GetMapping("/unsafe/showlist")
	public ResultJson<List<UnsafeType>> showUnsafeList(){
		System.out.println("进入ReportC...showUnsafeList...");
		//调用service层执行查询操作
		List<UnsafeType> result = unsafeS.getList();
		
		return new ResultJson<List<UnsafeType>>(ResultJson.SUCCESS, null, result);
	}
	
	/**   
	 * @Title: showProlemTypeArea   
	 * @Description: 查找问题属地对应区域
	 * @return: ResultJson<List<ProblemTypeArea>>  返回问题属地对应区域数据列表   
	 */
	@GetMapping("/problemtype/area")
	public ResultJson<List<ProblemTypeArea>> showProlemTypeArea(){
		System.out.println("进入ReportC...showProlemTypeArea...");
		//调用service层执行查询操作
		List<ProblemTypeArea> result = reportS.findProblemTypeArea();
		
		return new ResultJson<List<ProblemTypeArea>>(ResultJson.SUCCESS, null, result);
	}
	
	/**   
	 * @Title: showProblemInfoByCondition   
	 * @Description: 按条件分页查询问题上报数据
	 * @return: ResultJsonForTable<List<ProblemInfo>> 返回成功响应数据 
	 */
	@PostMapping("/showproblembycondition")
	public ResultJsonForTable<List<ProblemInfo>> showProblemInfoByCondition(ProblemInfoQuery problemInfoQuery, @RequestParam(value="piidArray[]", required=false) String[] piidArray,String equiType) {
	
		
		log.info("------equiType----------------"+ equiType);
		log.info("------查询条件：{}", problemInfoQuery);
		log.info("------piids: {}",Arrays.toString(piidArray));
		Integer page = problemInfoQuery.getPage();
		Integer limit = problemInfoQuery.getLimit();
		String startTime = problemInfoQuery.getStartTime();
		String endTime = problemInfoQuery.getEndTime();
		String sortField = problemInfoQuery.getSortField();
		String sortType = problemInfoQuery.getSortType();
		problemInfoQuery.setPiids(piidArray);
		//根据时间范围推算出具体日期
		problemInfoQuery.setDuedateRange(CommonUtil.timeRangeToDateString(problemInfoQuery.getDuedateRange()));
		
		List<ProblemInfo> result = reportS.getProblemInfoByPage(problemInfoQuery, page, limit, startTime, endTime, sortField, sortType,equiType);
		if(result != null) {
			Map<String, Object> map = reportS.ProblemCount(problemInfoQuery, startTime, endTime,equiType);
			System.err.println(map);
			//查询出的数据量
			Integer count = Integer.valueOf(map.get("TOTAL").toString());
			//查询出的已完成整改数量
			Integer complete = Integer.valueOf(map.get("COMPLETE").toString());
			return new ResultJsonForTable<List<ProblemInfo>>(0, String.valueOf(complete), count, result);
		}
		
		return new ResultJsonForTable<>(0,"查询失败", 0, null);
	}
	
	/**   
	 * @Title: showReport   
	 * @Description: 显示当前登录用户暂存的问题报告数据
	 * @return: ResultJson<ProblemInfo> 返回成功响应数据 
	 */
	@GetMapping("/show")
	public ResultJson<ProblemInfoVO> showReport(String resavepeople){
		System.out.println("进入ReportC...showReport...");
		//调用service层执行查询操作
		ProblemInfoVO result = reportS.getByResavepeopleOrPiid(resavepeople, null);
		
		return new ResultJson<ProblemInfoVO>(ResultJson.SUCCESS, null, result);
	}
	
	/**   
	 * @Title: reloadReport   
	 * @Description: 显示回退后的问题报告数据
	 * @return: ResultJson<ProblemInfo> 返回成功响应数据 
	 */
	@GetMapping("/reload")
	public ResultJson<ProblemInfoVO> reloadReport(String piid){
		System.out.println("进入ReportC...reloadReport...");
		//调用service层执行查询操作
		ProblemInfoVO result = reportS.getByResavepeopleOrPiid(null, piid);
		
		return new ResultJson<ProblemInfoVO>(ResultJson.SUCCESS, null, result);
	}
	
	/**   
	 * @Title: saveReport   
	 * @Description: 保存问题报告数据
	 * @return: ResultJson<List<UnsafeType>> 返回成功响应数据 
	 */
	@PostMapping("/")
	public ResultJson<String> saveReport(ProblemInfo problemInfo, @RequestParam(value="imgList[]", required=false) String[] imgList){
		System.out.println("进入ReportC...saveReport...");
		System.out.println(problemInfo);
		//调用service层执行查询操作
		String repId = reportS.addOne(problemInfo, imgList);
		//boolean result=true;
		if(repId != null) {
			return new ResultJson<String>(ResultJson.SUCCESS, "保存问题报告成功", repId);
		}else {
			return new ResultJson<String>(ResultJson.ERROR, "保存问题报告失败");
		}		
	}
	
	/**   
	 * @Title: saveReport   
	 * @Description: 保存问题图片
	 * @return: ResultJson<List<UnsafeType>> 返回成功响应数据 
	 */

   	@Value("${problem.image.upload.path}")
	private String rootPath;   //获取图片存放根目录
	
	@PostMapping("/upload")
	public ResultJson<Void> saveUpload(
			@RequestParam("file") MultipartFile file, 
			@RequestParam("resavepeople") String resavepeople, 
			@RequestParam("username") String username,
			@RequestParam("piid") String piid,
			@RequestParam("remark") String remark,
			@RequestParam("tProblemRepId") String tProblemRepId, HttpServletRequest request){
		
		System.out.println("进入ReportC...saveUpload...");
		
		log.info("上传图片名为：{}", file.getOriginalFilename());
		log.info("当前系统登录人为：{}", resavepeople);
		log.info("用户拼音名为：{}"+username);
		log.info("问题报告piid：{}", piid);
		log.info("上报问题报告id：{}", tProblemRepId);
		log.info("图片来源remark：{}", remark);
		
		if("".equals(resavepeople) || "".equals(tProblemRepId)) {
			return new ResultJson<>(ResultJson.ERROR, "图片上传失败");
		}
		
		//为图片生成一个存储名称
		long nanoTime = System.nanoTime();
		//图片显示名称
		String phoDisplayName = file.getOriginalFilename();
		//图片存储名称
		String phoName = phoDisplayName.replace(phoDisplayName.substring(0, phoDisplayName.lastIndexOf('.')), String.valueOf(String.valueOf(nanoTime)));
		
		//生成图片存储位置，数据库保存的是虚拟映射路径
		Date date = new Date();
		if(username == null || "".equals(username)) {
			username = "DefaultUser";
		}
		File imagePath = CommonUtil.imageSaved(username, rootPath, date);
		
		try {
			file.transferTo(new File(imagePath, phoName));
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return new ResultJson<>(ResultJson.ERROR, "图片上传失败");
		} catch (IOException e) {
			e.printStackTrace();
			return new ResultJson<>(ResultJson.ERROR, "图片上传失败");
		}
		//request.getContextPath() 或  request.getServletPath()
		String phoAddress =  new StringBuilder(request.getContextPath()+"/image/").append(username).append("/")
				.append(CommonUtil.dateFormat(date)).append("/").append(phoName).toString();
		
		//将上传图片信息封装实体类中
		ProblemReportpho reportPho = new ProblemReportpho();
		reportPho.setPhoUploadPeople(resavepeople);
		reportPho.setTProblemRepId(tProblemRepId);
		reportPho.setPiid(piid);
		reportPho.setPhoUploadDate(date);
		reportPho.setRemark(remark);
		reportPho.setRemarkone("0");
		reportPho.setPhoDispiayName(file.getOriginalFilename());
		reportPho.setPhoName(phoName);
		reportPho.setPhoAddress(phoAddress);
		String phoId = UUID.randomUUID().toString().replace("-", "");
		reportPho.setTProblemPhoId(phoId);
		
		//调用service层进行插入操作
		boolean result = reportPhoS.addOne(reportPho);
		if(result) {
			return new ResultJson<>(ResultJson.SUCCESS, "图片上传成功");
		}else {
			return new ResultJson<>(ResultJson.ERROR, "图片上传失败");
		}
	}
	
	@PostMapping("/updatepho")
	public ResultJson<Void> updatePho(String tProblemRepId, String tempRepId, String piid,
			@RequestParam(value="imgList[]", required=false) String[] imgList){
		System.out.println("进入ReportC...updatePho...");
		
		//调用service层进行插入操作
		boolean result = reportPhoS.updateTempPho(tProblemRepId, tempRepId, piid, imgList);
	
		if(result) {
			return new ResultJson<>(ResultJson.SUCCESS, "暂存图片更新成功");
		}else {
			return new ResultJson<>(ResultJson.ERROR, "暂存图片更新失败");
		}
	}
	
	@PostMapping("/verifyuser")
	public ResultJson<Void> verifyUser(@RequestParam(value="userList[]") String[] userList){
		System.out.println("进入ReportC...verifyUser...");
		log.info("上报人列表为:{}"+Arrays.toString(userList));
		if(userList.length < 1) {
			return new ResultJson<>(ResultJson.ERROR, "上报人填写不符合要求");
		}
		String result = reportS.verifyApplyPeople(userList);
		if(result == null) {
			return new ResultJson<>(ResultJson.SUCCESS, "上报人校验成功");
		}else {
			return new ResultJson<>(ResultJson.ERROR, result);
		}
		
	}
	
	/**
	 * 删除暂存的问题上报数据
	 * @return
	 */
	@PostMapping("/deletereport")
	public ResultJson<Void> deleteReport(@RequestParam String repid){
		
		Integer result = reportS.deleteByReportid(repid);
		if(result == null || result == 0) {
			return new ResultJson<Void>(ResultJson.ERROR, "删除问题上报数据失败");
		}
		return new ResultJson<Void>(ResultJson.SUCCESS, "删除问题上报数据成功");
	}
	
	/**
	 * 下载问题批量上报模板excel表
	 * 
	 */
	@GetMapping("/download/template")
	public ResponseEntity<byte[]> donwloadExcelTemplate(){
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:问题批量上报模板表.xlsx");	
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		try {
			InputStream is = resource.getInputStream();
			//建立字节缓冲区
			byte[] data = new byte[4*1024];
			int length = -1;
			while((length = is.read(data)) != -1) {

				byteArray.write(data,0,length);
			}
			is.close();
			byteArray.close();
			//处理文件名中文乱码
			String fileName = URLEncoder.encode("问题批量上报模板表.xlsx", "UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Content-Disposition", "attchement;filename=" + fileName);
			return new ResponseEntity<byte[]>(byteArray.toByteArray(), headers, HttpStatus.OK);
		}catch (Exception e) {
			log.error("-------下载问题批量上报模板excel表失败------");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 上传excel表
	 * 
	 */
	@PostMapping("/upload/template")
	public ResultJson<Void> uploadExcelTemplate(@RequestParam("file") MultipartFile file, String resavepeople, String depet){
		log.info("------上传的excel表名为："+file.getOriginalFilename());
		log.info("------resavepeople："+resavepeople);
		log.info("------depet："+depet);
		
		try {
			String msg = reportS.massProblemReport(file.getInputStream(), file.getOriginalFilename(), resavepeople, depet);
			return new ResultJson<>(ResultJson.SUCCESS, msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResultJson<>(ResultJson.ERROR, "上传Excel表失败");
	}
	
	@PostMapping("/save/repairinfo")
	public ResultJson<Void> updateRepairInfo(
			ProblemInfo info, String equipName, String positionNum, String serialNum){
		System.out.println("进入ReportC...updateRepairInfo...");
		log.info("------插入检维修信息ProblemInfo：{}", info);
		log.info("------设备名称equipName：{}", equipName);
		log.info("------设备位号positionNum：{}", positionNum);
		log.info("------设备型号serialNum：{}", serialNum);
		
		info.setSupervisorydate(new Date());
		Integer rows = problemInfoS.updateProblemDescribeByPiid(info, equipName, positionNum, serialNum);
		if(rows == 0 || rows == -1) {
			log.info("------插入检维修信息失败------");
			return new ResultJson<Void>(ResultJson.ERROR, "插入检维修信息失败");
		}
		log.info("------插入检维修信息成功------");
		return new ResultJson<Void>(ResultJson.SUCCESS, "插入检维修信息成功");
	}
	
	/**
	 * 上报问题删除
	 * @param tProblemRepId - 上报问题ID
	 * @return
	 */
	@RequestMapping("/problem/delete")
	public ResultJson<Boolean> deleteProblemInfo(String tProblemRepId, String deleteComment, String piid, String resavepeople, boolean isFinished){
		System.out.println("进入ReportC...deleteProblemInfo...");
		log.info("------问题上报主键 tProblemRepId：{}", tProblemRepId);
		log.info("------流程ID piid：{}", piid);
		log.info("------删除原因 deleteComment：{}", deleteComment);
		log.info("------当前登录人 resavepeople：{}", resavepeople);
		log.info("-----是否已整改 isFinished: {}", isFinished);
		
		try {
			Boolean result = reportS.deleteProblemInfo(tProblemRepId, deleteComment, piid, resavepeople, isFinished);
			if(result) {
				return new ResultJson<>(ResultJson.SUCCESS, "删除问题上报记录成功", result);
			}
			return new ResultJson<>(ResultJson.ERROR, "删除问题上报记录失败", result);
		}catch (RuntimeException e) {
			return new ResultJson<>(ResultJson.ERROR, "删除问题上报记录失败", false);
		}
	}
	
	/**
	 * 查询问题完成情况统计
	 * @param startDate - 开始日期
	 * @param endDate - 截止日期
	 * @return
	 */
	@RequestMapping("/finish/record")
	public ResultJson<List<Map<String, String>>> getReportFinishRecords(String startDate, String endDate){
		System.out.println("进入ReportC...getReportFinishRecords...");
		log.info("------查询时间 startDate：{}, endDate: {}", startDate, endDate);
		List<Map<String, String>> result = reportS.getReportFinishRecords(startDate, endDate);
		if(result != null) {
			return new ResultJson<>(ResultJson.SUCCESS, "查询问题完成情况统计成功", result);
		}
		return new ResultJson<>(ResultJson.ERROR, "查询问题完成情况统计失败", result);
	}
}
