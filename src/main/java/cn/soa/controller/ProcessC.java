package cn.soa.controller;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ResultJson;
import cn.soa.entity.ResultJsonForTable;
import cn.soa.service.inter.AcitivityHistorySI;
import cn.soa.service.inter.AcitivityIdentitySI;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.BussinessSI;
import cn.soa.service.inter.ConfigSI;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.ProcessVariableSI;
import cn.soa.service.inter.activiti.ProcessStartHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/process"})
public class ProcessC {

   private static Logger logger = LoggerFactory.getLogger(ProcessC.class);
   @Autowired
   private ActivitySI activityS;
   @Autowired
   private ConfigSI configS;
   @Autowired
   private BussinessSI bussinessS;
   @Autowired
   private ProcessVariableSI processVariableS;
   @Autowired
   private AcitivityHistorySI acitivityHistoryS;
   @Autowired
   private ProcessStartHandler processStartHandler;
   @Autowired
   private ProblemInfoSI problemInfoS;
   @Autowired
   private AcitivityIdentitySI acitivityIdentityS;


   @PostMapping({"/deployment"})
   public ResultJson deployProcessC1(@RequestParam("name") String name, @RequestParam("xmlUrl") @NotBlank String xmlUrl, @RequestParam("pngUrl") @NotBlank String pngUrl) {
      logger.info("--C----------部署流程---------------");
      logger.info(name);
      logger.info(xmlUrl);
      logger.info(pngUrl);
      Deployment deployment;
      if(name != null) {
         deployment = this.activityS.deployProcess(name, xmlUrl, pngUrl);
         if(deployment != null) {
            logger.info(deployment.toString());
            return new ResultJson(0, "部署成功", Boolean.valueOf(true));
         } else {
            return new ResultJson(1, "部署失败", Boolean.valueOf(false));
         }
      } else {
         deployment = this.activityS.deployProcessNoName(xmlUrl, pngUrl);
         return deployment != null?new ResultJson(0, "部署成功", Boolean.valueOf(true)):new ResultJson(1, "部署失败", Boolean.valueOf(false));
      }
   }

   @GetMapping({"/deployment"})
   public ResultJson deployProcessC2(@RequestParam("name") String name, @RequestParam("xmlUrl") @NotBlank String xmlUrl, @RequestParam("pngUrl") @NotBlank String pngUrl) {
      logger.info("--C----------部署流程---------------");
      logger.info(name);
      logger.info(xmlUrl);
      logger.info(pngUrl);
      Deployment deployment;
      if(name != null) {
         deployment = this.activityS.deployProcess(name, xmlUrl, pngUrl);
         return deployment != null?new ResultJson(0, "部署成功", Boolean.valueOf(true)):new ResultJson(1, "部署失败", Boolean.valueOf(false));
      } else {
         deployment = this.activityS.deployProcessNoName(xmlUrl, pngUrl);
         return deployment != null?new ResultJson(0, "部署成功", Boolean.valueOf(true)):new ResultJson(1, "部署失败", Boolean.valueOf(false));
      }
   }

   @GetMapping({"/configfiles"})
   public ResultJson getConfigFileBPMN() {
      logger.info("--C----------获取activity流程配置文件格式为bpmn的全部文件（已指定目录/process）---------------");
      List files = this.activityS.getconfigFileBPMN();
      return files != null?new ResultJson(0, "获取文件成功", files):new ResultJson(1, "获取文件失败", files);
   }

   @PostMapping({"/{dfid}"})
   public ResultJson startProcessByDfid(@PathVariable("dfid") @NotBlank String dfid, ProblemInfo problemInfo) {
      logger.info("--C--------启动流程（同时业务处理）  -------------");
      logger.info(dfid);
      logger.info(problemInfo.toString());
      String bsid = this.bussinessS.dealProblemReport(problemInfo);
      if(bsid == null) {
         return new ResultJson(1, "业务处理失败，流程未启动", "业务处理失败，流程未启动");
      } else {
         logger.info("--C--------bsid  -------------" + bsid);

         try {
            Map e = this.configS.setVarsAtStart();
            logger.info("--C--------basicVars  -------------" + e);
            Map e2 = this.processVariableS.addVarsStartProcess(problemInfo);
            logger.info("--C--------tempVars  -------------" + e2);
            HashMap vars = new HashMap();
            vars.putAll(e);
            vars.putAll(e2);
            logger.info("--C--------vars  -------------" + vars);
            boolean beforeHandler = this.processStartHandler.before(bsid, problemInfo);
            if(beforeHandler) {
               logger.info("--C--------流程启动前的流程其他业务处理  -------------" + beforeHandler);
            }

            String piid = this.activityS.startProcessByDfid(dfid, bsid, vars);
            logger.info("--C--------piid  -------------" + piid);
            this.processStartHandler.after(bsid, piid, problemInfo);
            if(beforeHandler) {
               logger.info("--C--------流程启动后的流程其他业务处理  -------------" + beforeHandler);
            }

            return new ResultJson(0, "流程启动成功", piid + "," + bsid);
         } catch (Exception var11) {
            var11.printStackTrace();

            try {
               this.problemInfoS.deleteByBsid(bsid);
               logger.info("--C--------流程启动失败后，删除新增的业务记录成功  -------------" + bsid);
            } catch (Exception var10) {
               var11.printStackTrace();
               logger.info("--C--------流程启动失败后，删除新增的业务记录失败  -------------" + bsid);
            }

            return new ResultJson(1, "流程启动失败", (Object)null);
         }
      }
   }
   
   /**   
	 * @Title: startProcess   
	 * @Description: 启动流程（同时业务处理）  
	 * @return: ResultJson<String>        
	 */
   @PostMapping({""})
   public ResultJson startProcess(ProblemInfo problemInfo) {
      logger.info("--C--------启动流程（同时业务处理）  -------------");
      logger.info(problemInfo.toString());
      String bsid = this.bussinessS.dealProblemReport(problemInfo);
      if(bsid == null) {
         return new ResultJson(1, "业务处理失败，流程未启动", "业务处理失败，流程未启动");
      } else {
         logger.info("--C--------bsid  -------------" + bsid);

         try {
            Map e = this.configS.setVarsAtStart();
            logger.info("--C--------basicVars  -------------" + e);
            Map e2 = this.processVariableS.addVarsStartProcess(problemInfo);
            logger.info("--C--------tempVars  -------------" + e2);
            HashMap vars = new HashMap();
            vars.putAll(e);
            vars.putAll(e2);
            logger.info("--C--------vars  -------------" + vars);
            boolean beforeHandler = this.processStartHandler.before(bsid, problemInfo);
            if(beforeHandler) {
               logger.info("--C--------流程启动前的流程其他业务处理  -------------" + beforeHandler);
            }

            String piid = this.activityS.startProcess(bsid, vars);
            logger.info("--C--------piid  -------------" + piid);
            this.processStartHandler.after(bsid, piid, problemInfo);
            if(beforeHandler) {
               logger.info("--C--------流程启动后的流程其他业务处理  -------------" + beforeHandler);
            }

            return new ResultJson(0, "流程启动成功", piid + "," + bsid);
         } catch (Exception var10) {
            var10.printStackTrace();

            try {
               this.problemInfoS.deleteByBsid(bsid);
               logger.info("--C--------流程启动失败后，删除新增的业务记录成功  -------------" + bsid);
            } catch (Exception var9) {
               var10.printStackTrace();
               logger.info("--C--------流程启动失败后，删除新增的业务记录失败  -------------" + bsid);
            }

            return new ResultJson(1, "流程启动失败", (Object)null);
         }
      }
   }

   @PutMapping({"/nodes/next/group/piid/{piid}"})
   public ResultJson nextGroupNodeByPIID(@PathVariable("piid") String piid, @RequestParam Map map) {
      logger.info("--C-------- 执行流程的下一步     -------------");
      logger.info(piid);
      logger.info(map.toString());
      boolean b = this.activityS.nextNodeByPIID(piid, map);
      return b?new ResultJson(0, "流程流转下一个节点任务成功", Boolean.valueOf(true)):new ResultJson(1, "流程流转下一个节点任务失败", Boolean.valueOf(false));
   }

   @PutMapping({"/nodes/next/piid/{piid}"})
   public ResultJson nextNodeByPIID(@PathVariable("piid") String piid, @RequestParam Map map) {
      logger.info("--C-------- 执行流程的下一步     -------------");
      logger.info(piid);
      logger.info(map.toString());
      boolean b = this.activityS.nextNodeByPIID1(piid, map);
      return b?new ResultJson(0, "流程流转下一个节点任务成功", Boolean.valueOf(true)):new ResultJson(1, "流程流转下一个节点任务失败", Boolean.valueOf(false));
   }

   @PutMapping({"/nodes/next/tsid/{tsid}"})
   public ResultJson nextNodeByTSID(@PathVariable("tsid") String tsid, 
      @RequestParam(
         value = "var",
         required = false
      ) String var, 
      @RequestParam(
         value = "varValue",
         required = false
      ) String varValue, @RequestParam("comment") String comment, @RequestParam("nodeid") String nodeid) {
      logger.info("--C-------- 执行流程的下一步     -------------");
      logger.info(tsid);
      logger.info(var);
      logger.info(comment);
      logger.info(varValue);
      Boolean b = Boolean.valueOf(this.activityS.nextNodeByTSID(tsid, var, varValue, comment));
      return b.booleanValue()?new ResultJson(0, "流程流转下一个节点任务成功", Boolean.valueOf(true)):new ResultJson(1, "流程流转下一个节点任务失败", Boolean.valueOf(false));
   }

   @PutMapping({"/nodes/end/group/piid/{piid}"})
   public ResultJson endProcessIngroup(@PathVariable("piid") @NotBlank String piid, @RequestParam("comment") String comment, @RequestParam("userName") String userName, @RequestParam("operateName") String operateName) {
      logger.info("--C-------- 终止流程     -------------");
      logger.info(piid);
      logger.info(comment);
      logger.info(userName);
      logger.info(operateName);
      String s = this.activityS.endProcessByPiidInComment(piid, comment, userName, operateName);
      return StringUtils.isBlank(s)?new ResultJson(1, "闭环流程失败", "闭环流程失败"):new ResultJson(0, "闭环流程成功", "闭环流程成功");
   }

   @PutMapping({"/nodes/end/piid/{piid}"})
   public ResultJson endProcess(@PathVariable("piid") @NotBlank String piid, @RequestParam("comment") String comment, @RequestParam("userName") String userName, @RequestParam("operateName") String operateName) {
      logger.info("--C-------- 终止流程     -------------");
      logger.info(piid);
      logger.info(comment);
      logger.info(userName);
      logger.info(operateName);
      String s = this.activityS.endProcessByPiidInComment(piid, comment, userName, operateName);
      return StringUtils.isBlank(s)?new ResultJson(1, "闭环流程失败", "闭环流程失败"):new ResultJson(0, "闭环流程成功", "闭环流程成功");
   }

   @GetMapping({"/nodes/historyAct/tsid/{tsid}"})
   public ResultJson getHisActInfosByTsid(@PathVariable("tsid") @NotBlank String tsid) {
      logger.info("--C-------- 根据任务tsid，获取流程所有的历史节点      -------------");
      logger.info(tsid);
      List historyNodesInfo = this.activityS.getHisInfosByTsid(tsid);
      return historyNodesInfo != null && historyNodesInfo.size() > 0?new ResultJson(0, "获取流程所有的历史节点成功", historyNodesInfo):new ResultJson(0, "获取流程所有的历史节点失败", (Object)null);
   }

   @GetMapping({"/nodes/historyAct/piid/{piid}"})
   public ResultJson getHisActInfosByPiid(@PathVariable("piid") @NotBlank String piid) {
      logger.info("--C-------- 根据任务piid，获取流程所有的历史节点      -------------");
      logger.info(piid);
      List historyNodesInfo = this.activityS.getHisActNodesByPiid(piid);
      return historyNodesInfo != null && historyNodesInfo.size() > 0?new ResultJson(0, "获取流程所有的历史节点成功", historyNodesInfo):new ResultJson(0, "获取流程所有的历史节点失败", (Object)null);
   }

   @GetMapping({"/nodes/historyTask/piid/{piid}"})
   public ResultJson getHisTaskNodeInfosByPiid(@PathVariable("piid") @NotBlank String piid) {
      logger.info("--C-------- 根据流程piid，获取当前流程的任务节点信息      -------------");
      logger.info(piid);
      List historyNodesInfo = this.activityS.getHisTaskNodeInfosByPiid(piid);
      return historyNodesInfo != null && historyNodesInfo.size() > 0?new ResultJson(0, "获取流程实例已完成的任务节点成功", historyNodesInfo):new ResultJson(0, "获取流程实例已完成的任务节点失败", (Object)null);
   }

   @GetMapping({"/tasks/history/all/piid/{piid}"})
   public ResultJson getAllHisTaskNodeInfosByPiid(@PathVariable("piid") @NotBlank String piid) {
      logger.info("--C-------- 根据流程piid，获取当前流程的任务节点信息      -------------");
      logger.info(piid);
      List historyNodesInfo = this.acitivityHistoryS.getHisTaskNodesInfoByPiid(piid);
      return historyNodesInfo != null && historyNodesInfo.size() > 0?new ResultJson(0, "获取流程实例已完成的任务节点成功", historyNodesInfo):new ResultJson(0, "获取流程实例已完成的任务节点失败", (Object)null);
   }

   @PutMapping({"/nodes/before/piid/{piid}"})
   public ResultJson backToBeforeNodesByPiid(@PathVariable("piid") @NotBlank String piid, @RequestParam("comment") String comment) {
      logger.info("--C-------- 根据任务piid，流程返回到上一个节点     -------------");
      logger.info(piid);
      logger.info(comment);
      boolean b = this.activityS.backToBeforeNodeByPiid(piid, comment);
      return b?new ResultJson(0, "流程返回到上一个节点成功", Boolean.valueOf(true)):new ResultJson(0, "流程返回到上一个节点失败", (Object)null);
   }

   @PutMapping({"/nodes/before/group/piid/{piid}"})
   public ResultJson backToBeforeNodesByPiidInGroup(@PathVariable("piid") @NotBlank String piid, @RequestParam Map map) {
      logger.info("--C-------- 根据任务piid，流程返回到上一个节点     -------------");
      logger.info(piid);
      if(map != null && map.size() > 0) {
         logger.info(map.toString());
      }

      boolean b = this.activityS.backToBeforeNodeByPiidInGroup(piid, map);
      return b?new ResultJson(0, "流程返回到上一个节点成功", Boolean.valueOf(true)):new ResultJson(0, "流程返回到上一个节点失败", (Object)null);
   }

   @GetMapping({"/tasks"})
   public ResultJson getAllTasksByUsernameC(@RequestParam("userName") @NotBlank String userName) {
      logger.info("--C-------- 根据用户姓名，查询用户的所有待办任务（个人任务+组任务）     -------------");
      logger.info(userName);
      List tasks = this.activityS.getAllTasksByUsername(userName);
      return tasks != null?new ResultJson(0, "代办任务查询成功", tasks):new ResultJson(0, "代办任务查询失败", tasks);
   }

   @GetMapping({"/tasks/layui"})
   public ResultJsonForTable getAllTasksByUsername1C(@RequestParam("userName") @NotBlank String userName) {
      logger.info("--C-------- 根据用户姓名，查询用户的所有待办任务（个人任务+组任务）     -------------");
      logger.info(userName);
      List tasks = this.activityS.getAllTasksByUsername(userName);
      return tasks != null?new ResultJsonForTable(0, "代办任务查询成功", tasks.size(), tasks):new ResultJsonForTable(0, "代办任务查询失败", 0, tasks);
   }

   @PutMapping({"/nodes/jump/group/piid/{piid}"})
   public ResultJson jumpNodesByPiidInGroup(@PathVariable("piid") String piid, @RequestParam Map map) {
      logger.info("--C-------- 流程节点跳转 - 组任务      -------------");
      logger.info(piid);
      if(map != null && map.size() > 0) {
         logger.info(map.toString());
      } else {
         logger.info("-------变量map为null--------");
      }

      boolean b = this.activityS.transferProcessByPiid(piid, map);
      return b?new ResultJson(0, "节点跳转成功", Boolean.valueOf(true)):new ResultJson(0, "节点跳转失败", Boolean.valueOf(false));
   }

   @GetMapping({"/userId/piid"})
   public ResultJson getPiidsByUserIdC(@RequestParam("userId") @NotBlank String userId) {
      logger.info("--C-------- 查找与指定人相关的流程的piid    -------------");
      logger.info(userId);
      List piids = this.acitivityIdentityS.getConnectPiidByUserId(userId);
      return piids != null?new ResultJson(0, "查找与指定人相关的流程成功", piids):new ResultJson(1, "查找与指定人相关的流程失败", (Object)null);
   }

   @DeleteMapping
   public ResultJson DeleteAllData() {
      return null;
   }

   @GetMapping({"/nodes/all/piid/{piid}"})
   public ResultJson findAllHisActsBypiid(@PathVariable("piid") String piid) {
      logger.info("--C-------- 根据任务piid,查询当前流程实例的所有任务节点（包括完成和未完成任务的候选执行人,不包括分支节点）      -------------");
      logger.info(piid);
      if(StringUtils.isBlank(piid)) {
         return null;
      } else {
         List acts = this.activityS.findAllHisActsBypiid(piid);
         return new ResultJson(0, "查找成功", acts);
      }
   }
}
