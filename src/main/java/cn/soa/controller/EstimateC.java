package cn.soa.controller;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemReportpho;
import cn.soa.entity.ResultJson;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.ProblemReportphoSI;

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

	/**
	 * 问题信息图片数据
	 * @param piid
	 * @return
	 */
	@GetMapping("/problemreportpho")
	public ResultJson<List<ProblemReportpho>> getEstimatePho(String piid) {

		 List<ProblemReportpho> problemReportphos = problemReportphoSI.getByPiid(piid);

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
	@PostMapping("/problemdescribe")
	public ResultJson<ProblemInfo> changeProblemDescribeByPiid(@Param("piid")String piid,@Param("problemdescribe")String problemdescribe) {

		System.err.println(piid+problemdescribe);
		Integer row = problemInfoSI.changeProblemDescribeByPiid(piid, problemdescribe);

		if (row != -1) {
			return new ResultJson<ProblemInfo>(0, "数据更新成功");
		} else {
			return new ResultJson<ProblemInfo>(1, "问题描述更新失败");
		}
	}
	
	@PostMapping("/modifyestimated")
	public ResultJson<Integer> ModifyEstiByPiid(ProblemInfo problemInfo){
		
		Integer row = problemInfoSI.ModifyEstiByPiid(problemInfo);
		return row > 0 ? new ResultJson<Integer>(0, "数据更新成功"): new ResultJson<Integer>(1, "问题描述更新失败");
		
	}
	
}
