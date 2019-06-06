package cn.zg.mapper;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.dao.ProblemInfoMapper;
import cn.soa.entity.ProblemInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ProblemInfoMapperTest {
	
	@Autowired
	private ProblemInfoMapper problemInfoMapper;

	@Test
	public void findByPiidTest() {
		ProblemInfo estimate = problemInfoMapper.findByPiid("ADAA80DB601C4470BE8BB224705F5F9C");
		System.err.println(estimate);
	}
	
	@Test
	public void updateProblemDescribeByPiidTest() {
		Integer row = problemInfoMapper.updateProblemDescribeByPiid("ADAA80DB601C4470BE8BB224705F5F9C", "test1");
		System.err.println(row);
	}
	
	@Test
	public void updateEstiByPiidTest() {
		
		ProblemInfo info = new ProblemInfo();
		info.setRemark("123456");
		info.setTicketNo("ticketNo");
		info.setPiid("ADAA80DB601C4470BE8BB224705F5F9C");
		
		info.setRectificationperiod(null);
		Integer row = problemInfoMapper.updateEstiByPiid(info);
		System.err.println(row);
	}
}
