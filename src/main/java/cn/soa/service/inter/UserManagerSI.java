package cn.soa.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserManagerSI {

	/**   
	 * @Title: findUsersByRoleId   
	 * @Description:   根据角色id查询该角色下所有人员 
	 * @return: List<String>        
	 */  
	List<String> findUsersByRoleId(String roleId);

	/**   
	 * @Title: findUsersByRoleName   
	 * @Description:   根据角色名称查询该角色下所有人员 
	 * @return: List<String>        
	 */  
	List<String> findUsersByRoleName(String roleName);

	/**   
	 * @Title: findUserPostByNum   
	 * @Description: 根据用户的id查询用户的岗位    
	 * @return: String        
	 */  
	String findUserPostByNum(String usernum);

	/**   
	 * @Title: findUserPostByName   
	 * @Description: 根据用户的名称查询用户的岗位      
	 * @return: String        
	 */  
	String findUserPostByName(String username);
	
}
