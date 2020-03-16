
/**
 * <一句话功能描述>
 * <p>人员组织树业务层实现
 * @author 陈宇林
 * @version [版本号, 2019年6月4日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
package cn.soa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.UserOrganizationTreeMapper;
import cn.soa.entity.UserOrganization;
import cn.soa.service.inter.UserOrganizationTreeService;

@Service
public class UserOrganizationTreeServiceImpl implements UserOrganizationTreeService {

	@Autowired
	private UserOrganizationTreeMapper uotMapper;
	
	/**
	 * getUserOrganizationByParentId递归集合属性
	 */
	private List<UserOrganization> list=new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.soa.service.inter.UserOrganizationTreeService#getUserOrganizationTreeData(
	 * )
	 */
	@Override
	public List<UserOrganization> getUserOrganizationTreeData() {

		/**
		 * 查询人员组织树数据
		 */
		List<UserOrganization> userOrganizations = uotMapper.findAll();

		return userOrganizations;
	}

	/**
	 * 根据属地查询当前属地的下一级组织或人员service层
	 * @return
	 */
	@Override
	public List<UserOrganization> getUserOrganizationByName(String name,String username) {
		
		List<UserOrganization> listuser = new ArrayList<UserOrganization>();
		List<UserOrganization> list = uotMapper.findUserOrganizationByName(name);
		listuser.addAll(list);
		
		//判断里面是否有组织
		for (UserOrganization userOrganization : list) {
			if (userOrganization.getIs_parent()==0) {
				listuser.addAll(getUserOrganizationByParentId(userOrganization.getUsernum()));
			}
		}
		
		for (int i = 0; i < listuser.size(); i++) {
			if (listuser.get(i).getName().equals(username)) {
				listuser.remove(i);
				break;
			}
		}
		this.list.clear();
		return listuser;
	}

	/**
	 * 获取净化技术干部/维修技术干部service层
	 * @return
	 */
	public List<UserOrganization> getUserOrganizationByOrgan(String organ,String username){
		return  findUserOrganizationByOrgan(organ, username);
	}
	
	/**
	 * 根据属地id查询当前属地的下一级人员service层
	 * @return
	 */
	
	private List<UserOrganization> getUserOrganizationByParentId(String usernum) {

		List<UserOrganization> listuser = uotMapper.findUserOrganizationByParentId(usernum);
		list.addAll(listuser);

		for (int i=0;i<listuser.size();i++) {
			if (listuser.get(i).getIs_parent()==0) {
				getUserOrganizationByParentId(listuser.get(i).getUsernum());
			}
		}

		return list;

	}
	
	/**
	 * 获取净化技术干部/维修技术干部service层
	 * @return
	 */
	private List<UserOrganization> findUserOrganizationByOrgan(String organ,String username){
		return uotMapper.findUserOrganizationByOrgan(organ, username);
	}
}
