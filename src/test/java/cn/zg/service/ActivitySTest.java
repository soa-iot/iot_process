package cn.zg.service;

import java.util.List;

import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.entity.UserOrganization;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.UserManagerSI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ActivitySTest {
	@Autowired
	private ActivitySI asi;
	
	@Autowired
	private UserManagerSI userManagerS;
	
	@Autowired
	private ProblemInfoSI problemInfoS;
	
//	@Test
	public void deployProcess() {
		String name = "净化厂机电仪检维修流程";
		String xmlUrl = "process/repairProcess.bpmn";
		String pngUrl = "process/repairProcess.png";
		Deployment deployObj = asi.deployProcess( name, xmlUrl, pngUrl );
		System.out.println(deployObj);
	}
	
	//@Test
	public void findUserByArea() {
		String userName = "HSE办公室";
		List<UserOrganization> findUserByArea = userManagerS.findUserByArea(userName);
		System.out.println(findUserByArea);
	}
	
	@Test
	public void modifyProblemState() {
		boolean b = problemInfoS.modifyProblemState();
		System.out.println(b);
	}
}
