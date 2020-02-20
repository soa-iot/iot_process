package cn.zg.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.UserOrganization;
import cn.soa.service.inter.ProblemInfoSI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ProblemInfoSTest {

	@Autowired
	private ProblemInfoSI problemInfoSI;
	
	@Test
	public void getByPiidTest() {
		ProblemInfo estimate = problemInfoSI.getByPiid("ADAA80DB601C4470BE8BB224705F5F9C");
		System.err.println(estimate);
	}
	
	@Test
	public void getDeptByProblemtypeTest() {
		 List<UserOrganization> estimate = problemInfoSI.getDeptByProblemtype("超级管理员");
		System.err.println(estimate);
	}
}
