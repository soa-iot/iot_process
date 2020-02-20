package cn.soa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import cn.soa.dao.UserOrganizationTreeMapper;
import cn.soa.dao.UserRoleMapper;
import cn.soa.entity.LayuiTree;
import cn.soa.entity.ResultJson;
import cn.soa.entity.Role;
import cn.soa.entity.RoleVO;
import cn.soa.entity.UserOrganization;
import cn.soa.entity.UserRole;
import cn.soa.service.inter.UserManagerSI;

@Service
@PropertySource({"classpath:process/variables.properties"})
public class UserManagerS implements UserManagerSI {
	private static Logger logger = LoggerFactory.getLogger( UserManagerS.class );
	
	@Value("${userManager.server.ip}")
	private String ip;
	
	@Value("${userManager.server.port}")
	private String port;
	
	@Autowired
	private RestTemplate restTemplate; 
	
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	@Autowired
	private UserOrganizationTreeMapper userOrganizationTreeMapper;
	
	
	public void findOrganByUsernameS( String usernum, String url, int level ) {
		
	}
	
	public void findOrganByUsernumS( String usernum, String url, int level ) {
		
	}
	
	public String findFirstOrganByUsernumC( String usernum, String url ) {
		HashMap<String, String> map = new HashMap<>();
		ResponseEntity<UserOrganization> organ = restTemplate.getForEntity( url, UserOrganization.class, map);
		UserOrganization u = organ.getBody();
		return u.getName();
	}
	
	/**   
	 * @Title: findUsersByRoleId   
	 * @Description: 根据角色id查询该角色下所有人员 
	 * @return: List<String>        
	 */  
	@Override
	public List<String> findUsersByRoleId( String roleId ){
		List<String> userList = new ArrayList<String>();
		String url = "http://" + ip + ":" + port + "/iot_usermanager/role/id/users";
		HashMap<String, String> map = new HashMap<>();
		map.put( "roleId", roleId );		
		ResponseEntity<ResultJson> organ = restTemplate.getForEntity( url, ResultJson.class, map);
		ResultJson r = organ.getBody();
		if( r.getState() == 1 || r.getData() != null ) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<UserOrganization> users = (List<UserOrganization>) r.getData(); 
		for( UserOrganization u : users ) {
			userList.add( u.getUsernum() );
		}
		if( userList.size() > 0 ) logger.debug( userList.toString() );
		return userList;
	}
	
	
	/**   
	 * @Title: findUsersByRoleId   
	 * @Description: 根据角色名称查询该角色下所有人员 
	 * @return: List<String>        
	 */ 
	@Override
	public List<String> findUsersByRoleName( String roleName ){
		List<String> userList = new ArrayList<String>();
		String url = "http://" + ip + ":" + port + "/iot_usermanager/role/name/users";
		HashMap<String, String> map = new HashMap<>();
		map.put( "roleName", roleName );		
		ResponseEntity<ResultJson> organ = restTemplate.getForEntity( url, ResultJson.class, map);
		ResultJson r = organ.getBody();
		if( r.getState() == 1 || r.getData() != null ) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<UserOrganization> users = (List<UserOrganization>) r.getData(); 
		for( UserOrganization u : users ) {
			userList.add( u.getUsernum() );
		}
		if( userList.size() > 0 ) logger.debug( userList.toString() );
		return userList;
	}
	
	/**   
	 * @Title: findUserPost   
	 * @Description: 根据用户的id查询用户的岗位  
	 * @return: String        
	 */  
	@Override
	public String findUserPostByNum( String usernum ) {
		HashMap<String, String> map = new HashMap<>();
		String url = "http://" + ip + ":" + port + "/iot_usermanager/user/users/" + usernum.trim();
		ResponseEntity<ResultJson> organ = restTemplate.getForEntity( url, ResultJson.class, map);
		ResultJson r = organ.getBody();
		if( r.getState() == 1 || r.getData() != null ) {
			return null;
		}		
		UserOrganization user = (UserOrganization) r.getData(); 
		logger.debug( user.toString() );		
		return user.getRemark2();
	}
	
	/**   
	 * @Title: findUserPostByName   
	 * @Description: 根据用户的名称查询用户的岗位    
	 * @return: String        
	 */  
	@Override
	public String findUserPostByName( String username ) {
		HashMap<String, String> map = new HashMap<>();
		String url = "http://" + ip + ":" + port + "/iot_usermanager/role/name/users";
		ResponseEntity<ResultJson> organ = restTemplate.getForEntity( url, ResultJson.class, map);
		ResultJson r = organ.getBody();
		if( r.getState() == 1 || r.getData() != null ) {
			return null;
		}		
		UserOrganization user = (UserOrganization) r.getData(); 
		logger.debug( user.toString() );		
		return user.getRemark2();
	}
	
	
	/**   
	 * @Title: findUserByArea   
	 * @Description:  根据一个名称（String，一个配置表中的名称） ，获取对应的角色下所有用户
	 * @return: List<UserOrganization>        
	 */  
	@Override
	public List<UserOrganization> findUserByArea( String area ){
		HashMap<String, String> map = new HashMap<>();
		map.put( "roleName", area );
		String url = "http://" + ip + ":" + port + "/iot_usermanager/user/roleName?roleName={roleName}";
		ResponseEntity<ResultJson> organ = restTemplate.getForEntity( url, ResultJson.class, map);
		ResultJson r = organ.getBody();
		if( r.getState() == 1 || r.getData() == null ) {
			logger.debug( "-------获取人员数据失败--------" );
			return null;
		}		
		List<UserOrganization> users =  (List<UserOrganization>) r.getData();
		logger.debug( users.toString() );		
		return users;
	}
	
	/**
	 * 查找仪表、电气、机械这三个班长
	 * @param  无
	 * @return 用户列表树形结构
	 */
	@Override
	public List<LayuiTree> findRepair() {
		
		List<RoleVO> result = userRoleMapper.findRepair();
		if(result == null) {
			logger.debug("-------获取仪表、电气、机械这三个班长数据失败--------");
			return null;
		}
		
		//将查找结果填充到layui树形结构实体类中
		List<LayuiTree> treeList = new ArrayList<>();
		for(RoleVO role : result) {
			LayuiTree tree = new LayuiTree();
			tree.setLabel(role.getName());
			tree.setId(role.getRolID());
			List<LayuiTree> subTree = new ArrayList<>();
			for(UserOrganization user : role.getUsers()) {
				subTree.add(new LayuiTree(user.getName(), user.getUsernum()));
			}
			tree.setChildren(subTree);
			treeList.add(tree);
		}
		return treeList;
	}
	
	/**
	 * 查找所在组下的用户列表
	 * @param orgID  组织标号
	 * @return 用户列表树形结构
	 */
	@Override
	public List<LayuiTree> findUserByDept(String dept) {
		List<UserOrganization> result = userOrganizationTreeMapper.findUserOrganizationByName(dept);
		
		if(result == null) {
			logger.debug("-------获取所在组下的用户列表数据失败--------");
			return null;
		}
		//将查找结果填充到layui树形结构实体类中
		List<LayuiTree> treeList = new ArrayList<>();
		LayuiTree tree = new LayuiTree();
		tree.setLabel(dept);
		List<LayuiTree> subTree = new ArrayList<>();
		for(UserOrganization user : result) {
			subTree.add(new LayuiTree(user.getName()));
		}
		tree.setChildren(subTree);
		treeList.add(tree);
		
		return treeList;
	}
	
	/**
	 * 根据用户名查找用户编号
	 * @param  name 用户名
	 * @return 用户编号
	 */
	@Override
	public String getUsernumByName(String name) {
		return userRoleMapper.findUsernumByName(name);
	}
}
