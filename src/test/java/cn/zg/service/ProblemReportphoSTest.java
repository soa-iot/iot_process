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
import cn.soa.entity.ProblemReportpho;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.ProblemReportphoSI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ProblemReportphoSTest {

	@Autowired
	private ProblemReportphoSI problemReportphoSI;
	
	@Test
	public void getByPiidTest() {
		 List<ProblemReportpho> problemReportphos = problemReportphoSI.getByPiid("13",null);
		System.err.println(problemReportphos);
	}
}
