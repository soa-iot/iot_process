package cn.zg.mapper;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.dao.ProblemReportphoMapper;
import cn.soa.entity.ProblemReportpho;
import cn.soa.service.inter.ReportPhoSI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ProblemReportphoMapperTest {
	
	@Autowired
	private ProblemReportphoMapper problemReportphoMapper;
	
	@Autowired
	private ReportPhoSI phos;

	@Test
	public void findByPiidTest() {
		  List<ProblemReportpho> problemReportphos = problemReportphoMapper.findByPiid("13",null);
		System.err.println(problemReportphos);
	}
	
	@Test
	public void updateTempPho() {
		String tProblemRepId = UUID.randomUUID().toString();
		String piid = "12323213123";
		phos.updateTempPho(tProblemRepId, "15BC574DCB78432E882DDDF3D15ADAA6", piid, null);
	}
}
