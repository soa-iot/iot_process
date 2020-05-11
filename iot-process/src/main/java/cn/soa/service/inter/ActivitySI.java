package cn.soa.service.inter;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import cn.soa.entity.TodoTask;
import cn.soa.entity.activity.HistoryAct;

@Service
public interface ActivitySI {

	/**   
	 * @Title: deployMechatronicsProcess   
	 * @Description:  部署流程  (name,xmlUrl,pngUrl)  
	 * @return: Deployment        
	 */  
	Deployment deployProcess( String name, String xmlUrl, String pngUrl );
	
	/**   
	 * @Title: deployProcessNoName   
	 * @Description: 部署流程 (xmlUrl,pngUrl)  
	 * @return: Deployment        
	 */  
	Deployment deployProcessNoName(String xmlUrl, String pngUrl);

	/**   
	 * @Title: getProcessFile   
	 * @Description: 获取activity流程配置文件格式为bpmn的全部文件（已指定目录/process）  
	 * @return: List<Map<String,Object>>        
	 */  
	List<Map<String, Object>> getconfigFileBPMN();

	/**   
	 * @Title: getProcessDefinition   
	 * @Description: 根据流程部署对象，获取流程定义对象    
	 * @return: ProcessDefinition        
	 */  
	ProcessDefinition getProcessDefinition(Deployment deployment);

	/**   
	 * @Title: startProcess   
	 * @Description: 启动流程    
	 * @return: void        
	 */  
	String startProcessByDfid( String dfid, String bsid, Map<String, Object> vars);

	/**   
	 * @Title: startProcess   
	 * @Description: 启动流程   
	 * @return: String        
	 */  
	String startProcess(String bsid, Map<String, Object> vars);
	
	/**   
	 * @Title: startProcessNobsid   
	 * @Description: 启动流程(无业务主键id)    
	 * @return: String        
	 */  
	String startProcessNobsid(String dfid, String bsid, Map<String, Object> vars);

	/**   
	 * @Title: nextNodeByTSID   
	 * @Description: 执行流转下一个节点  (根据任务tsid)  
	 * @return: void        
	 */  
	boolean nextNodeByTSID(String tsid, String var, String varValue, String comments);

	/**   
	 * @Title: nextNodeByPIID   
	 * @Description:  执行流转下一个节点 (根据任务piid) 
	 * @return: void        
	 */  
	boolean nextNodeByPIID(  String piid, Map<String,Object> map  );

	/**   
	 * @Title: getEndNode   
	 * @Description:   获取流程的最终节点  
	 * @return: ActivityImpl        
	 */  
	ActivityImpl getEndNode(String tsid);
	
	/**   
	 * @Title: clearTransition   
	 * @Description:  清除当前的流程的各种指向   
	 * @return: List<PvmTransition>        
	 */  
	List<PvmTransition> clearTransition( ActivityImpl activityImpl );

	/**   
	 * @Title: restoreTransition   
	 * @Description: 还原流程节点流向  
	 * @return: void        
	 */  
	void restoreTransition(ActivityImpl startAct, List<PvmTransition> oriPvmTransitionList);

	/**   
	 * @Title: findTaskById   
	 * @Description:  根据任务tsid查询任务  
	 * @return: Task        
	 */  
	Task findTaskById(String tsid);

	/**   
	 * @Title: findActivityImplByTaskId   
	 * @Description: 根据任务tsid查询当前流程实例处于的节点对象实现    
	 * @return: ActivityImpl        
	 */  
	ActivityImpl findActivityImplByTaskId(String tsid);

	/**   
	 * @Title: findProcessDefinitionEntityByTaskId   
	 * @Description: 根据任务tsid查询当前流程定义对象   
	 * @return: ProcessDefinitionEntity        
	 */  
	ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String tsid);

	/**   
	 * @Title: findActivityImplByTaskActId   
	 * @Description: 根据任务tsid、流程节点id查询流程节点对象实现   
	 * @return: ActivityImpl        
	 */  
	ActivityImpl findActivityImplByTaskActId(String tsid, String activityId);

	/**   
	 * @Title: transferProcessInVars   
	 * @Description:流程跳转(提供流程变量)   
	 * @return: void        
	 */  
	void transferProcessInVars(String tsid, String actId, Map<String, Object> vars);

	/**   
	 * @Title: transferProcessNoVars   
	 * @Description: 流程跳转(不提供流程变量)    
	 * @return: void        
	 */  
	void transferProcessNoVars(String tsid, String actId, String userName);

	/**   
	 * @Title: endProcess   
	 * @Description:终止流程   
	 * @return: void        
	 */  
	String endProcessByTsid(String tsid);

	/**   
	 * @Title: endProcessInComment   
	 * @Description: 终止流程（批准信息）   
	 * @return: String        
	 */  
	String endProcessByTsidInComment(String tsid, String comment, String userName);

	/**   
	 * @Title: getHistoryNodesByPiid   
	 * @Description: 根据流程piid，查询该流程的历史节点    
	 * @return: List<HistoricActivityInstance>        
	 */  
	List<HistoricActivityInstance> getHistoryNodesByPiid(String piid);

	/**   
	 * @Title: getHistoryNodesByTsid   
	 * @Description: 根据当tsid，查询该流程的历史节点    
	 * @return: List<HistoricActivityInstance>        
	 */  
	List<HistoricActivityInstance> getHistoryNodesByTsid(String tsid);

	/**   
	 * @Title: getBeforeNodesByTsid   
	 * @Description: 根据任务tsid，查询流程当前节点的上一个节点   
	 * @return: HistoricActivityInstance        
	 */  
	HistoricTaskInstance getBeforeTasksByTsid(String tsid);

	/**   
	 * @Title: getBeforeNodesByPiid   
	 * @Description: 根据流程piid，查询流程当前节点的上一个节点  
	 * @return: HistoricActivityInstance        
	 */  
	HistoricActivityInstance getBeforeTasksByPiid(String piid);

	/**   
	 * @Title: backToBeforeNodeByTsid 
	 * @Description: 根据流程任务tsid，回退流程当前节点的上一个节点    
	 * @return: boolean        
	 */  
	boolean backToBeforeNodeByTsid(String tsid);

	/**   
	 * @Title: getAllHistoryInfos   
	 * @Description: 根据流程任务id，获取当前流程的历史节点 信息  
	 * @return: List<Map<String,Object>>        
	 */  
	List<Map<String, Object>> getHisInfosByTsid(String tsid);

	/**   
	 * @Title: getPersonalTasksByUsername   
	 * @Description: 根据用户姓名，查询该用户个人待办任务   
	 * @return: List<Task>        
	 */  
	List<Task> getPersonalTasksByUsername(String userName);

	/**   
	 * @Title: getCandidateTasksByUsername   
	 * @Description:  根据用户姓名，查询该用户组待办任务 
	 * @return: List<Task>        
	 */  
	List<Task> getCandidateTasksByUsername(String userName);

	/**   
	 * @Title: getAllTasksByUsername   
	 * @Description: 根据用户姓名，查询用户的所有待办任务（个人任务+组任务）  
	 * @return: List<Task>        
	 */  
	List<TodoTask> getAllTasksByUsername(String userName);

	/**   
	 * @Title: getProcessDefinitions   
	 * @Description:  获取流程所有流程定义   
	 * @return: List<ProcessDefinition>        
	 */  
	List<ProcessDefinition> getProcessDefinitions();

	/**   
	 * @Title: getTsidByPiid   
	 * @Description: 根据piid查询当前任务节点的tsid   
	 * @return: String        
	 */  
	String getTsidByPiid(String piid);

	/**   
	 * @Title: getPiidByTsid   
	 * @Description:  根据tsid查询当前任务节点的  piid 
	 * @return: String        
	 */  
	String getPiidByTsid(String tsid);

	/**   
	 * @Title: saveCommentByTsid   
	 * @Description: 根据任务tsid，增加任务节点的备注信息   
	 * @return: boolean        
	 */  
	boolean saveCommentByTsid(String tsid, String comment);

	/**   
	 * @Title: saveCommentByPiid   
	 * @Description: 根据任务piid，增加任务节点的备注信息   
	 * @return: boolean        
	 */  
	boolean saveCommentByPiid(String piid, String comment);

	/**   
	 * @Title: getHisInfosByPiid   
	 * @Description: 根据流程piid，获取当前流程的历史节点信息  
	 * @return: List<Map<String,Object>>        
	 */  
	List<Map<String, Object>> getHisActNodesByPiid(String piid);

	/**   
	 * @Title: getHisTaskNodesByPiid   
	 * @Description: 根据流程piid，查询该流程的历史任务节点    
	 * @return: List<HistoricTaskInstance>        
	 */  
	List<HistoricTaskInstance> getHisTaskNodesByPiid(String piid);

	/**   
	 * @Title: getHisTaskNodeInfosByPiid   
	 * @Description:  根据流程piid，获取当前流程的任务节点信息 
	 * @return: List<Map<String,Object>>        
	 */  
	List<Map<String, Object>> getHisTaskNodeInfosByPiid(String piid);

	/**   
	 * @Title: nextNodeByPIID1   
	 * @Description: 执行流转下一个节点 (根据任务piid) - 非组任务  
	 * @return: boolean        
	 */  
	boolean nextNodeByPIID1(String piid, Map<String, Object> map);

	/**   
	 * @Title: getHisTaskNodesByTsid   
	 * @Description: 根据流程tsid，查询该流程的历史任务节点    
	 * @return: List<HistoricTaskInstance>        
	 */  
	List<HistoricTaskInstance> getHisTaskNodesByTsid(String tsid);

	/**   
	 * @Title: backToBeforeNodeByPiid   
	 * @Description:  根据流程任务piid，回退流程当前节点的上一个节点   - 非组任务
	 * @return: boolean        
	 */  
	boolean backToBeforeNodeByPiid(String piid, String comment );
	
	/**   
	 * @Title: backToBeforeNodeByPiid   
	 * @Description:  根据流程任务piid，回退流程当前节点的上一个节点   - 组任务
	 * @return: boolean        
	 */  
	boolean backToBeforeNodeByPiidInGroup(String piid, Map<String,Object> map );

	/**   
	 * @Title: endProcessByPiid   
	 * @Description:终止流程（piid）   
	 * @return: String        
	 */  
	String endProcessByPiidInComment(String piid, String comment, String userName, String operateName );

	/**   
	 * @Title: transferProcessInVarsByPiid   
	 * @Description: 流程跳转(提供流程变量) - piid    
	 * @return: boolean        
	 */  
	boolean transferProcessByPiid(String piid, Map<String, Object> vars);

	String getActiveTsidByPiid(String piid);

	/**   
	 * @Title: findAllHisActsBypiid   
	 * @Description: 根据任务piid,查询当前流程实例的所有任务节点（包括完成和未完成,）      
	 * @return: List<HistoryAct>        
	 */  
	List<HistoryAct> findAllHisActsBypiid(String piid);

	

	
}
