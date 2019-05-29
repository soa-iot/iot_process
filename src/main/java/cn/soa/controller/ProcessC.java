package cn.soa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.activiti.engine.repository.Deployment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.soa.entity.ResultJson;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.BussinessSI;
import cn.soa.service.inter.ConfigSI;
import cn.soa.service.inter.ProcessVariableSI;

/**
 * @ClassName: ProcessC
 * @Description: 流程控制 - 控制层
 * @author zhugang
 * @date 2019年5月29日
 */
@RestController
@RequestMapping("/process")
public class ProcessC {
	private static Logger logger = LoggerFactory.getLogger( ProcessC.class );
	
	@Autowired
	private ActivitySI activityS;
	
	@Autowired
	private ConfigSI configS;
	
	@Autowired
	private BussinessSI bussinessS;
	
	@Autowired
	private ProcessVariableSI processVariableS;
		
	/**   
	 * @Title: startProcessC   
	 * @Description:  部署流程
	 * @return: void        
	 */ 
	@PostMapping("/deployment")
	public ResultJson<Boolean> startProcessC( 
			@RequestParam("name") String name,
			@RequestParam("xmlUrl") @NotBlank String xmlUrl,
			@RequestParam("pngUrl") @NotBlank String pngUrl ) {
		logger.debug( "--C----------部署流程---------------" );
		logger.debug( name );
		logger.debug( xmlUrl );
		logger.debug( pngUrl );
		if( name !=null ) {			
			Deployment deployment = activityS.deployProcess( name, xmlUrl, pngUrl );
			if( deployment != null ) {
				return new ResultJson<Boolean>( 0, "部署成功", true );
			}else {
				return new ResultJson<Boolean>( 1, "部署失败", false );
			}
		}else {
			Deployment deployment = activityS.deployProcessNoName( xmlUrl, pngUrl );
			if( deployment != null ) {
				return new ResultJson<Boolean>( 0, "部署成功", true );
			}else {
				return new ResultJson<Boolean>( 1, "部署失败", false );
			}
		}				
	}
	
	/**   
	 * @Title: getProcessConfigFile   
	 * @Description: 获取activity流程配置文件格式为bpmn的全部文件（已指定目录/process）  
	 * @return: ResultJson<List<Map<String,Object>>>        
	 */  
	@GetMapping("/configfiles")
	public ResultJson<List<Map<String,Object>>> getConfigFileBPMN() {
		logger.debug( "--C----------获取activity流程配置文件格式为bpmn的全部文件（已指定目录/process）---------------" );
		List<Map<String, Object>> files = activityS.getconfigFileBPMN();
		if( files != null ) {
			return new ResultJson<List<Map<String,Object>>>( 0, "获取文件成功", files );
		}
		return new ResultJson<List<Map<String,Object>>>( 1, "获取文件失败", files );
	}
	
	/**   
	 * @Title: startProcess   
	 * @Description: 启动流程（同时业务处理）  
	 * @return: ResultJson<String>        
	 */ 
	@PostMapping("/{dfid}")
	public ResultJson<String> startProcess(
			@PathVariable("dfid") @NotBlank String dfid,
			@RequestParam Map<String,Object> bussinessData ){
		logger.debug( "--C--------启动流程（同时业务处理）  -------------" );
		logger.debug( dfid );
		logger.debug( bussinessData.toString() );
		
		/*
		 * 执行业务处理（具体业务处理需要实现以下接口）
		 */
		String bsid = bussinessS.dealProblemReport( bussinessData );
		if( bsid != null ) {
			return new ResultJson<String>( 1, "业务处理失败，流程未启动", "业务处理失败，流程未启动" );
		}
		
		/*
		 * 处理数据库配置流程变量
		 */
		Map<String, Object> basicVars = configS.setVarsAtStart();
		
		/*
		 * 处理临时流程变量
		 */
		Map<String, Object> tempVars = processVariableS.addVarsStartProcess( bussinessData );
				
		/*
		 * 流程启动
		 */
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.putAll(basicVars);
		vars.putAll(tempVars);
		String piid = activityS.startProcess( dfid, bsid, vars );
		
		return new ResultJson<String>( 0, "流程启动成功", piid );
	}
	
	/**   
	 * @Title: nextNode   
	 * @Description: 执行流程的下一步（流程实例piid）   
	 * @return: ResultJson<String>        
	 */ 
	@PutMapping("/nodes/next/{piid}")
	public ResultJson<Boolean> nextNodeByPIID( 
			@PathVariable("piid") String piid,
			@RequestParam("var") String var,
			@RequestParam("varValue") String varValue,
			@RequestParam("comment") String comment,
			@RequestParam("nodeid") String nodeid,
			Map<String,Object> map ){
		logger.debug( "--C-------- 执行流程的下一步     -------------" );
		logger.debug( piid );
		logger.debug( var );
		logger.debug( comment );
		logger.debug( varValue );
		logger.debug( map.toString() );
		boolean b = activityS.nextNodeByPIID(piid, var, varValue, comment, nodeid, map);
		if( b ) {
			return new ResultJson<Boolean>( 0, "流程流转下一个节点任务成功", true );
		}
		return new ResultJson<Boolean>( 1, "流程流转下一个节点任务失败", false );
	}
	
	/**   
	 * @Title: nextNode   
	 * @Description: 执行流程的下一步（任务tsid）   
	 * @return: ResultJson<String>        
	 */ 
	@PutMapping("/nodes/next/{tsid}")
	public ResultJson<Boolean> nextNodeByTSID( 
			@PathVariable("tsid") String tsid,
			@RequestParam("var") String var,
			@RequestParam("varValue") String varValue,
			@RequestParam("comment") String comment,
			@RequestParam("nodeid") String nodeid,
			Map<String,Object> map ){
		logger.debug( "--C-------- 执行流程的下一步     -------------" );
		logger.debug( tsid );
		logger.debug( var );
		logger.debug( comment );
		logger.debug( varValue );
		logger.debug( map.toString() );
		Boolean b = activityS.nextNodeByTSID(tsid, var, varValue, comment, nodeid, map);
		if( b ) {
			return new ResultJson<Boolean>( 0, "流程流转下一个节点任务成功", true );
		}
		return new ResultJson<Boolean>( 1, "流程流转下一个节点任务失败", false );
	}
	
	/**   
	 * @Title: endProcess   
	 * @Description:  终止流程 
	 * @return: ResultJson<String>        
	 */  
	@PutMapping("/nodes/end/{tsid}")
	public ResultJson<String> endProcess(
			@PathVariable("tsid") @NotBlank String tsid,
			@RequestParam(value="comment",required=false) String comment ){
		logger.debug( "--C-------- 终止流程     -------------" );
		logger.debug( tsid );
		logger.debug( comment );
		String s = null;
		if( StringUtils.isBlank(comment) ) {
			s = activityS.endProcess(tsid);
		}else {
			s = activityS.endProcess(tsid);
		}
		if( StringUtils.isBlank( s ) ) {
			return new ResultJson<String>( 1, "闭环流程失败", "闭环流程失败" );
		}
		return new ResultJson<String>( 0, "闭环流程成功", "闭环流程成功" );
	}
	
	/**   
	 * @Title: getHitoryNodeInfos   
	 * @Description:  根据任务tsid，获取流程所有的历史节点 
	 * @return: ResultJson<List<Map<String,Object>>>        
	 */  
	@GetMapping("/nodes/history/{tsid}")
	public ResultJson<List<Map<String,Object>>> getHitoryNodeInfos(
			@PathVariable("tsid") @NotBlank String tsid ){
		logger.debug( "--C-------- 根据任务tsid，获取流程所有的历史节点      -------------" );
		logger.debug( tsid );
		List<Map<String, Object>> historyNodesInfo = activityS.getAllHistoryInfos( tsid );
		if( historyNodesInfo != null && historyNodesInfo.size() > 0  ) {
			return new ResultJson<List<Map<String,Object>>>( 0, "获取流程所有的历史节点成功", historyNodesInfo );
		}
		return new ResultJson<List<Map<String,Object>>>( 0, "获取流程所有的历史节点失败", null );

	}
	
	/**   
	 * @Title: backToBeforeNodes   
	 * @Description:   根据任务tsid，流程返回到上一个节点
	 * @return: ResultJson<Boolean>        
	 */  
	@PutMapping("/nodes/before/{tsid}")
	public ResultJson<Boolean> backToBeforeNodes(
			@PathVariable("tsid") @NotBlank String tsid ){
		logger.debug( "--C-------- 根据任务tsid，流程返回到上一个节点     -------------" );
		logger.debug( tsid );
		boolean b = activityS.backToBeforeNode( tsid );
		if( b ) {
			return new ResultJson<Boolean>( 0, "流程返回到上一个节点成功", true );
		}
		return new ResultJson<Boolean>( 0, "流程返回到上一个节点失败", null );
	}
}