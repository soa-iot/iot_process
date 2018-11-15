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
import cn.zg.dao.impl.OrganTreeDao;
import cn.zg.dao.inter.OrganizationRepository;
import cn.zg.dao.inter.RoleRepository;
import cn.zg.entity.daoEntity.Organization;
import cn.zg.entity.daoEntity.Role;
import cn.zg.entity.daoEntity.SimpleTreeData;
import cn.zg.entity.daoEntity.executorTreeNode;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { InternetOfThingsApplication.class })
@WebAppConfiguration
public class OrganTreeTest {
	
	@Autowired
	OrganTreeDao organTreeDao;
	
	//@Test
	public void  find1Test(){
		List<SimpleTreeData> lists = organTreeDao.findByParentID("0");
		System.out.println( lists );
	}
	
	@Test
	public void  find2Test(){
		String pidStr = 
			"'33327aab-e232-4af1-93d6-13fc60848aaa','gla0a176-af58-4448-bea1-6ca92d28bce6'";
		List<SimpleTreeData> lists = organTreeDao.findByPids(pidStr);
		System.out.println( lists );
	}
}
