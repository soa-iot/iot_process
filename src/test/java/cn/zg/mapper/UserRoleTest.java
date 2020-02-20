package cn.zg.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.dao.UserRoleMapper;
import cn.soa.entity.RoleVO;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class UserRoleTest {
	
	@Autowired
	private UserRoleMapper mapper;

	@Test
	public void findRepair() {
		List<RoleVO> result = mapper.findRepair();
		System.err.println(result);
	}
	
	@Test
	public void findUserByOrgid() {
		List<String> result = mapper.findUserByOrgid("297793FAFF494E378C22C2785DF5FCDF");
		System.err.println(result);
		String rest = mapper.findOrgNameByOrgid("297793FAFF494E378C22C2785DF5FCDF");
		System.err.println(rest);
	}
}
