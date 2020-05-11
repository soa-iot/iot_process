package cn.soa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.soa.entity.EventTotal;
import cn.soa.entity.FinishedTotal;
import cn.soa.entity.LayuiTree;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemReportpho;
import cn.soa.entity.ResultJson;
import cn.soa.entity.UserOrganization;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.ProblemReportphoSI;
import cn.soa.service.inter.UserManagerSI;

/**
 * @ClassName: ReportC
 * @Description: 问题评估业务控制层
 * @author Bru.Lo
 * @date 2019年5月29日
 */
@RestController
@RequestMapping("/estimates")
public class EstimateC {

	@Autowired
	private ProblemInfoSI problemInfoSI;

	@Autowired
	private ProblemReportphoSI problemReportphoSI;
	
	@Autowired
	private UserManagerSI userManagerS;

	/**
	 * 问题信息图片数据
	 * @param piid
	 * @return
	 */
	@GetMapping("/problemreportpho")
	public ResultJson<List<ProblemReportpho>> getEstimatePho(String piid,String remark) {
		System.err.println(piid+","+remark);
		 List<ProblemReportpho> problemReportphos = problemReportphoSI.getByPiid(piid,remark);

		if (problemReportphos != null) {
			return new ResultJson<List<ProblemReportpho>>(0, "数据获取成功", problemReportphos);
		} else {
			return new ResultJson<List<ProblemReportpho>>(1, "数据获取失败", problemReportphos);
		}
	}

	/**
	 * 根据流程标识字段查询问题评估信息
	 * 
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体的josn数据
	 */
	@GetMapping("/estim")
	public ResultJson<ProblemInfo> getEstimate(String piid) {

		ProblemInfo problemInfo = problemInfoSI.getByPiid(piid);
		
		if (problemInfo != null) {
			return new ResultJson<ProblemInfo>(0, "数据获取成功", problemInfo);
		} else {
			return new ResultJson<ProblemInfo>(1, "数据获取失败", problemInfo);
		}
	}
	
	/**
	 * 根据流程标识字段更新问题问题描述字段
	 * @param piid 流程标识字段
	 */
	@RequestMapping("/problemdescribe")
	public ResultJson<ProblemInfo> changeProblemDescribeByPiid(ProblemInfo problemInfo) {

	//public ResultJson<ProblemInfo> changeProblemDescribeByPiid(@Param("piid")String piid,@Param("problemdescribe")String problemdescribe) {

		//System.err.println(piid+problemdescribe);
		Integer row = problemInfoSI.changeProblemDescribeByPiid(problemInfo);

		if (row != -1) {
			return new ResultJson<ProblemInfo>(0, "数据更新成功");
		} else {
			return new ResultJson<ProblemInfo>(1, "问题描述更新失败");
		}
	}
	
	/**
	 * 修改问题描述
	 * @param problemInfo 问题描述内容
	 * @return 是否修改成功
	 */
	@PostMapping("/modifyestimated")
	public ResultJson<Integer> ModifyEstiByPiid(ProblemInfo problemInfo){
		
		System.err.println("------------------------------------------"+problemInfo.toString());
		Integer row = problemInfoSI.ModifyEstiByPiid(problemInfo);
		return row > 0 ? new ResultJson<Integer>(0, "数据更新成功",row): new ResultJson<Integer>(1, "问题描述更新失败",row);
		
	}
	
	/**
	 * 以属地名称获取其他的属地信息
	 * @param problemtype 属地名称
	 * @return 其他的属地信息集合
	 */
	@GetMapping("/problemtype")
	public ResultJson<List<UserOrganization>> getDeptByProblemtype(String problemtype){
		
		System.err.println(problemtype);
		List<UserOrganization> userOrganizations = problemInfoSI.getDeptByProblemtype(problemtype);
		return userOrganizations != null ? new ResultJson<List<UserOrganization>>(0, "数据获取成功",userOrganizations): new ResultJson<List<UserOrganization>>(1, "数据获取失败",userOrganizations);
		
	}
	
	@GetMapping("/autoFill")
	public ResultJson<List<ProblemInfo>> getByAutoFill(String problemdescribe) {
		
		List<ProblemInfo> data = problemInfoSI.getAutoFill(problemdescribe);
		
		return data != null ? new ResultJson<List<ProblemInfo>>(0,"数据获取成功", data):new ResultJson<List<ProblemInfo>>(1,"数据获取成功", data);
		
	}
	
	/**
	 * 事故事件情况统计
	 * @param date
	 * @return
	 */
	@GetMapping("eventtotal")
	public ResultJson<List<EventTotal>> findEventByApplydate(String date,String startTime,String endTime){
		
		if (startTime != null && !"".equals(startTime)) {
			startTime += " 00:00:00";
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime += " 23:59:59";
		}
		
		
		List<EventTotal> eventTotals = problemInfoSI.findEventByApplydate(date,startTime,endTime);
		
		if (eventTotals != null) {
			return new ResultJson<List<EventTotal>>(0,"查询成功",eventTotals);
		}else {
			return new ResultJson<List<EventTotal>>(1,"查询数据为空",eventTotals);
		}
	}
	
	/**
	 * 问题完成情况统计
	 * @param date
	 * @return
	 */
	@GetMapping("finished")
	public ResultJson<List<FinishedTotal>> findFinishedByApplydate(String date,String startTime,String endTime){
		
		if (startTime != null && !"".equals(startTime)) {
			startTime += " 00:00:00";
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime += " 23:59:59";
		}
		
		
		List<FinishedTotal> finishedTotals = problemInfoSI.findFinishedByApplydate(date,startTime,endTime);
		
		if (finishedTotals != null) {
			return new ResultJson<List<FinishedTotal>>(0,"查询成功",finishedTotals);
		}else {
			return new ResultJson<List<FinishedTotal>>(1,"查询数据为空",finishedTotals);
		}
	}
	
	
}
