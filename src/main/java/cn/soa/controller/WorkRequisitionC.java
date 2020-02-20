package cn.soa.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.soa.entity.AnticorWorkRequisition;
import cn.soa.entity.EquipmentInfo;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoQuery;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemReportpho;
import cn.soa.entity.ResultJson;
import cn.soa.entity.ResultJsonForTable;
import cn.soa.entity.StagingWorkRequisition;
import cn.soa.entity.UnsafeType;
import cn.soa.service.impl.ProblemInfoS;
import cn.soa.service.impl.ReportPhoS;
import cn.soa.service.inter.AnticorWorkRequisitionSI;
import cn.soa.service.inter.EquipmentInfoSI;
import cn.soa.service.inter.ReportSI;
import cn.soa.service.inter.StagingWorkRequisitionSI;
import cn.soa.service.inter.UnsafeSI;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;

/**
 * @ClassName: WorkRequisitionC
 * @Description: 作业申请单-控制层
 * @author Jiang,Hang
 * @date 2019年7月10日
 */
@RestController
@RequestMapping("/workrequisition")
@Slf4j
public class WorkRequisitionC {
	
	@Autowired
	private StagingWorkRequisitionSI stagingWorkRequistionS;
	
	@Autowired
	private AnticorWorkRequisitionSI anticorWorkRequisitionS;

	/**
	 * @Title: saveRequisition   
	 * @Description: 添加或修改脚手架作业申请单
	 * @return: ResultJson<Void> 
	 */
	@PostMapping("/staging/save")
	public ResultJson<Void> saveStagingRequisition(StagingWorkRequisition staging){
		
		log.info("-------------进入WorkRequisitionC---saveStagingRequisition");
		log.info("脚手架作业申请单: {}", staging);
		
		Integer result = stagingWorkRequistionS.addOrUpdate(staging);
		if(result == null) {
			return new ResultJson<Void>(ResultJson.ERROR, "保存失败");
		}
		return new ResultJson<Void>(ResultJson.SUCCESS, "保存成功");
	}
	
	/**
	 * @Title: showRequisition   
	 * @Description: 根据piid查看脚手架作业申请单
	 * @return: ResultJson<Void> 
	 */
	@GetMapping("/staging/show")
	public ResultJson<StagingWorkRequisition> showStagingRequisition(String piid){
		
		log.info("-------------进入WorkRequisitionC---showStagingRequisition");
		log.info("piid: {}", piid);
		
		StagingWorkRequisition result = stagingWorkRequistionS.findByPIID(piid);

		return new ResultJson<StagingWorkRequisition>(ResultJson.SUCCESS, "查询作业单成功",result);
	}
	
	/**
	 * @Title: saveRequisition   
	 * @Description: 添加或防腐保温作业申请单
	 * @return: ResultJson<Void> 
	 */
	@PostMapping("/anticor/save")
	public ResultJson<Void> saveAnticorRequisition(@RequestBody AnticorWorkRequisition anticor){
		log.info("-------------进入WorkRequisitionC---saveAnticorRequisition");
		log.info("防腐保温作业申请单: {}", anticor);
		
		Integer result = anticorWorkRequisitionS.insertOne(anticor);
		if(result == null) {
			return new ResultJson<Void>(ResultJson.ERROR, "保存失败");
		}
		return new ResultJson<Void>(ResultJson.SUCCESS, "保存成功");
	}
	
	/**
	 * @Title: showRequisition   
	 * @Description: 根据piid查看防腐保温作业申请单
	 * @return: ResultJson<Void> 
	 */
	@GetMapping("/anticor/show")
	public ResultJson<AnticorWorkRequisition> showAnticorRequisition(String piid){
		
		log.info("-------------进入WorkRequisitionC---showAnticorRequisition");
		log.info("piid: {}", piid);
		
		AnticorWorkRequisition result = anticorWorkRequisitionS.findByPIID(piid);

		return new ResultJson<AnticorWorkRequisition>(ResultJson.SUCCESS, "查询作业单成功",result);
	}
}
