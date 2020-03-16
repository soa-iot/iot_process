package cn.soa.controller;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ResultJson;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.ProblemReportphoSI;
import cn.soa.service.inter.UserManagerSI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/estimates"})
public class EstimateC {

   @Autowired
   private ProblemInfoSI problemInfoSI;
   @Autowired
   private ProblemReportphoSI problemReportphoSI;
   @Autowired
   private UserManagerSI userManagerS;


   @GetMapping({"/problemreportpho"})
   public ResultJson getEstimatePho(String piid, String remark) {
      System.err.println(piid + "," + remark);
      List problemReportphos = this.problemReportphoSI.getByPiid(piid, remark);
      return problemReportphos != null?new ResultJson(0, "数据获取成功", problemReportphos):new ResultJson(1, "数据获取失败", problemReportphos);
   }

   @GetMapping({"/estim"})
   public ResultJson getEstimate(String piid) {
      ProblemInfo problemInfo = this.problemInfoSI.getByPiid(piid);
      return problemInfo != null?new ResultJson(0, "数据获取成功", problemInfo):new ResultJson(1, "数据获取失败", problemInfo);
   }

   @RequestMapping({"/problemdescribe"})
   public ResultJson changeProblemDescribeByPiid(ProblemInfo problemInfo) {
      Integer row = this.problemInfoSI.changeProblemDescribeByPiid(problemInfo);
      return row.intValue() != -1?new ResultJson(0, "数据更新成功"):new ResultJson(1, "问题描述更新失败");
   }

   @PostMapping({"/modifyestimated"})
   public ResultJson ModifyEstiByPiid(ProblemInfo problemInfo) {
      System.err.println("------------------------------------------" + problemInfo.toString());
      Integer row = this.problemInfoSI.ModifyEstiByPiid(problemInfo);
      return row.intValue() > 0?new ResultJson(0, "数据更新成功", row):new ResultJson(1, "问题描述更新失败", row);
   }

   @GetMapping({"/problemtype"})
   public ResultJson getDeptByProblemtype(String problemtype) {
      System.err.println(problemtype);
      List userOrganizations = this.problemInfoSI.getDeptByProblemtype(problemtype);
      return userOrganizations != null?new ResultJson(0, "数据获取成功", userOrganizations):new ResultJson(1, "数据获取失败", userOrganizations);
   }


   @GetMapping({"/autoFill"})
   public ResultJson getByAutoFill(String problemdescribe) {
      List data = this.problemInfoSI.getAutoFill(problemdescribe);
      return data != null?new ResultJson(0, "数据获取成功", data):new ResultJson(1, "数据获取成功", data);
   }
}
