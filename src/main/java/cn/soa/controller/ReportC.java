package cn.soa.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.soa.dao.ReportMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemReportpho;
import cn.soa.entity.ResultJson;
import cn.soa.entity.UnsafeType;
import cn.soa.service.impl.ReportPhoS;
import cn.soa.service.impl.UnsafeS;
import cn.soa.service.inter.ReportSI;
import cn.soa.service.inter.UnsafeSI;
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
	public ResultJson<Void> saveReport(ProblemInfo problemInfo ){
		System.out.println("进入ReportC...saveReport...");
		
		//调用service层执行查询操作
		boolean result = reportS.addOne(problemInfo);
		
		if(result) {
			return new ResultJson<>(ResultJson.SUCCESS, "保存问题报告成功");
		}else {
			return new ResultJson<>(ResultJson.ERROR, "保存问题报告失败");
		}		
	}
	
	/**   
	 * @Title: saveReport   
	 * @Description: 保存问题报告数据
	 * @return: ResultJson<List<UnsafeType>> 返回成功响应数据 
	 */
	@PostMapping("/upload")
	public ResultJson<Void> saveUpload(
			@RequestParam("file") MultipartFile file, 
			@RequestParam("resavepeople") String resavepeople, 
			@RequestParam("tProblemRepId") String tProblemRepId){
		
		System.out.println("进入ReportC...saveUpload...");
		
		System.out.println(file.getOriginalFilename());	
		System.out.println(file.getSize());
		System.out.println("resavepeople = " + resavepeople);
		System.out.println("tProblemRepId = " + tProblemRepId);
		
		if("".equals(resavepeople) || "".equals(tProblemRepId)) {
			return new ResultJson<>(ResultJson.ERROR, "图片上传失败");
		}
		
		//为图片生成一个存储名称
		long millisTime = System.currentTimeMillis();
		//图片显示名称
		String phoDisplayName = file.getOriginalFilename();
		//图片存储名称
		String phoName = phoDisplayName.replace(phoDisplayName.substring(0, phoDisplayName.lastIndexOf('.')), String.valueOf(String.valueOf(millisTime)));
		
		//生成图片存储位置,  测试位置,后期要改
		String phoAddress = "/image/"+phoName;
		
		//将上传图片信息封装实体类中
		ProblemReportpho reportPho = new ProblemReportpho();
		reportPho.setPhoUploadPeople(resavepeople);
		reportPho.setTProblemRepId(tProblemRepId);
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
	
}
