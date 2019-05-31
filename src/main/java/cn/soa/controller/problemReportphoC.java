package cn.soa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.soa.entity.ProblemReportpho;
import cn.soa.entity.ResultJson;
import cn.soa.service.inter.ProblemReportphoSI;

/**
 * @ClassName: ReportC
 * @Description: 问题评估业务控制层
 * @author Bru.Lo
 * @date 2019年5月29日
 */
@RestController
@RequestMapping("/problemreportpho")
public class problemReportphoC {

	@Autowired
	private ProblemReportphoSI problemReportphoSI;

	/**
	 * 问题信息数据
	 * @param piid
	 * @return
	 */
	@GetMapping("/")
	public ResultJson<List<ProblemReportpho>> getEstimate(String piid) {

		 List<ProblemReportpho> problemReportphos = problemReportphoSI.getByPiid(piid);

		if (problemReportphos != null) {
			return new ResultJson<List<ProblemReportpho>>(0, "数据获取成功", problemReportphos);
		} else {
			return new ResultJson<List<ProblemReportpho>>(1, "数据获取失败", problemReportphos);
		}
	}
}
