
/**
 * <一句话功能描述>
 * <p> 人员组织树控制层
 * @author 陈宇林
 * @version [版本号, 2019年6月4日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
package cn.soa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.soa.entity.LayuiTree;
import cn.soa.entity.ResponseObject;
import cn.soa.entity.ResultJson;
import cn.soa.entity.UserOrganization;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.service.inter.UserOrganizationTreeService;

@RestController
@RequestMapping("/userOrganizationTree")
public class UserOrganizationTreeController {

	@Autowired
	private UserOrganizationTreeService userOrganizationTreeService;
	
	@Autowired
	private UserManagerSI userManagerS;

	/**
	 * 获取人员组织树的数据
	 * 
	 * @return
	 */
	@RequestMapping("/userOrganizationTreeData")
	public ResponseObject<List<UserOrganization>> userOrganizationTreeData() {

		ResponseObject<List<UserOrganization>> resObj;
		try {
			List<UserOrganization> result = userOrganizationTreeService.getUserOrganizationTreeData();
			resObj = new ResponseObject<List<UserOrganization>>(0, "success", result);
		} catch (Exception e) {
			e.printStackTrace();
			resObj = new ResponseObject<List<UserOrganization>>(1, "failed>>>" + e.getMessage(), null);
		}

		return resObj;
	}
	
	/**
	 * 根据组织获取所在组织人员
	 * @param area 当前组织
	 * @param username 当前用户
	 * @return
	 */
	@GetMapping("/userOrganizationArea")
	public ResponseObject<List<UserOrganization>> getUserOrganizationByNameTreeData(String area,String username) {

		System.err.println("-------------------------------------------属地单位："+area);
		ResponseObject<List<UserOrganization>> resObj;
		try {
			List<UserOrganization> result = userOrganizationTreeService.getUserOrganizationByName(area,username);
			resObj = new ResponseObject<List<UserOrganization>>(0, "success", result);
		} catch (Exception e) {
			e.printStackTrace();
			resObj = new ResponseObject<List<UserOrganization>>(1, "failed>>>" + e.getMessage(), null);
		}

		return resObj;
	}

	@PostMapping("/users")
	public ResultJson<List<LayuiTree>> getUserByOrgid(String dept) {

		System.err.println("部门名称："+dept);	
		List<LayuiTree> result = userManagerS.findUserByDept(dept);
		
		return new ResultJson<List<LayuiTree>>(result);
	}


	/**
	 * 
	 * 根据角色获取人员控制层
	 * @return
	 */
	@GetMapping("/userOrganizationOrgan")
	public ResponseObject<List<UserOrganization>> getUserOrganizationByOrgan(String organ,String username){
		
		System.err.println("-------------------------------------------所在组织："+organ);
		System.err.println("-------------------------------------------用户名："+username);
		ResponseObject<List<UserOrganization>> resObj;
		try {
			List<UserOrganization> result = userOrganizationTreeService.getUserOrganizationByOrgan(organ, username);
			resObj = new ResponseObject<List<UserOrganization>>(0, "success", result);
		} catch (Exception e) {
			e.printStackTrace();
			resObj = new ResponseObject<List<UserOrganization>>(1, "failed>>>" + e.getMessage(), null);
		}

		return resObj;

	}
}
