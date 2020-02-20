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
import cn.soa.dao.UserOrganizationTreeMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.UserOrganization;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class UserOrganizationTreeMapperTest {
	
	@Autowired
	private UserOrganizationTreeMapper userOrganizationTreeMapper;

	@Test
	public void findUserOrganizationByNameTest() {
		 List<UserOrganization> userOrganizations = userOrganizationTreeMapper.findUserOrganizationByName("净化工段");
		System.err.println(userOrganizations);
	}
	
	@Test
	public void findUserOrganizationByParentIdTest() {
		 List<UserOrganization> userOrganizations = userOrganizationTreeMapper.findUserOrganizationByParentId("jhgd");
		System.err.println(userOrganizations);
	}
	
	@Test
	public void findUserOrganizationByOrganTest() {
		 List<UserOrganization> userOrganizations = userOrganizationTreeMapper.findUserOrganizationByOrgan("检维修变电站", "li");
		System.err.println(userOrganizations);
	}
}
