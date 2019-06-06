package cn.soa.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemReportpho;
import cn.soa.entity.ResultJson;
import cn.soa.entity.UnsafeType;
import cn.soa.service.impl.ReportPhoS;
import cn.soa.service.inter.ReportSI;
import cn.soa.service.inter.UnsafeSI;
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
	 * @Title: showReport   
	 * @Description: 显示当前登录用户暂存的问题报告数据
	 * @return: ResultJson<ProblemInfo> 返回成功响应数据 
	 */
	@GetMapping("/show")
	public ResultJson<ProblemInfoVO> showReport(String resavepeople){
		System.out.println("进入ReportC...showReport...");
		//调用service层执行查询操作
		ProblemInfoVO result = reportS.getByResavepeople(resavepeople);
		
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
			@RequestParam("piid") String piid,
			@RequestParam("tProblemRepId") String tProblemRepId, HttpServletRequest request){
		
		System.out.println("进入ReportC...saveUpload...");
		
		log.info("上传图片名为：{}", file.getOriginalFilename());
		log.info("当前系统登录人为：{}", resavepeople);
		log.info("问题报告piid：{}", piid);
		log.info("上报问题报告id：{}", tProblemRepId);
		
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
		File imagePath = CommonUtil.imageSaved(resavepeople, rootPath, date);
		
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
		String phoAddress =  new StringBuilder(request.getServletPath()+"/image/").append(resavepeople).append("/")
				.append(CommonUtil.dateFormat(date)).append("/").append(phoName).toString();
		
		//将上传图片信息封装实体类中
		ProblemReportpho reportPho = new ProblemReportpho();
		reportPho.setPhoUploadPeople(resavepeople);
		reportPho.setTProblemRepId(tProblemRepId);
		reportPho.setPiid(piid);
		reportPho.setPhoUploadDate(date);
		reportPho.setRemark("1");
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
	
}
