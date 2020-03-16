package cn.soa.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import cn.soa.entity.TodoTask;
import cn.soa.entity.activity.HistoryAct;
import cn.soa.service.abs.BussinessSA;
import cn.soa.service.inter.AcitivityHistoryActSI;
import cn.soa.service.inter.AcitivityIdentitySI;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.BussinessSI;
import cn.soa.service.inter.ProblemInfoSI;

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
    
    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private AcitivityIdentitySI acitivityIdentityS;
    
//    @Autowired
//    private BussinessSA bussinessSA;
    
    @Autowired
    private BussinessSI bussinessSI;
    
    @Autowired
    private AcitivityHistoryActSI acitivityHistoryActS;
    
    
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
    		logger.info( deployment.toString() );
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
     * @Title: startProcess   
     * @Description: 根据流程定义dfid，启动流程  
     * @return: void        
     */  
    @Override
    public String startProcessByDfid( String dfid, 
    		String bsid, Map<String,Object> vars ) {
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById( dfid, bsid, vars );
		return processInstance.getId();
    }
    
    /**   
     * @Title: startProcess   
     * @Description: 启动流程  
     * @return: void        
     */  
    @Override
    public String startProcess( String bsid, Map<String,Object> vars ) {
		ProcessInstance processInstance = 
				runtimeService.startProcessInstanceByKey("processPure2", bsid, vars);
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
     * @Title: getTsidByPiid   
     * @Description:  根据piid查询当前任务节点的tsid 
     * @return: String        
     */ 
    @Override
    public String getTsidByPiid( String piid ) {
    	if( StringUtils.isBlank( piid )) {
    		logger.info( "------piid为null--------" );
    		return null;
    	}
    	try {
    		List<Task> tasks = taskService.createTaskQuery().processInstanceId(piid).list();
    		if( tasks.size() > 0 ) {
    			logger.info( "tasks.toString()--------------------------"+tasks.toString() );
    			return tasks.get(0).getId();
    		}
    		logger.info( "------未正确找到task对象-------" );
    		return null;    		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**   
	 * @Title: getActiveTsidByPiid   
	 * @Description:  根据piid，查找当前活动任务的tsid 
	 * @return: String        
	 */ 
	@Override
	public String getActiveTsidByPiid( String piid ) {
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		List<HistoricTaskInstance> lists = historyService
    				.createHistoricTaskInstanceQuery()
    				.processInstanceId( piid )
    				.orderByTaskCreateTime()
    				.asc()
    				.list();
    		if( lists != null && lists.size()>0 ) {
    			logger.info( lists.toString() );
    		}else {
    			logger.info( "---------根据流程piid，查询该流程的历史任务节点  为null或空-------------" );
    		}
    		logger.info( "---------根据流程piid，该流程的历史任务节点id------------" + lists.get( lists.size() - 1 ).getId());
    		return lists.get( lists.size() - 1 ).getId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
    }

    
    /**   
     * @Title: getPiidByTsid   
     * @Description:  根据tsid查询当前任务节点的  piid
     * @return: String        
     */
    @Override
    public String getPiidByTsid( String tsid ) {
    	if( StringUtils.isBlank( tsid )) {
    		logger.info( "------tsid为null--------" );
    		return null;
    	}
    	
    	try {
    		Task task = taskService.createTaskQuery().taskId(tsid).singleResult();
    		if( task != null ) {
    			logger.info( task.toString() );
    			return task.getProcessInstanceId();
    		}
    		logger.info( "------未正确找到task对象-------" );
    		return null;    		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    
    /**   
     * @Title: saveCommentByTsid   
     * @Description:  根据任务tsid，增加任务节点的备注信息 
     * @return: boolean        
     */ 
    @Override
    public boolean saveCommentByTsid( String tsid, String comment ) {
    	if( StringUtils.isBlank( tsid )) {
    		logger.info( "------tsid为null--------" );
    		return false;
    	}
    	String piid = getPiidByTsid( tsid );
    	if( StringUtils.isBlank( piid )) {
    		logger.info( "------piid为null--------" );
    		return false;
    	}
    	try {   		
    		taskService.addComment( tsid, piid, comment );
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	return false;
    }
    
    /**   
     * @Title: saveCommentByTsid   
     * @Description:  根据任务piid，增加任务节点的备注信息 
     * @return: boolean        
     */  
    @Override
    public boolean saveCommentByPiid( String piid, String comment ) {
    	if( StringUtils.isBlank( piid )) {
    		logger.info( "------piid为null--------" );
    		return false;
    	}
    	String tsid = getTsidByPiid( piid );
    	if( StringUtils.isBlank( tsid )) {
    		logger.info( "------tsid为null--------" );
    		return false;
    	}
    	try {   		
    		taskService.addComment( tsid, piid, comment );
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
	    	logger.info( files.toString() );
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
     * @Title: nextNode   
     * @Description: 执行流转下一个节点  (根据任务tsid)
     * @return: void        
     */  
    @Override
    @Transactional
    public boolean nextNodeByTSID( String tsid, String var, String varValue, String comments) {
    	try {
        	
        	/*
        	 * 加入节点流程变量,流程流转下一个节点
        	 */
        	if( StringUtils.isNotBlank( var ) ) {
        		HashMap<String, Object> vars = new HashMap<String,Object>();
        		vars.put(var, varValue);
        		taskService.complete( tsid, vars);
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
     * @Description:   执行流转下一个节点 (根据任务piid)- 组任务
     * @return: void        
     */  
    @Override
    @Transactional
    public boolean nextNodeByPIID( String piid, Map<String,Object> map ) { 
    	//检查
    	if( map.get("userName") == null ) {
    		logger.info( "-------执行流转下一个节点 (根据任务piid)--------" );
    		logger.info( "-------userName参数不存在--------" );
    		return false;
    	}
    	
    	/*
    	 * 根据piid获取tsid
    	 */
    	String tsid = getTsidByPiid( piid );
    	
    	Object commentObj = map.get( "comment" );
    	if( commentObj == null ) {
    		logger.info( "-------执行流转下一个节点 (根据任务piid)--------" );
    		logger.info( "-------comment参数不存在--------" );
    	}
    	String comment = map.get( "comment" ).toString();
    	logger.info( "-------流程节点备注信息--------" + comment );
    	
    	try {     
    		//拾取任务
    		taskService.claim( tsid, map.get( "userName" ).toString() );    		
    		
    		/*
    		 * 增加备注信息
    		 */
    		boolean b = saveCommentByPiid( piid, comment );
    		if( b ) {
    			logger.info( "---执行流转下一个节点，保存备注信息成功---------" );
    		}else {
    			logger.info( "---执行流转下一个节点，保存备注信息失败---------" );
    		}
    		map.remove( "comment" );   
    		
    		/*
    		 * 获取操作名称
    		 */
    		String  operateName = "";
    		Object operateNameObj = map.get( "operateName" );
    		logger.info( "---操作名称---------" + operateNameObj );
    		if(operateName != null ) operateName = operateNameObj.toString().trim();
    		map.remove( "operateName" );
    		   
    		/*
    		 * 设置当前任务流程变量 - 定制 -后续改设计模式
    		 */
    		Object areaValue = taskService.getVariable( tsid, "area" );
    		String areaValueStr = "";
//    		Object areaValue = runtimeService.getVariable( piid, "area" );
    		if( areaValue != null ) {
    			areaValueStr = areaValue.toString();
    			logger.info( "---获取当前任务节点流程变量area成功:---------" + areaValue ); 
    		}else {
    			logger.info( "---获取当前任务节点流程变量area失败---------" );
    		}
    		
    		
        	/*
        	 *设置流程变量
        	 */
    		HashMap<String, Object> vars = new HashMap<String,Object>();
    		for( Entry<String, Object> e : map.entrySet() ) {
    			if(StringUtils.isBlank( e.getKey() ) ) {
    				logger.info( "---key---------" + e.getKey() );
    			}
    			taskService.setVariable(tsid, e.getKey(), e.getValue());    			
    			logger.info( "---成功设置流程变量名----" + e.getKey() + "---对应值----" + e.getValue() );
    		}
    		 
    		
    		//设置本节点的流程变量
    		if( StringUtils.isNotBlank( areaValueStr ) ) {
    			logger.info( "---获取当前任务节点流程变量area的areaValueStr:---------" + areaValueStr ); 
    			taskService.setVariableLocal( tsid, "area", areaValueStr );
    		}  		
    		
    		/*
    		 * 流程流转下一个节点
    		 */
    		taskService.complete( tsid );
    		
    		/*
    		 * 添加节点操作名称
    		 */
    		updateOprateNameS( piid, tsid, operateName );
    		
        	return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}   	
    }
    
    /**   
     * @Title: nextNodeByPIID   
     * @Description:   执行流转下一个节点 (根据任务piid) - 非组任务
     * @return: void        
     */  
    @Override
    @Transactional
    public boolean nextNodeByPIID1( String piid, Map<String,Object> map ) { 
    	logger.info( "-------执行流转下一个节点 (根据任务piid)- 非组任务--------" );
    	if( StringUtils.isBlank(piid) ) {
    		logger.info( "-------piid为空或者null--------" );
    	}
    	
    	/*
    	 * 根据piid获取tsid
    	 */
    	String tsid = getTsidByPiid( piid );
    	
    	Object commentObj = map.get( "comment" );
    	if( commentObj == null ) {
    		logger.info( "-------comment参数不存在--------" );
    	}
    	String comment = map.get( "comment" ).toString();
    	logger.info( "-------流程节点备注信息--------" + comment );
	
    	try {     	
    		
    		/*
    		 * 获取操作名称
    		 */
    		String  operateName = "";
    		Object operateNameObj = map.get( "operateName" );
    		logger.info( "---操作名称---------" + operateNameObj );
    		if(operateName != null ) operateName = operateNameObj.toString().trim();
    		map.remove( "operateName" );
    		
    		/*
    		 * 增加备注信息
    		 */
    		boolean b = saveCommentByPiid( piid, comment );
    		if( b ) {
    			logger.info( "---执行流转下一个节点，保存备注信息成功---------" );
    		}else {
    			logger.info( "---执行流转下一个节点，保存备注信息失败---------" );
    		}
    		map.remove( "comment" );
    		
    		/*
    		 * 设置当前任务流程变量 - 定制 -后续改设计模式
    		 */
    		Object areaValue = taskService.getVariable( tsid, "area" );
    		if( areaValue != null ) {
    			logger.info( "---获取流程变量area成功:---------" + areaValue );
    		}else {
    			logger.info( "---获取流程变量area失败---------" );
    		}
    		taskService.setVariableLocal( tsid, "area", areaValue );
    		
        	/*
        	 *设置流程变量
        	 */
    		HashMap<String, Object> vars = new HashMap<String,Object>();
    		for( Entry<String, Object> e : map.entrySet() ) {
    			if(StringUtils.isBlank( e.getKey() ) ) {
    				logger.info( "---流程变量名key为空---------" + e.getKey() );
    				continue;
    			}
    			taskService.setVariable(tsid, e.getKey(), e.getValue());
    			logger.info( "---成功设置流程变量名----" + e.getKey() + "---对应值----" + e.getValue() );
    		}
    		
    		/*
    		 * 流程流转下一个节点
    		 */
    		taskService.complete( tsid );
    		
    		/*
    		 * 添加节点操作名称
    		 */
    		updateOprateNameS( piid, tsid, operateName );
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
			logger.info( "findTaskById方法传入参数为空" );
			return null;
		}	
		
		Task task= taskService.createTaskQuery()
				.taskId( tsid )
				.singleResult();
	
		if( taskService == null) {
			logger.info( "findTaskById查询Task任务不存在" );
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
			logger.info( "--S---------获取流程的最后节点失败---tsid为null" );
		}
		ProcessDefinitionEntity processDefEntity = ( ProcessDefinitionEntity )
				repositoryService.createProcessDefinitionQuery()
				.processDefinitionId( task.getProcessDefinitionId() )
				.singleResult();
		logger.info( "--S---------流程所有节点---" + processDefEntity.toString() );
		logger.info( "--S---------流程所有节点---" + processDefEntity.getActivities().toString() );
		for (ActivityImpl activityImpl : processDefEntity.getActivities() ) {  
			List<PvmTransition> pvmTransitionList = activityImpl  
				.getOutgoingTransitions();  
			logger.info( "--S---------循环---" + activityImpl.toString() );
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
			logger.info( "--S---------任务tsid为null或空---------" );
			return null;
		}
		Task task = taskService.createTaskQuery().taskId( tsid ).singleResult();
		if( task == null ) {
			logger.info( "--S---------根据任务tsid查询任务节点不存在---------" );
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
			logger.info( "---S--------任务tsid为null-------------" );
			return null;
		}	
		
		Task task = findTaskById( tsid );
		if( task == null ) {
			logger.info( "--S---------根据任务tsid查询任务节点不存在---------" );
			return null;
		}
		String pdid = task.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)
				repositoryService.getProcessDefinition( pdid );
		
		if( processDefinitionEntity == null) {
			logger.info( "findProcessDefinitionByTaskId查询ProcessDefinition不存在" );
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
			logger.info( "---S--------任务tsid为null-------------" );
			return null;
		}	
		ProcessDefinitionEntity processDefinition = 
				findProcessDefinitionEntityByTaskId( tsid );
		if( processDefinition == null ) {
			logger.info( "---S--------根据任务tsid查询流程定义对象为null-------------" );
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
     * @Description: 流程跳转(提供流程变量) - piid  
     * @return: void        
     */ 
    @Override
    public boolean transferProcessByPiid( String piid, Map<String, Object> vars ) {

    	logger.info( "---S-------流程跳转(提供流程变量) - piid  ------------" );

    	if( vars == null ) {
    		logger.info( "---S--------流程变量map-vars 为null或空------------" );
			return false;
    	}
    	
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------流程piid为null-------------" );
			return false;
		}	
    	
    	String tsid = getTsidByPiid( piid );
    	if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null或空-------------" );
			return false;
		}	
    	
    	/*
		 * 获取操作名称
		 */
		String  operateName = "";
		Object operateNameObj = vars.get( "operateName" );
		logger.info( "---操作名称---------" + operateNameObj );
		if(operateName != null ) operateName = operateNameObj.toString().trim();
		vars.remove( "operateName" );
    	   	
    	/*
    	 * 增加备注信息
    	 */
    	String comment;
		Object commentObj = vars.get( "comment" );  		
		if(  commentObj != null ) {
			comment = commentObj.toString();
			logger.info( "---流程跳转，备注信息---------" + comment );
			boolean b = saveCommentByTsid( tsid, comment );
	    	if( b ) {
	    		logger.info( "---执行回退上一个节点，保存备注信息成功---------" );
	    	}else {
	    		logger.info( "---执行回退上一个节点，保存备注信息失败---------" );
	    	}
	    	vars.remove( "comment" );
		}else {
			logger.info( "---流程跳转，备注信息不存在---------" );
		}
		

		/*
		 * 设置流程实例全局流程变量到本节点任务的局部流程变量
		 */
		Object areaValue = taskService.getVariable( tsid, "area" );
		if( areaValue != null  && !areaValue.toString().trim().isEmpty()) {
			taskService.setVariableLocal( tsid , "area", areaValue);
			taskService.setVariable( tsid , "area", areaValue);
			logger.info( "---S--------全局流程变量area设置为局部流程变量成功-------------" + areaValue );
		}else {
			logger.info( "---S--------全局流程变量area为空-------------" );
		}
		
    	
    	/*
    	 * 流程跳转
    	 */
    	String actId;
    	Object actIdObj = vars.get( "actId" );
    	if( actIdObj !=null ) {
    		actId = actIdObj.toString();
    		logger.info( "---流程跳转，目标节点actId--------" + actId );
    		String actId1 = new String();
    		actId1 = actId;
    		vars.remove( "actId" );   		
    		logger.info( "---流程跳转，目标节点actId1--------" + actId1 );
    		logger.info( "---流程跳转，vars-------" + vars );
    		transferProcessInVarsAndGroup( tsid, actId, vars );
 
    		/*
    		 * 添加节点操作名称
    		 */
    		updateOprateNameS( piid, tsid, operateName );
    		
    		return true;
    	}else {
    		logger.info( "---流程跳转，流程变量vars不包含actId：-------" + actIdObj );
    		return false;
    	}
    	
    }
    
    /**   
     * @Title: transferProcess   
     * @Description: 流程跳转(提供流程变量) - 非组任务 
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
		 taskService.complete( tsid, vars);  
		
		/*
		 * 还原流向
		 */
		// 删除目标节点新流入  
		 targetAct.getIncomingTransitions().remove( newTransition );  	  
	    // 还原以前流向  
	    restoreTransition( startAct, oriPvmTransitionList);  
	}
    
    /**   
     * @Title: transferProcessInVarsAndGroup   
     * @Description:  流程跳转(提供流程变量) -  组任务 
     * @return: void        
     */  
    public void transferProcessInVarsAndGroup( 
			String tsid, String actId, Map<String, Object> vars ) {
    	Object userNameObj = vars.get( "userName" );
    	String userName = "";
    	if( userNameObj != null  ) {
    		userName = userNameObj.toString();
			logger.info( "---S--------流程执行人userName-------------" + userName );
		}else {
			logger.info( "---S--------流程变量map不包含userName-------------" );
		}
    	
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
		//拾取任务
		taskService.claim( tsid, userName ); 
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
    public void transferProcessNoVars( String tsid, String actId, String userName ) {
		/*
		 * 当前节点、目标节点
		 */
		ActivityImpl startAct = findActivityImplByTaskId( tsid );
		ActivityImpl targetAct = findActivityImplByTaskActId( tsid, actId );
		logger.info( "---S--------当前节点为-------------" + startAct );
		logger.info( "---S--------目标节点为-------------" + targetAct );
		
		/*
		 * 重建流向
		 */
		//清空当前流向,返回流向集
		List<PvmTransition> oriPvmTransitionList = clearTransition( startAct );
		//创建新流向
		TransitionImpl newTransition = startAct.createOutgoingTransition(); 
		//设置新流向
		newTransition.setDestination( targetAct ); 
		
		//拾取任务
		taskService.claim( tsid, userName );  
		
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
     * @Title: endProcessByPiid   
     * @Description:  终止流程（piid） 
     * @return: String        
     */  
    @Override
    public String endProcessByPiidInComment( String piid, String comment, String userName, String operateName ) {
    	if( StringUtils.isBlank( userName ) ) {
			logger.info( "---S--------任务userName为null或空-------------" );
			return null;
		}	
    	
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null或空-------------" );
			return null;
		}	
    	
    	if( StringUtils.isBlank( operateName ) ) {
			logger.info( "---S--------任务operateName为null或空-------------" );
			return null;
		}	
    	
    	String tsid = getTsidByPiid( piid );
    	if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null或空-------------" );
			return null;
		}	
    	
    	String endProcessByTsid = endProcessByTsidInComment( tsid, comment, userName  );
    	if( StringUtils.isBlank( endProcessByTsid ) ) {
			logger.info( "---S--------闭环流程失败-------------" );
			return null;
		}else {
	    	/*
	    	 * 保存操作名称
	    	 */
			updateOprateNameS( piid, tsid, operateName );
			
			logger.info( "---S--------闭环流程成功-------------" );
			return endProcessByTsid;
		}
    	
    }
    
    /**   
     * @Title: endProcess   
     * @Description: 终止流程（tsid）
     * @return: void        
     */  
    @Override
    public String endProcessByTsid( String tsid ) {
//    	if( StringUtils.isBlank( tsid ) ) {
//			logger.info( "---S--------任务tsid为null-------------" );
//			return null;
//		}	
//    	
//		/*
//		 * 获取流程的end节点
//		 */
//    	ActivityImpl actiImpl;
//    	try {
//			actiImpl = getEndNode( tsid );
//			logger.info( "---S--------end节点为-------------" + actiImpl );
//			if( actiImpl == null ) {
//				return null;
//			}
//    	} catch (Exception e) {
//			logger.info( "--S---------获取流程的end节点失败----" );
//			return null;
//		}
//		
//		/*
//		 * 流程跳转
//		 */
//		try {
//			transferProcessNoVars( tsid, actiImpl.getId() );
//		} catch (Exception e) {
//			logger.info( "--S---------流程转向失败----" );
			return null;
//		}
//				
//		/*
//		 * 完成流程
//		 */
//		taskService.complete( tsid );
//		return "终止流程成功";		
	}
    
    /**   
     * @Title: endProcessInComment   
     * @Description:  终止流程（批准信息） 
     * @return: String        
     */  
    @Override
    public String endProcessByTsidInComment( String tsid, String comment, String userName ) {
    	if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null-------------" );
			return null;
		}	
    	
		/*
		 * 获取流程的end节点
		 */
    	ActivityImpl actiImpl;
    	try {
			actiImpl = getEndNode( tsid );
			logger.info( "---S--------end节点为-------------" + actiImpl );
			if( actiImpl == null ) {
//				return null;
			}
    	} catch (Exception e) {
			logger.info( "--S---------获取流程的end节点失败----" );
//			return null;
		}
    	
    	/*
    	 * 增加备注信息
    	 */
    	boolean b = saveCommentByTsid( tsid, comment);
    	if( b ) {
    		logger.info( "---执行回退上一个节点，保存备注信息成功---------" );
    	}else {
    		logger.info( "---执行回退上一个节点，保存备注信息失败---------" );
    	}
		
		/*
		 * 流程跳转
		 */
		try {
//			transferProcessNoVars( tsid, actiImpl.getId() );
			transferProcessNoVars( tsid, "end" , userName );
		} catch (Exception e) {
			logger.info( "--S---------流程转向失败----" );
			return null;
		}
				
		/*
		 * 完成流程
		 */
//		taskService.complete( tsid );
		return "终止流程成功";			
	}
    
    /**   
     * @Title: getHistoryNodesByPiid   
     * @Description: 根据流程piid，查询该流程的历史活动节点  
     * @return: List<HistoricActivityInstance>        
     */ 
    @Override
    public List<HistoricActivityInstance> getHistoryNodesByPiid( String piid ){
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		List<HistoricActivityInstance> lists = historyService
    				.createHistoricActivityInstanceQuery()
    				.processInstanceId( piid )
    				.finished()
    				.list();
    		logger.info( lists.toString() );
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
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		Task task = taskService.createTaskQuery().taskId( tsid ).singleResult();
    		String piid = task.getProcessInstanceId();
    		List<HistoricActivityInstance> lists = historyService
    				.createHistoricActivityInstanceQuery()
    				.processInstanceId( piid )
    				.orderByHistoricActivityInstanceStartTime()
    				.asc()
    				.list();
    		return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}   	
    }
    
    
    /**   
     * @Title: getHistoryTasksByTsid   
     * @Description:  根据当tsid，查询该流程的历史任务节点   
     * @return: List<HistoricTaskInstance>        
     */  
    public List<HistoricTaskInstance> getHistoryTasksByTsid( String tsid ){
    	if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		Task task = taskService.createTaskQuery().taskId( tsid ).singleResult();
    		String piid = task.getProcessInstanceId();
    		List<HistoricTaskInstance> lists = historyService
    				.createHistoricTaskInstanceQuery()
    				.processInstanceId( piid )
    				.orderByTaskCreateTime()
    				.asc()
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
	public HistoricTaskInstance getBeforeTasksByTsid ( String tsid ){
    	if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null-------------" );
			return null;
		}	
    	/*
    	 * 查询所有历史节点
    	 */
    	try {
    		List<HistoricTaskInstance> historyTasks = getHistoryTasksByTsid( tsid );
    		logger.info( "---S--------任务tsid的全部历史节点-------------" + historyTasks.toString() );
    		if( historyTasks.size() > 0 ) {
    			return historyTasks.get( historyTasks.size() - 2 );    			
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
	public HistoricActivityInstance getBeforeTasksByPiid ( String piid ){
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    
    	/*
    	 * 查询所有历史节点
    	 */
    	try {
    		List<HistoricActivityInstance> historyNodes = getHistoryNodesByPiid( piid );
    		if( historyNodes.size() > 0 ) {
    			return historyNodes.get( historyNodes.size() - 2 );    			
    		}else {
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**   
	 * @Title: backToBeforeNodeByPiid   
	 * @Description:  根据流程任务piid，回退流程当前节点的上一个节点   - 组任务
	 * @return: boolean        
	 */  
	public boolean backToBeforeNodeByPiidInGroup(String piid, Map<String,Object> map ) {
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null或空-------------" );
			return false;
		}	
		
		
		String tsid = getTsidByPiid( piid );
		if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null或空-------------" );
			return false;
		}
		logger.info( "---S--------任务tsid为-------------" + tsid );
		
		
		Object commentObj = map.get( "comment" );
    	if( commentObj == null ) {
    		logger.info( "-------comment参数不存在--------" );
    	}
    	String comment = map.get( "comment" ).toString();
    	logger.info( "-------流程回退节点备注信息--------" + comment );
	 	
    		
		try {			
			
			/*
			 * 查询上一个节点
			 */
			HistoricTaskInstance hisTask = getBeforeTasksByTsid ( tsid );
			logger.info( "---S--------上一个任务节点hisTasks为-------------" + hisTask );
			String beforeTaskId = hisTask.getTaskDefinitionKey();
			if( StringUtils.isBlank( beforeTaskId ) ) {
				logger.info( "---S--------上一个任务节点beforeTaskId为null或空-------------" ); 
				return false;
			}
			logger.info( "---S--------上一个任务节点beforeTaskId为-------------" + beforeTaskId ); 
			
			/*
    		 * 设置当前任务流程变量 - 定制 -后续改设计模式
    		 */
    		Object areaValueNow = taskService.getVariable( tsid, "area" );
    		String areaValueStr = "";
//    		Object areaValue = runtimeService.getVariable( piid, "area" );
    		if( areaValueNow != null ) {
    			areaValueStr = areaValueNow.toString();
    			logger.info( "---获取当前任务节点流程变量area成功:---------" + areaValueNow ); 
    		}else {
    			logger.info( "---获取当前任务节点流程变量area失败---------" );
    		}
			
			/*
			 * 回退设置当前的属地变量area - 此部分定制
			 */
			String beforeTsid = hisTask.getId();
			logger.info( "---S--------上一个任务节点beforeNode的tsid为-------------" + beforeTsid );
			if( !StringUtils.isBlank( beforeTsid ) ) {
				HistoricVariableInstance varibleInstance = historyService
						.createHistoricVariableInstanceQuery()
						.variableName("area")
						.taskId(beforeTsid)
						.singleResult();
				logger.info( "---S--------上一个任务节点varibleInstance的流程变量varibleInstance为-------------" + varibleInstance );
				String areaValue = "";
				if( varibleInstance != null) {
					Object beforeArea = varibleInstance.getValue();
					String name = varibleInstance.getVariableName();
					logger.info( "---S--------上一个任务节点name的流程变量name为-------------" + name );
					if( beforeArea != null ) {
						logger.info( "---S--------上一个任务节点beforeNode的流程变量beforeArea为-------------" + beforeArea.toString() );
						areaValue = beforeArea.toString();
						taskService.setVariable( tsid, "area", areaValue );
					}else {
						logger.info( "---S--------上一个任务节点beforeNode的流程变量beforeArea为null或空-------------");
					}		
				}else {
					logger.info( "---S--------上一个任务节点varibleInstance的流程变量varibleInstance为null-------------");
				}
					
			}
			
			//设置本节点的流程变量
    		if( StringUtils.isNotBlank( areaValueStr ) ) {
    			logger.info( "---获取当前任务节点流程变量area的areaValueStr:---------" + areaValueStr ); 
    			taskService.setVariableLocal( tsid, "area", areaValueStr );
    		}  	
    		
    		/*
    		 * 获取操作名称
    		 */
    		String  operateName = "";
    		Object operateNameObj = map.get( "operateName" );
    		logger.info( "---操作名称---------" + operateNameObj );
    		if(operateName != null ) operateName = operateNameObj.toString().trim();
    		map.remove( "operateName" );

    		/*
    		 * 增加备注信息
    		 */
    		boolean b = saveCommentByPiid( piid, comment );
    		if( b ) {
    			logger.info( "---回退上一个节点，保存备注信息成功---------" );
    		}else {
    			logger.info( "---回退上一个节点，保存备注信息失败---------" );
    		}
    		map.remove( "comment" );
			
			/*
			 * 跳转
			 */
    		transferProcessInVarsAndGroup( tsid, beforeTaskId, map );
    		
    		/*
	    	 * 保存操作名称
	    	 */
			updateOprateNameS( piid, tsid, operateName );
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
    
    /**   
	 * @Title: backToBeforeNode   
	 * @Description: 根据流程任务piid，回退流程当前节点的上一个节点   - 非组任务
	 * @return: boolean        
	 */
    @Override
    public boolean backToBeforeNodeByPiid( String piid, String comment ) {
//		if( StringUtils.isBlank( piid ) ) {
//			logger.info( "---S--------任务piid为null或空-------------" );
//			return false;
//		}	
//		
//		
//		String tsid = getTsidByPiid( piid );
//		if( StringUtils.isBlank( tsid ) ) {
//			logger.info( "---S--------任务tsid为null或空-------------" );
//			return false;
//		}
//		logger.info( "---S--------任务tsid为-------------" + tsid );
//		
//		try {			
//			
//			/*
//			 * 查询上一个节点
//			 */
//			HistoricActivityInstance beforeNode = getBeforeNodesByTsid ( tsid );
//			logger.info( "---S--------上一个任务节点beforeNode为-------------" + beforeNode );
//			String beforeNodeActid = beforeNode.getActivityId();
//			
//			/*
//			 * 回退设置前一个的属地变量area
//			 */
//			String beforeTsid = beforeNode.getTaskId();
//			logger.info( "---S--------上一个任务节点beforeNode的tsid为-------------" + beforeTsid );
//			if( StringUtils.isBlank( beforeTsid ) ) {
//				Object beforeArea = taskService.getVariableLocal( beforeTsid, "area" );
//				String areaValue = "";
//				if( beforeArea != null ) {
//					logger.info( "---S--------上一个任务节点beforeNode的流程变量beforeArea为-------------" + beforeArea.toString() );
//					areaValue = beforeArea.toString();
//					taskService.setVariable( tsid, "area", areaValue );
//				}else {
//					logger.info( "---S--------上一个任务节点beforeNode的流程变量beforeArea为null或空-------------");
//				}			
//			}
//			
//			
//			if( StringUtils.isBlank( beforeNodeActid ) ) {
//				logger.info( "---S--------上一个任务节点beforeNodeActid为null或空-------------" ); 
//				return false;
//			}
//			logger.info( "---S--------上一个任务节点beforeNodeActid为-------------" + beforeNodeActid ); 
//			
//			/*
//    		 * 增加备注信息
//    		 */
//    		boolean b = saveCommentByPiid( piid, comment );
//    		if( b ) {
//    			logger.info( "---执行回退上一个节点，保存备注信息成功---------" );
//    		}else {
//    			logger.info( "---执行回退上一个节点，保存备注信息失败---------" );
//    		}
//			
//			/*
//			 * 跳转
//			 */
//			transferProcessInVars( tsid, beforeNodeActid, null );
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
			return false;
//		}
	}
	
	/**   
	 * @Title: backToBeforeNode   
	 * @Description: 根据流程任务tsid，回退流程当前节点的上一个节点  
	 * @return: boolean        
	 */
    @Override
	public boolean backToBeforeNodeByTsid( String tsid ) {
//		if( StringUtils.isBlank( tsid ) ) {
//			logger.info( "---S--------任务tsid为null-------------" );
//			return false;
//		}	
//		
//		try {
//			/*
//			 * 查询上一个节点
//			 */
//			HistoricActivityInstance beforeNode = getBeforeTasksByTsid ( tsid );
//			String beforeNodeActid = beforeNode.getActivityId();
//			/*
//			 * 跳转
//			 */
//			transferProcessInVars( tsid, beforeNodeActid, null );
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
			return false;
//		}
	}
	
	/**   
	 * @Title: getAllHistoryInfo   
	 * @Description:  根据流程任务id，获取当前流程的历史节点信息
	 * @return: List<Map<String,Object>>        
	 */ 
    @Override
	public List<Map<String,Object>> getHisInfosByTsid( String tsid ){
		ArrayList<Map<String, Object>> allHistoryInfos = new ArrayList<Map<String,Object>>();
		if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null-------------" );
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
	 * @Title: getAllHistoryInfo   
	 * @Description:  根据流程piid，获取当前流程的历史节点信息
	 * @return: List<Map<String,Object>>        
	 */ 
    @Override
	public List<Map<String,Object>> getHisActNodesByPiid( String piid ){
    	logger.info( "---S--------根据流程piid，获取当前流程的历史节点信息-------------" );
		ArrayList<Map<String, Object>> allHistoryInfos = new ArrayList<Map<String,Object>>();
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------piid为null-------------" );
			return null;
		}
		
		String tsid = getTsidByPiid( piid ); 
		if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------tsid为null-------------" );
			return null;
		}
		/*
		 * 获取历史节点
		 */
		try {
			List<HistoricActivityInstance> historyNodes = getHistoryNodesByPiid( piid );
			logger.info(historyNodes.toString());
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
				tempMap.put( "nodeExecutor", h.getAssignee() );
				tempMap.put( "nodeEndTime", h.getEndTime() );
				allHistoryInfos.add( tempMap );
			}
			return allHistoryInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    /**   
     * @Title: getHisTaskNodeInfosByPiid   
     * @Description:  根据流程piid，获取当前流程的任务节点信息 
     * @return: List<Map<String,Object>>        
     */  
    @Override
    public List<Map<String,Object>> getHisTaskNodeInfosByPiid( String piid ){
    	logger.info( "---S--------根据流程piid，获取当前流程的任务节点信息-------------" );
		ArrayList<Map<String, Object>> allHistoryInfos = new ArrayList<Map<String,Object>>();
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------piid为null-------------" );
			return null;
		}
		
		String tsid = getTsidByPiid( piid ); 
		if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------tsid为null-------------" );
			return null;
		}
		/*
		 * 获取历史节点
		 */
		try {
			List<HistoricTaskInstance> historyNodes = getHisTaskNodesByPiid( piid );
			logger.info(historyNodes.toString());
			for( HistoricTaskInstance h : historyNodes ) {
				HashMap<String, Object> tempMap = new HashMap<String, Object>();
				String taskid = h.getId();
				List<Comment> comments = taskService.getTaskComments( taskid );
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
				tempMap.put( "nodeId", h.getId() );
				tempMap.put( "nodeName", h.getName() );
				tempMap.put( "nodeExecutor", h.getAssignee() );
				tempMap.put( "nodeEndTime", h.getEndTime() );
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
	public List<TodoTask> getAllTasksByUsername( String userName ){
		List<Task> allTasks = new ArrayList<Task>();
		List<TodoTask> todoTasks = new ArrayList<TodoTask>();
		try {
			List<Task> personalTasks = taskService.createTaskQuery()
					.taskAssignee( userName )
					.orderByTaskCreateTime()
					.desc()
					.list();	
			logger.info( "-------personalTasks--------" + personalTasks );
			if( personalTasks != null && personalTasks.size() > 0 ) {
				logger.info( personalTasks.toString() );
				allTasks.addAll( personalTasks );
			}
			
			List<Task> candidatetasks = taskService.createTaskQuery()
					.taskCandidateUser( userName )
					.orderByTaskCreateTime()
					.desc()
					.list();
			logger.info( "-------candidatetasks--------" + personalTasks );
			if( candidatetasks != null && candidatetasks.size() > 0 ) {
				logger.info( candidatetasks.toString() );
				allTasks.addAll( candidatetasks );
			}	
			if( allTasks != null && allTasks.size() > 0 ) {
				logger.info( allTasks.toString() );
				for( Task t : allTasks ) {
					TodoTask todoTask = new TodoTask();					
					todoTask.setTsid( t.getId() );
					todoTask.setDfid( t.getProcessDefinitionId() );
					todoTask.setPiid( t.getProcessInstanceId() );
					todoTask.setName( t.getName() );
					todoTask.setCurrentnode( t.getName() );					
		
					//获取问题上报信息和上报人
					List<HistoricTaskInstance> hisTasks = getHisTaskNodesByTsid( t.getId() );
					if(  hisTasks != null &&  hisTasks.size() > 0 &&hisTasks.get(0) != null ) {
						//上报信息
						String firstTsid = hisTasks.get(0).getId().toString() ;
						logger.info( "---------问题上报节点id：------------" + firstTsid );
						List<Comment> taskComments = taskService.getTaskComments( firstTsid );
						logger.info( "---------问题上报信息：------------" + taskComments );
						if( taskComments != null && taskComments.size() > 0 ) {
							Comment c = taskComments.get(0);
							if( c != null && StringUtils.isNotBlank( c.getFullMessage() )) {
								todoTask.setDescrible( c.getFullMessage() );
								logger.info( "---------获取备注信息成功------------" + c.getFullMessage() );
							}else {
								logger.info( "---------获取备注信息失败------------" + c.getFullMessage() );
							}
						}
						
						//上报人
						HistoricTaskInstance firstTisTaskNode = historyService.createHistoricTaskInstanceQuery().taskId(firstTsid).singleResult();
						logger.info( "---------问题上报节点firstTisTaskNode ：------------" + firstTisTaskNode );
						if( firstTisTaskNode != null ) {
							String reportor = firstTisTaskNode.getAssignee();
							todoTask.setReportperson( reportor );
							logger.info( "---------问题上报人 ：------------" + reportor );
							
							//上报时间
							Object reportTimeObj = firstTisTaskNode.getCreateTime();
							logger.info( "---------问题上报时间 ：------------" + reportTimeObj );
							if( reportTimeObj != null ) {
								Date date = (Date) reportTimeObj;
//								String reportTime = reportTimeObj.toString();
								SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
								String reportTime = sdf.format(date);
								todoTask.setReporttime( reportTime );
								logger.info( "---------问题上报时间 获取成功：------------" + reportTime );
							}	
						}else {
							logger.info( "---------问题上报节点Task为null------------" );
						}
										
					}
				
					//添加属地变量
					Object areaVar = taskService.getVariable( t.getId(), "area" );
					if( areaVar != null  ) {
						logger.info( "---------属地单位：------------" + areaVar.toString() );
						todoTask.setArea( areaVar.toString() );
					}
					
					//添加超期记录
					LocalDate today = LocalDate.now();
					java.util.Date date = t.getCreateTime();
				    Instant instant = date.toInstant();
				    ZoneId zone = ZoneId.systemDefault();
				    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
				    LocalDate createDay = localDateTime.toLocalDate();
					long p2 = ChronoUnit.DAYS.between(createDay, today);
					logger.info( "---------待办任务的时间间隔，检验超期------------" + p2 );
					todoTask.setTip( p2 > 2 ? "超时" : "未超时" );				
					todoTasks.add( todoTask );					
				}
				logger.info( todoTasks.toString() );
				return todoTasks;
			}else {
				return null;
			}		
		} catch (Exception e) {
			e.printStackTrace();		
			return todoTasks;
		}				
	}
    
    /**   
     * @Title: getHistoryNodesByPiid   
     * @Description: 根据流程piid，查询该流程的历史任务节点  
     * @return: List<HistoricActivityInstance>        
     */ 
    @Override
    public List<HistoricTaskInstance> getHisTaskNodesByPiid( String piid ){
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		List<HistoricTaskInstance> lists = historyService
    				.createHistoricTaskInstanceQuery()
    				.processInstanceId( piid )
    				.orderByTaskCreateTime()
    				.finished()
    				.asc()
    				.list();
    		if( lists != null && lists.size()>0 ) {
    			logger.info( lists.toString() );
    		}else {
    			logger.info( "---------根据流程piid，查询该流程的历史任务节点  为null或空-------------" );
    		}
    		
    		return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}   	
    }
    
    /**   
     * @Title: getHistoryNodesByPiid   
     * @Description: 根据流程tsid，查询该流程的历史任务节点  
     * @return: List<HistoricActivityInstance>        
     */ 
    @Override
    public List<HistoricTaskInstance> getHisTaskNodesByTsid( String tsid ){
    	if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------tsid为null-------------" );
			return null;
		}	
    	String piid = getPiidByTsid( tsid );
    	if( StringUtils.isBlank( piid )) {
    		logger.info( "---S--------piid为null-------------" );
			return null;
    	}
    	
    	try {
    		List<HistoricTaskInstance> lists = historyService
    				.createHistoricTaskInstanceQuery()
    				.processInstanceId( piid )
    				.orderByTaskCreateTime()
    				.finished()
    				.asc()
    				.list();
    		if( lists != null && lists.size()>0 ) {
    			logger.info( lists.toString() );
    		}else {
    			logger.info( "---------根据流程tsid，查询该流程的历史任务节点  为null或空-------------" );
    		}
    		return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}   	
    }
    
    /**   
     * @Title: getTaskCandidate   
     * @Description: 根据当前任务id，查询当前任务潜在的所有执行人  
     * @return: Set<User>        
     */  
    public Set<User> getTaskCandidateByPiid( String piid ) {
    	logger.info( "---------根据当前任务id，查询当前任务潜在的所有执行人  -------------" );
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------piid为null-------------" );
			return null;
		}	
    	String tsid = getTsidByPiid( piid );
    	if( StringUtils.isBlank( tsid )) {
    		logger.info( "---S--------tsid为null-------------" );
			return null;
    	}
    	
		Set<User> users = new HashSet<User>();
		List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask( tsid );
		if (identityLinkList != null && identityLinkList.size() > 0) {
			for (Iterator<IdentityLink> iterator = identityLinkList.iterator(); iterator
					.hasNext();) {
				IdentityLink identityLink = (IdentityLink) iterator.next();
				if ( identityLink.getUserId() != null ) {
					String userName = identityLink.getUserId();	
					if (userName != null) {
						User user = new UserEntity();
						user.setId( userName );
						users.add( user );						
					}else {
						logger.info( "---S--------非组任务的潜在执行人不存在-------------" );
					}
				}	
				if( users != null && users.size() > 0 ) {
					users.forEach( s->logger.info( "---------非组任务的潜在执行人  -------------" + s.getId() ) );
				}				
				
				if ( identityLink.getGroupId() != null ) {
					// 根据组获得对应人员
					List<User> userList = identityService.createUserQuery()
							.memberOfGroup(identityLink.getGroupId()).list();
					
					if (userList != null && userList.size() > 0)
						users.addAll(userList);
				}else {
					users.forEach( s->logger.info( "---------组任务的潜在执行人 不存在 -------------" ) );
				}
				if( users != null && users.size() > 0 ) {
					users.forEach( s->logger.info( "---------组任务的潜在执行人  -------------" + s.getId() ) );
				}
			}
 
		}else {
			logger.info( "---------IdentityLink对象为null或空  -------------" );
		}
		return users;
	}

    /**   
     * @Title: findAllHisActsBypiid   
     * @Description: 根据任务piid,查询当前流程实例的所有任务节点（包括完成和未完成任务的候选执行人,不包括分支节点）    
     * @return: List<HistoryAct>        
     */  
    @Override
    public List<HistoryAct> findAllHisActsBypiid( String piid ){
    	logger.info( "--S-------根据当前任务id，查询当前任务潜在的所有执行人  -------------" );
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------piid为null-------------" );
			return null;
		}
    	
    	List<HistoryAct> hisActs = new ArrayList<HistoryAct>();
    	try {
    		//查询所有节点
    		List<HistoryAct> oldHisActs = acitivityHistoryActS.findAllHisActsBypiid( piid );
    		if( oldHisActs == null || oldHisActs.size() < 1 ) {
    			logger.info( "---S--------全部act节点为null或空-------------" );
    			return null;
    		}
    		logger.info( "---S--------全部act节点为-------------" + oldHisActs.toString() );
    		
    		//去掉分支节点
    		String flag = "1";//判断最新节点有无执行人
    		int i= 0;
    		int s = oldHisActs.size();
    		int s1 = oldHisActs.size();
    		for( HistoryAct h : oldHisActs ) {
    			i++;
    			if( i == s  ) {
    				if( h.getEND_TIME_() == null ) {
    					flag = null;
    				}
    			}
    			String act_TYPE_ = h.getACT_TYPE_();
    			if(  act_TYPE_ != null  ) {
    				if( act_TYPE_.contains( "exclusiveGateway" ) || act_TYPE_.contains( "startEvent")  || act_TYPE_.contains( "endEvent") ) {
    					s1 --;
    					continue;
    				}		
    			}
    			hisActs.add( h );   			
    		}
    		if( hisActs == null || hisActs.size() < 1 ) {
    			logger.info( "---S--------全部task节点为null或空-------------" );
    			return null;
    		}
    		logger.info( "---S--------全部task节点为-------------" + hisActs.toString() );
    		
    		//查看问题是否完成，若未完成，则查找最后候选执行人
    		String candidateStr = null;
    		if( flag == null ) {//流程是未完成的状态
    			List<cn.soa.entity.activity.IdentityLink> candidates = acitivityIdentityS.findCandidateByPiid( piid );
    			for( cn.soa.entity.activity.IdentityLink id : candidates ) {
    				candidateStr = candidateStr == null ? id.getUSER_ID_():candidateStr + "," + id.getUSER_ID_();
    			}
    			logger.info( "---S--------最后一个节点的执行人-------------" + candidateStr );
    			if( StringUtils.isNotBlank( candidateStr  ) 
    					&& StringUtils.isBlank( hisActs.get( s1 - 1 ).getASSIGNEE_() )) {//在问题评估时回退到问题上报，这个回退后，第二个问题上报节点的tsid在候选人表中是没有的，在节点历史表中默认有人，这个人就是第一次问题上报的人
    				hisActs.get( s1 - 1 ).setASSIGNEE_( candidateStr );
    			}	
    			return hisActs;
    		}
    		
    		return hisActs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**   
     * @Title: updateOprateNameS   
     * @Description: 保存操作名称   
     * @return: void        
     */  
    public void updateOprateNameS( String piid, String tsid, String operateName) {
    	 int num = acitivityHistoryActS.updateOprateNameS( piid, tsid, operateName );
		String loginfo = num > 0?"成功":"失败";
		logger.info( "------添加节点操作名称------" + loginfo );
    }
   
    
    
    /**   
     * @Title: main   
     * @Description:   测试类
     * @return: void        
     */  
    public static void main(String[] args) {
    	LocalDate today = LocalDate.now();
		java.util.Date date = new Date(System.currentTimeMillis()-60*60*24*3*1000);
	    Instant instant = date.toInstant();
	    ZoneId zone = ZoneId.systemDefault();
	    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
	    LocalDate createDay = localDateTime.toLocalDate();
		long p2 = ChronoUnit.DAYS.between(createDay, today);
		System.out.println(p2);
		String s = p2 > 2 ? "超时" : "未超时" ;
		System.out.println(s);
	}
}
