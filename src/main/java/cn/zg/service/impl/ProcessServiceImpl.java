package cn.zg.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cn.zg.dao.inter.EmployeeRepository;
import cn.zg.dao.inter.OrganizationRepository;
import cn.zg.dao.inter.ProcessInsectionRepository;
import cn.zg.dao.inter.RoleRepository;
import cn.zg.entity.daoEntity.Emploee;
import cn.zg.entity.daoEntity.Organization;
import cn.zg.entity.daoEntity.ProblemInspection;
import cn.zg.service.inter.ProcessServiceInter;
import cn.zg.utils.globalUtils.GlobalUtil;

/**
 * @ClassName: ProcessServiceImpl
 * @Description: 流程控制接口实现
 * @author zhugang
 * @date 2018年10月16日
 */
@Service
public class ProcessServiceImpl implements ProcessServiceInter {
	private static Logger logger = LoggerFactory.getLogger( ProcessServiceImpl.class );
	
	@Autowired
    private RepositoryService repositoryService;
	
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private HistoryService historyService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ProcessInsectionRepository processInsectionRepository;
	/**   
	 * @Title: deployMechatronicsProcess   
	 * @Description: 部署机电仪流程定义  
	 * @param: @return      
	 * @return: Deployment        
	 * @throws FileNotFoundException 
	 */  
	public Deployment deployMechatronicsProcess()  {
		//获取资源相对路径
//		String pngPath = "process/repairProcess.png";	
//		String bpmnPath = "process/repairProcess.bpmn";					
		//读取资源作为一个输入流
//		FileInputStream pngfileInputStream = new FileInputStream( pngPath );
//		FileInputStream bpmnfileInputStream = new FileInputStream( bpmnPath );
		Deployment deployment = repositoryService.createDeployment()
				 .name( "净化厂机电仪检维修流程")
//				 .addInputStream( "repairProcess.bpmn",  bpmnfileInputStream )
//				 .addInputStream( "repairProcess.png",  pngfileInputStream )
				 .addClasspathResource("process/repairProcess.bpmn")
				 .addClasspathResource("process/repairProcess.png")
				 .deploy();
		return deployment;
	}
	
	/**   
	 * @Title: getMechatronicsProcess   
	 * @Description: 根据流程部署，获取流程定义  
	 * @param: @param deployment
	 * @param: @return      
	 * @return: ProcessDefinition        
	 */  
	public ProcessDefinition getMechatronicsProcess( Deployment deployment ) {
		ProcessDefinition processDefinition = 
				repositoryService.createProcessDefinitionQuery()
				.deploymentId( deployment.getId() )
				.singleResult();
		return processDefinition;
	}
	
	/**   
	 * @Title: startMechatronicsProcess   
	 * @Description: 根据流程定义，启动流程  
	 * @param: @param processDefinition
	 * @param: @return      
	 * @return: String        
	 */  
	public String startMechatronicsProcess( 
			ProcessDefinition processDefinition, Map<String,Object> vars,
			ProblemInspection problemInspection ){
		/*
		 * 启动流程
		 */
		ProcessInstance processInstance = 
				runtimeService.startProcessInstanceById( 
						processDefinition.getId(), 
						problemInspection.getPiid() , 
						vars );
        String processId = processInstance.getId();
        logger.debug( "流程实例ID" + processId );
        
        /*
         * 保存流程实例ID、当前流程任务ID到业务表
         */
        Task ativeTask = taskService.createTaskQuery()
        		.processInstanceId( processId )
        		.singleResult();
        logger.debug( "当前流程任务ID" + ativeTask.getId() );
        try {
        	problemInspection.setCurrentPrid( processId ); 
        	problemInspection.setCurrentTsid( ativeTask.getId() );
        	processInsectionRepository.saveAndFlush( problemInspection );
		} catch (Exception e) {
			logger.debug( "保存流程实例ID的prid失败" );
		}
        return processId;
	}
	
	/**   
	 * @Title: startMechatronicsProcess   
	 * @Description: 根据流程key，启动流程   
	 * @param: @param mechatronicsProcesskey
	 * @param: @param vars
	 * @param: @param problemInspection      
	 * @return: void        
	 */  
	public void startMechatronicsProcess( 
			String mechatronicsProcesskey, Map<String,Object> vars,
			ProblemInspection problemInspection ){
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey( 
				mechatronicsProcesskey, 
				problemInspection.getPiid(), 
				vars );
	}
	
	/**   
	 * @Title: compeletCandidateTask   
	 * @Description: 完成组任务节点
	 * @param: @param taskId
	 * @param: @param userId
	 * @param: @param nextNodeExecutor      
	 * @return: void        
	 */  
	public void compeletCandidateTask( 
			String taskId, String userId, String nextNodeExecutor ) {
		//认领任务
		taskService.claim( taskId, userId );
		
		//完成任务
		Map<String,Object> var = new HashMap<String,Object>();
		var.put( "pureArrangeExecutor", nextNodeExecutor );
		taskService.complete( taskId, var );
	}
	
	/**   
	 * @Title: compeletTask   
	 * @Description: 完成个人任务节点  
	 * @param: @param taskId
	 * @param: @param nextNodeExecutor      
	 * @return: void        
	 */  
	public void compeletTask( String taskId, String nextNodeExecutor ) {
		Map<String,Object> var = new HashMap<String,Object>();
		var.put( "repairArrangeExecutor" , nextNodeExecutor.trim() );
		taskService.complete( taskId, var );
	}
	
	/**   
	 * @Title: transCanTaskToPer   
	 * @Description: 根据taskID，转办组任务 
	 * @param: @param taskId
	 * @param: @param userId      
	 * @return: void        
	 */  
	public void transCanTaskToPer( String taskId, String userId ) {
		//认领任务
		taskService.claim( taskId, userId );
	}
	
	/**   
	 * @Title: transTask   
	 * @Description: 根据taskID，转办任务   
	 * @param: @param taskId
	 * @param: @param userId      
	 * @return: void        
	 */  
	public void transTask( String taskId, String userId ) {
		//转办任务
		taskService.setAssignee( taskId, userId );
	}
	
	/**   
	 * @Title: executeCurrentProcessNode   
	 * @Description: 根据流程实例ID完成任务 
	 * @param: @param processId      
	 * @return: void        
	 */  
	public void executeCurrentProcessNode( String processId,
			ProblemInspection problemInspection ){
		Task task = taskService.createTaskQuery()
						.processInstanceId( processId )
						.singleResult();
		taskService.complete( task.getId() );
		
		/*
		 * 更新流程最新节点ID，到上报表中
		 */
		//获取当前流程实例任务节点
		Task activeTask = 
				taskService.createTaskQuery().taskId( task.getId() ).active().singleResult();
		String activeTaskId = activeTask.getId();
		System.out.println( "S-activeTaskId : " + activeTaskId );
		//更新tsid到业务表中
		problemInspection.setCurrentTsid( activeTaskId );
		try {
			processInsectionRepository.save( problemInspection );
		} catch (Exception e) {
			logger.debug( "更新tsid到业务表中失败" );
		}
		
	}
	
	/**   
	 * @Title: executeCurrentProcessNode   
	 * @Description: 根据流程实例和流程变量，ID完成任务   
	 * @param: @param processId
	 * @param: @param vars      
	 * @return: void        
	 */  
	public void executeCurrentProcessNode( String processId , 
			Map<String,Object> vars, ProblemInspection problemInspection  ) {
		Task task = taskService.createTaskQuery()
						.processInstanceId( processId )
						.singleResult();
		taskService.complete( task.getId() , vars );
	}
	
	/**   
	 * @Title: getTasksByAssignee   
	 * @Description: 查询用户个人任务
	 * @param: @param userId
	 * @param: @return      
	 * @return: List<Task>        
	 */  
	public List<Task> getPersonalTasksByAssignee( String userName ){
		List<Task> tasks = taskService.createTaskQuery()
				.taskAssignee( userName )
				.orderByTaskCreateTime()
				.desc()
				.list();
		return tasks;
	}
	
	/**   
	 * @Title: getCandidateTasksByAssignee   
	 * @Description:  查询用户组任务   
	 * @param: @param userId
	 * @param: @return      
	 * @return: List<Task>        
	 */  
	public List<Task> getCandidateTasksByAssignee( String userName ){
		List<Task> tasks = taskService.createTaskQuery()
				.taskCandidateUser( userName )
				.orderByTaskCreateTime()
				.desc()
				.list();
		return tasks;
	}
	
	/**   
	 * @Title: getCanPerTasksByAssignee   
	 * @Description: 查询用户的个人任务+组任务
	 * @param: @param userId
	 * @param: @return      
	 * @return: List<Task>        
	 */  
	public List<Task> getCanPerTasksByAssignee( String userName ){
		List<Task> personalTasks = taskService.createTaskQuery()
				.taskAssignee( userName )
				.orderByTaskCreateTime()
				.desc()
				.list();
		List<Task> candidatetasks = taskService.createTaskQuery()
				.taskCandidateUser( userName )
				.orderByTaskCreateTime()
				.desc()
				.list();
		personalTasks.addAll( candidatetasks );				
		return personalTasks;
	}
	
	/**   
	 * @Title: getProblemInspectionByTaskId   
	 * @Description: 根据任务id,查询任务对应的业务数据对象  
	 * @param: @param taskId
	 * @param: @return      
	 * @return: String        
	 */  
	public String getProblemInspectionByTaskId( String taskId ) {
		Task task = taskService.createTaskQuery()
				.taskId( taskId )
				.singleResult();
		ProcessInstance pi = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId( task.getProcessInstanceId() )
				.singleResult();
		
		String businessKey = "";
		//判断
		if( pi == null || task == null ) {
			return "";
		}
		try {
			businessKey = pi.getBusinessKey();
		} catch (Exception e) {
			return "";
		}		
		return businessKey == null?"":businessKey ;
	}	
	
	/**   
	 * @Title: findProcessDefinitionByTaskId   
	 * @Description: 根据任务id查询流程定义
	 * @param: @param taskId
	 * @param: @return      
	 * @return: ProcessDefinition        
	 */  
	public ProcessDefinition findProcessDefinitionByTaskId( String taskId ) {
		if( StringUtils.isBlank( taskId ) ) {
			logger.debug( "findProcessDefinitionByTaskId方法传入参数为空" );
			return null;
		}	
		
		Task task = findTaskById( taskId );
		String pdid = task.getProcessDefinitionId();
		ProcessDefinition processDefinition = repositoryService
				.getProcessDefinition( pdid );
		
		if( taskService == null) {
			logger.debug( "findProcessDefinitionByTaskId查询ProcessDefinition不存在" );
			return null;
		}		
		return processDefinition;
	}
	
	/**   
	 * @Title: findProcessDefinitionEntityByTaskId   
	 * @Description: 根据任务id查询流程定义实现  
	 * @param: @param taskId
	 * @param: @return      
	 * @return: ProcessDefinitionEntity        
	 */  
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId( String taskId ) {
		if( StringUtils.isBlank( taskId ) ) {
			logger.debug( "findProcessDefinitionEntityByTaskId方法传入参数taskId为空或null" );
			return null;
		}	
		
		Task task = findTaskById( taskId );
		String pdid = task.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)
				repositoryService.getProcessDefinition( pdid );
		
		if( processDefinitionEntity == null) {
			logger.debug( "findProcessDefinitionByTaskId查询ProcessDefinition不存在" );
			return null;
		}		
		return processDefinitionEntity;
	}
	
	/**   
	 * @Title: findTaskById   
	 * @Description: 根据任务ID查询任务  
	 * @param: @param taskId
	 * @param: @return      
	 * @return: Task        
	 */  
	public Task findTaskById( String taskId ) {
		if( StringUtils.isBlank( taskId ) ) {
			logger.debug( "findTaskById方法传入参数为空" );
			return null;
		}	
		
		Task task= taskService.createTaskQuery()
				.taskId( taskId )
				.singleResult();
		
		if( taskService == null) {
			logger.debug( "findTaskById查询Task任务不存在" );
			return null;
		}
		return task;
	}
	
	/**   
	 * @Title: findActivityImplByTaskId   
	 * @Description: 根据任务ID和活动ID，得到活动节点 
	 * @param: @param taskId
	 * @param: @param activityId
	 * @param: @return      
	 * @return: ActivityImpl        
	 */  
	public ActivityImpl findActivityImplByTaskActId( String taskId, String activityId ) {
//		ProcessDefinition processDefinition = 
//				findProcessDefinitionByTaskId( taskId );
		ProcessDefinitionEntity processDefinition = 
				findProcessDefinitionEntityByTaskId( taskId );
		//验证参数taskId
		if( StringUtils.isBlank( taskId ) ) {
			logger.debug( "findActivityImplByTaskId方法传入参数taskId为空或null" );
			return null; 			
		}
		//验证参数activityId为null和空
		if( StringUtils.isBlank( activityId ) ) {
			activityId = findTaskById( taskId ).getTaskDefinitionKey(); 		
		}
		//验证参数activityId = END
		if ( activityId.toUpperCase().equals( "END" ) ) {  
            for ( ActivityImpl activityImpl : processDefinition.getActivities() ) {  
                List<PvmTransition> pvmTransitionList = activityImpl  
                        .getOutgoingTransitions();  
                if ( pvmTransitionList.isEmpty() ) {  
                    return activityImpl;  
                }  
            }  
        }  		
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)  
                .findActivity(activityId);	
		return activityImpl;
	}
	
	/**   
	 * @Title: findActivityImplByTaskId   
	 * @Description: 根据任务id，查询活动节点   
	 * @param: @param taskId
	 * @param: @return      
	 * @return: ActivityImpl        
	 */  
	public ActivityImpl findActivityImplByTaskId( String taskId ) {
		Task task = taskService.createTaskQuery().taskId( taskId ).singleResult();
		//获取流程定义id、实例id
		String processDefinitionId = task.getProcessInstanceId();
		String processInstanceId = task.getProcessInstanceId(); 		
		//获取流程实例对象、流程定义对象
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId( processDefinitionId )
				.singleResult();
//		ProcessDefinitionEntity processDefinitionEntity =     //实现方式1
//				(ProcessDefinitionEntity) processDefinition;
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)
				repositoryService.getProcessDefinition( processDefinitionId );
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId( processInstanceId )
				.singleResult();
		//根据流程实例对象-查询活动节点对象-获取活动节点id
		String activityId = processInstance.getActivityId();
		ActivityImpl activityImpl = processDefinitionEntity
				.findActivity( activityId );
		return activityImpl;
	}
	
	/**   
	 * @Title: findActivityImplByPrid   
	 * @Description: 根据流程实例id，查询活动节点     
	 * @param: @param prid
	 * @param: @return      
	 * @return: ActivityImpl        
	 */  
	public ActivityImpl findActivityImplByPrid( String prid ){	
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId( prid.trim() )
				.singleResult();
		String processDefinitionId = processInstance.getProcessDefinitionId();
		//根据流程实例对象-查询活动节点对象-获取活动节点id
		String activityId = processInstance.getActivityId();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId( processDefinitionId )
				.singleResult();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)
				repositoryService.getProcessDefinition( processDefinitionId );
		ActivityImpl activityImpl = processDefinitionEntity
				.findActivity( activityId );
		return activityImpl;
	}
	
	/**   
	 * @Title: saveComment   
	 * @Description: 保存该任务节点备注信息   
	 * @param: @param taskId
	 * @param: @param _comment
	 * @param: @return      
	 * @return: boolean        
	 */  
	public Comment saveCommentService( String taskId, String _comment ){
		Task task = taskService.createTaskQuery()
				.taskId( taskId )
				.singleResult();
		if( task == null ) {
			logger.debug( "S-查询任务失败，为null" );
			return null;
		}
		String processInstanceId = task.getProcessInstanceId();
		Comment comment = 
				taskService.addComment( taskId, processInstanceId, "String", _comment);
		return comment;		
	}
	
	
	
	/**   
	 * @Title: getMonitor   
	 * @Description: 问题评估节点，根据问题上报人名称，查找对应的班组长 
	 * @param: @param executorName
	 * @param: @return      
	 * @return: List<String>       
	 */  
	public List<Emploee> getMonitor( String reportorName ) {
		/*
		 * 查询用户组织
		 */
		List<Emploee> employeeList = employeeRepository.findByEmpName( reportorName ); 	
		//用户组织
		List<Organization> totalOrganization = new ArrayList<Organization>();
		for( Emploee e : employeeList) {
			List<Organization> organizationList = 
					organizationRepository.findByEmpId( e.getEmpId() );
			for( Organization o : organizationList ) {
				totalOrganization.add( o );
			}
		}
		
		/*
		 * 查询角色下所有成员
		 */
		String markRole = "";
		for( Organization o : totalOrganization ) {
			markRole = markRole +  o.getOrgName() + "班长,";
		}
		markRole = markRole.substring( 0, markRole.length() -1 );
		System.out.println("markRole:" + markRole);
		String roleNameLike = "";
		String pureString = "净化";
		String repairString = "维修";
		String testString = "化验";
		if( markRole.contains( "净化") && !markRole.contains( "维修") 
				&& !markRole.contains( "化验") ) {
			roleNameLike = pureString + "%" + "班班长";
		} else if( markRole.contains( "维修") && !markRole.contains( "净化") 
				&& !markRole.contains( "化验") ){
			roleNameLike = repairString + "%" + "班班长";
		} else if( markRole.contains( "化验") && !markRole.contains( "净化") 
				&& !markRole.contains( "维修") ){
			roleNameLike = testString + "%" + "班班长";
		} else if( markRole.contains( "净化") ){
			roleNameLike = pureString + "%" + "班班长";
		} else if( markRole.contains( "维修") ){
			roleNameLike = repairString + "%" + "班班长";
		} else {
			throw new RuntimeException( "获取问题上报人的班长失败……" );
		}
		System.out.println("roleNameLike:"+ roleNameLike);
		List<Emploee> problemEstimateExecutors = 
				employeeRepository.findByRoleName( roleNameLike );			
		return problemEstimateExecutors;
	}
	
	/**   
	 * @Title: getUserHistoryTask   
	 * @Description: 根据上报人名，查询全部上报任务    
	 * @param: @param userName
	 * @param: @return      
	 * @return: List<ProblemInspection>        
	 */  
	public List<ProblemInspection> getUserHistoryTask( 
			String userName, String problemTypeStr ){
		//问题类型
		List<String> problemTypes = new ArrayList<String>();
		String[] problemTypeArr = problemTypeStr.split( "," );
		for( String s : problemTypeArr ) {
			problemTypes.add( s.trim() );
		}
		logger.debug( "查询历史任务：" + problemTypes.toString() );
		logger.debug( "查询历史任务：" + userName );
		List<ProblemInspection> problemInspections = new ArrayList<ProblemInspection>();		
		Sort sort = new Sort( Sort.Direction.ASC, "problemType" );
		problemInspections = 
				processInsectionRepository
				.findAllByReporterAndProblemTypeIn( userName, problemTypes, sort );
		return problemInspections;
	}
	
	
	/**   
	 * @Title: getActivityIdByPridService   
	 * @Description: 根据流程实例ID，获取活动节点ID和名称 
	 * @param: @return      
	 * @return: String        
	 */  
	public String getActivityIdByPridService( String prid ) {		
		ActivityImpl activityImpl = findActivityImplByPrid( prid );
		if( activityImpl == null ) {
			return "";
		}
		String idName = activityImpl.getId();
		idName = idName +  "," + activityImpl.getProperty( "name" );
		logger.debug( "根据流程实例ID，获取活动节点ID和名称 ：" + prid );
		return idName;
	}
	
	/**   
	 * @Title: endProcess   
	 * @Description: 终止流程   
	 * @param: @param taskId
	 * @param: @param vars      
	 * @return: void        
	 */  
	public void endProcess( String taskId, Map<String, Object> vars) {
		/*
		 * 获取流程的end节点
		 */
		ActivityImpl actiImpl = getEndNode( taskId );
		
		/*
		 * 流程跳转
		 */
		try {
			transferProcess( taskId, actiImpl.getId(), vars );
		} catch (Exception e) {
			logger.debug( "S-流程转向失败-" );
		}
				
		/*
		 * 完成流程
		 */
		taskService.complete( taskId );
	}
	
	/**   
	 * @Title: getEndNode   
	 * @Description: 根据taskId，获取当前流程的终点节点   
	 * @param: @param taskId
	 * @param: @return      
	 * @return: ActivityImpl        
	 */  
	public ActivityImpl getEndNode( String taskId ) {
		Task task = taskService.createTaskQuery().taskId( taskId ).singleResult();
		if( task == null ) {
			logger.debug( "S-终止流程-task为null" );
		}
		ProcessDefinitionEntity processDefEntity = ( ProcessDefinitionEntity )
				repositoryService.createProcessDefinitionQuery()
				.processDefinitionId( task.getProcessDefinitionId() )
				.singleResult();
		for (ActivityImpl activityImpl : processDefEntity.getActivities() ) {  
			List<PvmTransition> pvmTransitionList = activityImpl  
				.getOutgoingTransitions();  
			if ( pvmTransitionList.isEmpty() ) {  
				return activityImpl;  
			}  
		}  
		return null;
	}
	
	
	/**   
	 * @Title: transferProcess   
	 * @Description: 流程转向 
	 * @param: @param taskId
	 * @param: @param actId
	 * @param: @param vars      
	 * @return: void        
	 */  
	public void transferProcess( 
			String taskId, String actId, Map<String, Object> vars) {
		/*
		 * 当前节点、目标节点
		 */
		ActivityImpl startAct = findActivityImplByTaskId( taskId );
		ActivityImpl targetAct = findActivityImplByTaskActId( taskId, actId );
		
		/*
		 * 重建流向
		 */
		//清空当前流向,返回流向集
		List<PvmTransition> oriPvmTransitionList = clearTransition( startAct );
		//创建新流向
		TransitionImpl newTransition = startAct.createOutgoingTransition(); 
		//设置新流向
		newTransition.setDestination( targetAct ); 
		
		/*
		 * 完成转向
		 */
		 taskService.complete( taskId, vars );  
		
		/*
		 * 还原流向
		 */
		// 删除目标节点新流入  
		 targetAct.getIncomingTransitions().remove( newTransition );  	  
	    // 还原以前流向  
	    restoreTransition( startAct, oriPvmTransitionList);  
	}
	
	/**   
	 * @Title: restoreTransition   
	 * @Description: 还原以前流向     
	 * @param: @param startAct
	 * @param: @param oriPvmTransitionList      
	 * @return: void        
	 */  
	private void restoreTransition( 
			ActivityImpl startAct, List<PvmTransition> oriPvmTransitionList) {
		 // 清空现有流向  
        List<PvmTransition> pvmTransitionList = startAct.getOutgoingTransitions();  
        pvmTransitionList.clear();  
        // 还原以前流向  
        for (PvmTransition pvmTransition : oriPvmTransitionList) {  
            pvmTransitionList.add(pvmTransition);  
        }  		
	}

	/**   
	 * @Title: clearTransition   
	 * @Description: 清空当前流程节点的流向  
	 * @param: @param activityImpl
	 * @param: @return      
	 * @return: List<PvmTransition>        
	 */  
	private List<PvmTransition> clearTransition( ActivityImpl activityImpl ) {  
        // 存储当前节点所有流向临时变量  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for ( PvmTransition pvmTransition : pvmTransitionList ) {  
            oriPvmTransitionList.add( pvmTransition );  
        }  
        pvmTransitionList.clear();  
  
        return oriPvmTransitionList;  
    }  
	
	/**   
	 * @Title: getHistoryAct   
	 * @Description: 根据流程piid,获取流程历史节点 
	 * @param: @param piid
	 * @param: @return      
	 * @return: List<HistoricActivityInstance>        
	 */  
	public List<HistoricActivityInstance> getHisActByPiid( String piid ) {
		List<HistoricActivityInstance> lists = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId( piid )
				.orderByHistoricActivityInstanceStartTime()
				.list();
		return lists;
	}
	
	/**   
	 * @Title: getHisActBytaskId   
	 * @Description: 根据流程taskId,获取流程历史节点   
	 * @param: @param taskId
	 * @param: @return      
	 * @return: List<HistoricActivityInstance>        
	 */  
	public List<HistoricActivityInstance> getHisActBytaskId( String taskId ) {
		Task task = taskService.createTaskQuery().taskId( taskId ).singleResult();
		String piid = task.getProcessInstanceId();
		List<HistoricActivityInstance> lists = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId( piid )
				.orderByHistoricActivityInstanceStartTime()
				.list();
		return lists;
	}
}
