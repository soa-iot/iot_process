package cn.zg.service;

import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.service.inter.ActivitySI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ActivitySTest {
	@Autowired
	private ActivitySI asi;
	
	@Test
	public void deployProcess() {
		String name = "净化二厂闭环流程";
		String xmlUrl = "process/repairProcess.bpmn";
		String pngUrl = "process/repairProcess.png";
		Deployment deployObj = asi.deployProcess( name, xmlUrl, pngUrl );
		System.out.println(deployObj);
	}
}
