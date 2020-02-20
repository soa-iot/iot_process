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
import cn.soa.entity.UserOrganization;

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
		
		ProblemInfo problemInfo = new ProblemInfo();
		problemInfo.setPiid("160130");
		problemInfo.setProblemdescribe("160130");
		Integer row = problemInfoMapper.updateProblemDescribeByPiid(problemInfo);
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
	
	@Test
	public void findApplyPeople() {
		String parentId = problemInfoMapper.findApplyPeople("周广浩");
		System.out.println("parentId: " + parentId);
	}
	
	@Test
	public void findDeptByProblemtypeTest() {
		
		List<UserOrganization> row = problemInfoMapper.findDeptByProblemtype("超级管理员");
		System.err.println(row);

	}
	
	@Test
	public void deleteByReportid() {
		Integer result = problemInfoMapper.deleteByRepid("C00287BE52C1452CA8C0B7D90C9AD749");
		System.err.println("result: "+result);
	}
}
