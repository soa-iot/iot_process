package cn.soa.utils;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivityUtils {
	private static Logger logger = LoggerFactory.getLogger( ActivityUtils.class );
	
	@Autowired
    private RepositoryService repositoryService;
	
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private HistoryService historyService;
    
    @Autowired
    private TaskService taskService;
    
    
    /**   
     * @Title: deployMechatronicsProcess   
     * @Description:  部署流程 
     * @return: Deployment        
     */  
    public Deployment deployMechatronicsProcess(){
		//获取资源相对路径		
		//读取资源作为一个输入流
		Deployment deployment = repositoryService.createDeployment()
				 .name( "净化厂机电仪检维修流程")
				 .addClasspathResource("process/repairProcess.bpmn")
				 .addClasspathResource("process/repairProcess.png")
				 .deploy();
		return deployment;
	}
    
    /**   
     * @Title: getMechatronicsProcess   
     * @Description: 根据流程部署对象，获取流程定义对象  
     * @return: ProcessDefinition        
     */  
    public ProcessDefinition getMechatronicsProcess( Deployment deployment ) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId( deployment.getId() )
				.singleResult();
		return processDefinition;
	}
  
}
