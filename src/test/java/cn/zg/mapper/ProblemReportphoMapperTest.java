package cn.zg.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.dao.ProblemReportphoMapper;
import cn.soa.entity.ProblemReportpho;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ProblemReportphoMapperTest {
	
	@Autowired
	private ProblemReportphoMapper problemReportphoMapper;

	@Test
	public void findByPiidTest() {
		  List<ProblemReportpho> problemReportphos = problemReportphoMapper.findByPiid("13");
		System.err.println(problemReportphos);
	}
}
