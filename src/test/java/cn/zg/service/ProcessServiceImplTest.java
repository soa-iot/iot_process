package cn.zg.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ProcessServiceImplTest {
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
    private TaskService taskService;
	
	//@Test
	public void processInstanceTest() {
		String id = "20";
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceId( id )
				.singleResult();
		if( processInstance != null ) {
			System.out.println( processInstance.toString() );
		} 
		
	}
	
	@Test
	public void getTaskTest() {
		String piid = "57505";
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceId( piid ).singleResult();
		Task task = taskService.createTaskQuery()
				.processInstanceId( piid )
				.singleResult();
		String acid = pi.getActivityId();
		String parentId = pi.getParentId();
		System.out.println( acid );
		System.out.println( parentId );
		System.out.println( task.getId() );
	}
}
