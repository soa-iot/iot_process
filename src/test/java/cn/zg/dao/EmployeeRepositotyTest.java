package cn.zg.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.zg.InternetOfThingsApplication;
import cn.zg.dao.inter.EmployeeRepository;
import cn.zg.dao.inter.PurificationSchemeDao;
import cn.zg.entity.daoEntity.Emploee;
import cn.zg.entity.daoEntity.Schemeposition;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { InternetOfThingsApplication.class })
@WebAppConfiguration
public class EmployeeRepositotyTest {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	//@Test
	public void findByEmpNameTest() {
		String empName = "小红";
		List<Emploee> e =  employeeRepository.findByEmpName( empName );
		System.out.println( e.toString() );
	}
	
	//@Test
	public void findByRoleNameTest() {
		String roleNameLike = "净化%班班长";
		List<Emploee> e =  employeeRepository.findByRoleName(roleNameLike);
		System.out.println( e.toString() );
	}	
	
	@Test
	public void findByRoleNameEquTest() {
		String roleName = "净化技干";
		List<Emploee> lists = employeeRepository.findByRoleNameEqu( roleName  );
		System.out.println( lists );
	}
	
}
