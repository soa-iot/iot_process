package cn.soa.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import cn.soa.service.abs.BussinessSA;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.BussinessSI;

/**
 * @ClassName: ActivityS
 * @Description: 流程服务层
 * @author zhugang
 * @date 2019年5月6日
 */
@Service
public class ActivityS implements ActivitySI{
	private static Logger logger = LoggerFactory.getLogger( ActivityS.class );
	
	@Autowired
    private RepositoryService repositoryService;
	
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private HistoryService historyService;
    
    @Autowired
    private TaskService taskService;
    
//    @Autowired
//    private BussinessSA bussinessSA;
    
    @Autowired
    private BussinessSI bussinessSI;
    
    
    /**   
     * @Title: deployMechatronicsProcess   
     * @Description:  部署流程 (name,xmlUrl,pngUrl)
     * @return: Deployment        
     */  
    @Override
    public Deployment deployProcess( String name, String xmlUrl, String pngUrl ){		
    	try {
    		//获取资源相对路径		
    		//读取资源作为一个输入流
    		Deployment deployment = repositoryService.createDeployment()
   				 .name( name )
   				 .addClasspathResource( xmlUrl )
   				 .addClasspathResource( pngUrl )
   				 .deploy();
    		return deployment;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    /**   
     * @Title: deployMechatronicsProcess   
     * @Description:  部署流程 (xmlUrl,pngUrl)
     * @return: Deployment        
     */  
    @Override
    public Deployment deployProcessNoName( String xmlUrl, String pngUrl ){		
    	try {
    		Deployment deployment = repositoryService.createDeployment()
   				 .addClasspathResource( xmlUrl )
   				 .addClasspathResource( pngUrl )
   				 .deploy();
    		return deployment;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    /**   
     * @Title: getMechatronicsProcess   
     * @Description: 根据流程部署对象，获取流程定义对象  
     * @return: ProcessDefinition        
     */  
    @Override
    public ProcessDefinition getProcessDefinition( Deployment deployment ) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId( deployment.getId() )
				.singleResult();
		return processDefinition;
	}
    
    /**   
     * @Title: getProcessDefinitions   
     * @Description: 获取流程所有流程定义对象  
     * @return: List<ProcessDefinition>        
     */ 
    @Override
    public List<ProcessDefinition> getProcessDefinitions(){
    	try {
    		List<ProcessDefinition> processDefinitions = 
    				repositoryService.createProcessDefinitionQuery().list();
    		return processDefinitions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**   
     * @Title: startProcess   
     * @Description: 启动流程  
     * @return: void        
     */  
    @Override
    public String startProcess( String dfid, 
    		String bsid, Map<String,Object> vars ) {
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById( dfid, bsid, vars );
		return processInstance.getId();
    }
    
    /**   
     * @Title: startProcess   
     * @Description: 启动流程(无业务主键id)  
     * @return: void        
     */  
    @Override
    public String startProcessNobsid( String dfid, 
    		String bsid, Map<String,Object> vars ) {
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById( dfid, bsid, vars );
		return processInstance.getId();
    }
    
    /**   
     * @Title: getProcessFile   
     * @Description: 获取activity流程配置文件格式为bpmn的全部文件（已指定目录/process）
     * @return: List<Map<String,Object>>        
     */  
    @Override
    public List<Map<String,Object>> getconfigFileBPMN() {
    	List<Map<String,Object>> files = new ArrayList<Map<String,Object>>();
    	Map<String,Object> fileInfos = new HashMap<String,Object>();
    	//获取跟目录
    	File path;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
			if(!path.exists()) path = new File("");
	    	System.out.println("path:"+path.getAbsolutePath());

	    	//如果上传目录为/static/images/upload/，则可以如下获取：
	    	File upload = new File(path.getAbsolutePath(),"/src/main/resources/process");
	    	//在开发测试模式时，得到的地址为：{项目跟目录}/target/static/images/upload/
	    	//在打包成jar正式发布时，得到的地址为：{发布jar包目录}/static/images/upload/
//	      	List<File> fileList = (List<File>)FileUtils.listFiles(dir,new String[]{"doc"},true);//列出该目录下的所有doc文件，递归（扩展名不必带.doc）
//	     	List<File> fileList = (List<File>)FileUtils.listFiles(dir,null,true);//列出该目录下的所有文件，递归
	    	List<File> fileList = (List<File>)FileUtils.listFiles(upload,new String[]{"bpmn"},false);//列出该目录下的所有文件，不递归
	    	for( File f : fileList ) {
	    		fileInfos.put( "name", f.getName() );
	    		fileInfos.put( "apath", f.getAbsolutePath() );
	    		fileInfos.put( "cpath", f.getCanonicalPath() );
	    		fileInfos.put( "path", f.getPath() );
	    	}
	    	files.add( fileInfos );
	    	logger.debug( files.toString() );
	    	return files;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}   	
    } 
    
    /**   
     * @Title: nextNode   
     * @Description: 执行流转下一个节点  (根据任务tsid)
     * @return: void        
     */  
    @Override
    @Transactional
    public boolean nextNodeByTSID( String tsid, String var, String varValue, String comments,
    		String nodeid, Map<String,Object> map ) {
    	try {
    		/*
        	 * 业务处理
        	 */
        	String s = bussinessSI.nextNode(nodeid, map);
        	
        	/*
        	 * 加入节点流程变量,流程流转下一个节点
        	 */
        	if( StringUtils.isNotBlank( var ) ) {
        		HashMap<String, Object> vars = new HashMap<String,Object>();
        		vars.put(var, varValue);
        		taskService.complete( tsid, vars );
        	}else {
        		taskService.complete( tsid );
        	}
        	return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
   	
    }
    
    /**   
     * @Title: nextNodeByPIID   
     * @Description:   执行流转下一个节点 (根据任务piid)
     * @return: void        
     */  
    @Override
    @Transactional
    public boolean nextNodeByPIID( String tsid, String var, String varValue, String comments,
    		String nodeid, Map<String,Object> map ) {
    	try {
    		/*
        	 * 业务处理
        	 */
        	String s = bussinessSI.nextNode(nodeid, map);
        	
        	/*
        	 * 加入节点流程变量,流程流转下一个节点
        	 */
        	if( StringUtils.isNotBlank( var ) ) {
        		HashMap<String, Object> vars = new HashMap<String,Object>();
        		vars.put(var, varValue);
        		taskService.complete( tsid, vars );
        	}else {
        		taskService.complete( tsid );
        	}
        	return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}   	
    }
    
    /**   
     * @Title: findTaskById   
     * @Description:  根据任务tsid查询任务 
     * @return: Task        
     */  
    @Override
    public Task findTaskById( String tsid ) {
		if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "findTaskById方法传入参数为空" );
			return null;
		}	
		
		Task task= taskService.createTaskQuery()
				.taskId( tsid )
				.singleResult();
		
		if( taskService == null) {
			logger.debug( "findTaskById查询Task任务不存在" );
			return null;
		}
		return task;
	}
    
    /**   
     * @Title: getEndNode   
     * @Description: 获取流程的最终节点  
     * @return: ActivityImpl        
     */  
    @Override
    public ActivityImpl getEndNode( String tsid ) {
		Task task = taskService.createTaskQuery().taskId( tsid ).singleResult();
		if( task == null ) {
			logger.debug( "--S---------获取流程的最后节点失败---tsid为null" );
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
     * @Title: clearTransition   
     * @Description: 清楚当前的流程的各种指向  
     * @return: List<PvmTransition>        
     */  
    @Override
    public List<PvmTransition> clearTransition( ActivityImpl activityImpl ) {  
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
     * @Title: restoreTransition   
     * @Description:  还原流程节点流向
     * @return: void        
     */  
    @Override
    public void restoreTransition( 
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
     * @Title: findActivityImplByTaskId   
     * @Description: 根据任务tsid查询当前流程实例处于的节点对象实现  
     * @return: ActivityImpl        
     */ 
    @Override
    public ActivityImpl findActivityImplByTaskId( String tsid ) {
    	if( StringUtils.isBlank(tsid) ) {
			logger.debug( "--S---------任务tsid为null或空---------" );
			return null;
		}
		Task task = taskService.createTaskQuery().taskId( tsid ).singleResult();
		if( task == null ) {
			logger.debug( "--S---------根据任务tsid查询任务节点不存在---------" );
			return null;
		}
		//获取流程定义id、实例id
		String processDefinitionId = task.getProcessDefinitionId();
		String processInstanceId = task.getProcessInstanceId(); 		
		//获取流程实例对象、流程定义对象
//		ProcessDefinition processDefinition = repositoryService
//				.createProcessDefinitionQuery()
//				.processDefinitionId( processDefinitionId )
//				.singleResult();
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
     * @Title: findProcessDefinitionEntityByTaskId   
     * @Description: 根据任务tsid查询当前流程定义对象  
     * @return: ProcessDefinitionEntity        
     */  
    @Override
    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId( String tsid ) {
		if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务tsid为null-------------" );
			return null;
		}	
		
		Task task = findTaskById( tsid );
		if( task == null ) {
			logger.debug( "--S---------根据任务tsid查询任务节点不存在---------" );
			return null;
		}
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
     * @Title: findActivityImplByTaskActId   
     * @Description: 根据任务tsid、流程节点id查询流程节点对象实现 
     * @return: ActivityImpl        
     */ 
    @Override
    public ActivityImpl findActivityImplByTaskActId( String tsid, String activityId ) {
    	if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务tsid为null-------------" );
			return null;
		}	
		ProcessDefinitionEntity processDefinition = 
				findProcessDefinitionEntityByTaskId( tsid );
		if( processDefinition == null ) {
			logger.debug( "---S--------根据任务tsid查询流程定义对象为null-------------" );
			return null;
		}	
		
		//验证参数activityId为null和空
		if( StringUtils.isBlank( activityId ) ) {
			activityId = findTaskById( tsid ).getTaskDefinitionKey(); 		
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
     * @Title: transferProcess   
     * @Description: 流程跳转(提供流程变量)  
     * @return: void        
     */ 
    @Override
    public void transferProcessInVars( 
			String tsid, String actId, Map<String, Object> vars ) {
		/*
		 * 当前节点、目标节点
		 */
		ActivityImpl startAct = findActivityImplByTaskId( tsid );
		ActivityImpl targetAct = findActivityImplByTaskActId( tsid, actId );
		
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
		 taskService.complete( tsid, vars );  
		
		/*
		 * 还原流向
		 */
		// 删除目标节点新流入  
		 targetAct.getIncomingTransitions().remove( newTransition );  	  
	    // 还原以前流向  
	    restoreTransition( startAct, oriPvmTransitionList);  
	}
    
    /**   
     * @Title: transferProcessNoVars   
     * @Description: 流程跳转(不提供流程变量)   
     * @return: void        
     */ 
    @Override
    public void transferProcessNoVars( String tsid, String actId ) {
		/*
		 * 当前节点、目标节点
		 */
		ActivityImpl startAct = findActivityImplByTaskId( tsid );
		ActivityImpl targetAct = findActivityImplByTaskActId( tsid, actId );
		
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
		 taskService.complete( tsid );  
		
		/*
		 * 还原流向
		 */
		// 删除目标节点新流入  
		 targetAct.getIncomingTransitions().remove( newTransition );  	  
	    // 还原以前流向  
	    restoreTransition( startAct, oriPvmTransitionList);  
	}
    
    /**   
     * @Title: endProcess   
     * @Description: 终止流程
     * @return: void        
     */  
    @Override
    public String endProcess( String tsid ) {
    	if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务tsid为null-------------" );
			return null;
		}	
    	
		/*
		 * 获取流程的end节点
		 */
		ActivityImpl actiImpl = getEndNode( tsid );
		if( actiImpl == null ) {
			return null;
		}
		
		/*
		 * 流程跳转
		 */
		try {
			transferProcessNoVars( tsid, actiImpl.getId() );
		} catch (Exception e) {
			logger.debug( "--S---------流程转向失败----" );
			return null;
		}
				
		/*
		 * 完成流程
		 */
		taskService.complete( tsid );
		return "终止流程成功";		
	}
    
    /**   
     * @Title: endProcessInComment   
     * @Description:  终止流程（批准信息） 
     * @return: String        
     */  
    @Override
    public String endProcessInComment( String tsid, String comment ) {
    	if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务tsid为null-------------" );
			return null;
		}	
    	
		/*
		 * 获取流程的end节点
		 */
		ActivityImpl actiImpl = getEndNode( tsid );
		if( actiImpl == null ) {
			return null;
		}
		
		/*
		 * 流程跳转
		 */
		try {
			transferProcessNoVars( tsid, actiImpl.getId() );
		} catch (Exception e) {
			logger.debug( "--S---------流程转向失败----" );
			return null;
		}
				
		/*
		 * 完成流程
		 */
		taskService.complete( tsid);
		return "终止流程成功";		
	}
    
    /**   
     * @Title: getHistoryNodesByPiid   
     * @Description: 根据流程piid，查询该流程的历史节点  
     * @return: List<HistoricActivityInstance>        
     */ 
    @Override
    public List<HistoricActivityInstance> getHistoryNodesByPiid( String piid ){
    	if( StringUtils.isBlank( piid ) ) {
			logger.debug( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		List<HistoricActivityInstance> lists = historyService
    				.createHistoricActivityInstanceQuery()
    				.processInstanceId( piid )
    				.orderByHistoricActivityInstanceStartTime()
    				.list();
    		return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}   	
    }
    
    /**   
     * @Title: getHistoryNodesByPiid   
     * @Description: 根据当tsid，查询该流程的历史节点  
     * @return: List<HistoricActivityInstance>        
     */  
    @Override
    public List<HistoricActivityInstance> getHistoryNodesByTsid( String tsid ){
    	if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		Task task = taskService.createTaskQuery().taskId( tsid ).singleResult();
    		String piid = task.getProcessInstanceId();
    		List<HistoricActivityInstance> lists = historyService
    				.createHistoricActivityInstanceQuery()
    				.processInstanceId( piid )
    				.orderByHistoricActivityInstanceStartTime()
    				.list();
    		return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}   	
    }
    
	/**   
	 * @Title: getBeforeNodesByTsid   
	 * @Description:   根据任务tsid，查询流程当前节点的上一个节点 
	 * @return: HistoricActivityInstance        
	 */ 
    @Override
	public HistoricActivityInstance getBeforeNodesByTsid ( String tsid ){
    	if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务tsid为null-------------" );
			return null;
		}	
    	/*
    	 * 查询所有历史节点
    	 */
    	try {
    		List<HistoricActivityInstance> historyNodes = getHistoryNodesByTsid( tsid );
    		if( historyNodes.size() > 0 ) {
    			return historyNodes.get( historyNodes.size() - 1 );    			
    		}else {
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	/**   
	 * @Title: getBeforeNodesByPiid   
	 * @Description:  根据流程piid，查询流程当前节点的上一个节点 
	 * @return: HistoricActivityInstance        
	 */  
    @Override
	public HistoricActivityInstance getBeforeNodesByPiid ( String piid ){
    	if( StringUtils.isBlank( piid ) ) {
			logger.debug( "---S--------任务piid为null-------------" );
			return null;
		}	
    
    	/*
    	 * 查询所有历史节点
    	 */
    	try {
    		List<HistoricActivityInstance> historyNodes = getHistoryNodesByPiid( piid );
    		if( historyNodes.size() > 0 ) {
    			return historyNodes.get( historyNodes.size() - 1 );    			
    		}else {
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	/**   
	 * @Title: backToBeforeNode   
	 * @Description: 根据流程任务tsid，回退流程当前节点的上一个节点  
	 * @return: boolean        
	 */
    @Override
	public boolean backToBeforeNode( String tsid ) {
		if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务tsid为null-------------" );
			return false;
		}	
		
		try {
			/*
			 * 查询上一个节点
			 */
			HistoricActivityInstance beforeNode = getBeforeNodesByTsid ( tsid );
			String beforeNodeActid = beforeNode.getActivityId();
			/*
			 * 跳转
			 */
			transferProcessInVars( tsid, beforeNodeActid, null );
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**   
	 * @Title: getAllHistoryInfo   
	 * @Description:  根据流程任务id，获取当前流程的历史节点信息
	 * @return: List<Map<String,Object>>        
	 */ 
    @Override
	public List<Map<String,Object>> getAllHistoryInfos( String tsid ){
		ArrayList<Map<String, Object>> allHistoryInfos = new ArrayList<Map<String,Object>>();
		if( StringUtils.isBlank( tsid ) ) {
			logger.debug( "---S--------任务tsid为null-------------" );
			return null;
		}
		/*
		 * 获取历史节点
		 */
		try {
			List<HistoricActivityInstance> historyNodes = getHistoryNodesByPiid( tsid );
			for( HistoricActivityInstance h : historyNodes ) {
				HashMap<String, Object> tempMap = new HashMap<String, Object>();
				String historyActid = h.getTaskId();
				List<Comment> comments = taskService.getTaskComments( historyActid );
				if( comments.size() > 0 ) {
					for( Comment c : comments ) {
						Object o = tempMap.get( "nodeComment");
						if( o != null ) {
							tempMap.put( "nodeComment", o + c.getFullMessage() );
						}else {
							tempMap.put( "nodeComment",  c.getFullMessage() );
						}					
					}				
				}else {
					tempMap.put( "nodeComment", "" );
				}
				tempMap.put( "nodeId", h.getActivityId() );
				tempMap.put( "nodeName", h.getActivityName() );
				allHistoryInfos.add( tempMap );
			}
			return allHistoryInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    /**   
     * @Title: getPersonalTasksByUsername   
     * @Description:  根据用户姓名，查询该用户个人待办任务 
     * @return: List<Task>        
     */  
    @Override
    public List<Task> getPersonalTasksByUsername( String userName ){
    	try {
    		List<Task> tasks = taskService.createTaskQuery()
    				.taskAssignee( userName )
    				.orderByTaskCreateTime()
    				.desc()
    				.list();
    		return tasks;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/**   
	 * @Title: getCandidateTasksByUsername   
	 * @Description: 根据用户姓名，查询该用户组待办任务   
	 * @return: List<Task>        
	 */  
    @Override
	public List<Task> getCandidateTasksByUsername( String userName ){
		try {
			List<Task> tasks = taskService.createTaskQuery()
					.taskCandidateUser( userName )
					.orderByTaskCreateTime()
					.desc()
					.list();
			return tasks;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	/**   
	 * @Title: getCanPerTasksByAssignee   
	 * @Description:  根据用户姓名，查询用户的所有待办任务（个人任务+组任务）
	 * @param: @param userId
	 * @param: @return      
	 * @return: List<Task>        
	 */  
    @Override
	public List<Task> getAllTasksByUsername( String userName ){
		List<Task> allTasks = new ArrayList<Task>();
		try {
			List<Task> personalTasks = taskService.createTaskQuery()
					.taskAssignee( userName )
					.orderByTaskCreateTime()
					.desc()
					.list();
			allTasks.addAll( personalTasks );
			List<Task> candidatetasks = taskService.createTaskQuery()
					.taskCandidateUser( userName )
					.orderByTaskCreateTime()
					.desc()
					.list();
			allTasks.addAll( candidatetasks );				
			return allTasks;
		} catch (Exception e) {
			e.printStackTrace();		
			return allTasks;
		}				
	}	
}
