
/**
 * <一句话功能描述>
 * <p>人员组织树业务层接口
 * @author 陈宇林
 * @version [版本号, 2019年6月4日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
package cn.soa.service.inter;

import java.util.List;


import cn.soa.entity.UserOrganization;


public interface UserOrganizationTreeService {
	
	/**
	 * 获取人鱼组织树的数据
	 * @return
	 */
	public List<UserOrganization> getUserOrganizationTreeData();
	
	/**
	 * 根据属地查询当前属地的下一级组织或人员service层
	 * @return
	 */
	List<UserOrganization> getUserOrganizationByName(String name,String username);

	/**
	 * 获取净化技术干部/维修技术干部service层
	 * @return
	 */
	List<UserOrganization> getUserOrganizationByOrgan(String organ,String username);
}
