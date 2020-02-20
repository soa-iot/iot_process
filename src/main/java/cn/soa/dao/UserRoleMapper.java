package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.RoleVO;

/**
 * 用户角色持久层接口
 * @author Jiang, Hang
 *
 */
@Mapper
public interface UserRoleMapper {
	
	/**
	 * 查找仪表、电气、机械这三个班长
	 * @param  无
	 * @return 用户列表
	 */
	List<RoleVO> findRepair();
	
	/**
	 * 查找所在组下的用户列表
	 * @param  orgID  组织标号
	 * @return 所在组下的用户列表
	 */
	List<String> findUserByOrgid(String orgID);
	
	/**
	 * 根据组织编号查找该组织名称
	 * @param  dept  部门名称
	 * @return 组织名称
	 */
	String findOrgNameByOrgid(String dept);
	
	/**
	 * 根据用户名查找用户编号
	 * @param  name 用户名
	 * @return 用户编号
	 */
	String findUsernumByName(String name);
	
}
