package cn.zg.service;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.soa.IotprocessApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
public class ActivitiTest {
	
	@Autowired
	public ProcessEngine processEngine;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Test
	public void deployProcess() {
		Deployment deploy = processEngine.getRepositoryService().createDeployment()
					 .name("净化二厂闭环流程")
					 .addClasspathResource("process/repairProcess.bpmn")
					 .addClasspathResource("process/repairProcess.png")
					 .deploy();
		
		System.out.println("---------------------");
		System.out.println("ID: "+deploy.getId());
	}
	
	/**
	 * 删除流程
	 */
	@Test
	public void closeProcess() {
		runtimeService.deleteProcessInstance("80001", "过时");
		runtimeService.deleteProcessInstance("80019", "过时");
		runtimeService.deleteProcessInstance("110001", "过时");


		System.out.println("--------删除成功");
	}
}
