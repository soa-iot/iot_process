package cn.zg.dao.inter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.zg.entity.daoEntity.Role;
import cn.zg.entity.daoEntity.executorTreeNode;

/**
 * @ClassName: RoleRepository
 * @Description: 员工角色信息
 * @author zhugang
 * @date 2018年10月5日
 */
public interface RoleRepository extends JpaRepository<Role, String> {
	
	/**   
	 * @Title: findByEmpId   
	 * @Description: 根据用户id,查询用户角色  
	 * @param: @param empId
	 * @param: @return      
	 * @return: List<Role>        
	 */  
	@Transactional
	@Query( nativeQuery = true, 
			value = " select r.* "
					+ " from process_employee_role er"
					+ " left join process_role r "
					+ " on er.rol_id = r.rol_id "
					+ " where er.emp_id = :empId " )
	List<Role> findByEmpId( @Param( "empId" ) String empId);
	
	@Transactional
	@Query( nativeQuery = true,
			value = "   select 'name' as 'key', e.EMP_NAME as excutorName "
					+ " from process_role r"
					+ " left join process_employee_role er"
					+ " on er.rol_id = r.rol_id"
					+ " left join process_employee e "
					+ " on er.EMP_ID= e.EMP_ID  "
					+ " where r.ROL_NAME = '净化技干' " )
	List<executorTreeNode> findExectorInPA();
}
