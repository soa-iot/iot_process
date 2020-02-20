package cn.zg.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.soa.IotprocessApplication;
import cn.soa.dao.ProblemTypeAreaMapper;
import cn.soa.dao.UserRoleMapper;
import cn.soa.entity.ProblemTypeArea;
import cn.soa.entity.RoleVO;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IotprocessApplication.class })
@WebAppConfiguration
public class ProblemTypeAreaTest {
	
	@Autowired
	private ProblemTypeAreaMapper mapper;

	@Test
	public void findAll() {
		List<ProblemTypeArea> result = mapper.findAll();
		
		System.err.println(result);
	}
}
