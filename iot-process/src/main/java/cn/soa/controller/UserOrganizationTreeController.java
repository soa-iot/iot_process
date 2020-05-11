package cn.soa.controller;

import cn.soa.entity.ResponseObject;
import cn.soa.entity.ResultJson;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.service.inter.UserOrganizationTreeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/userOrganizationTree"})
public class UserOrganizationTreeController {

   @Autowired
   private UserOrganizationTreeService userOrganizationTreeService;
   @Autowired
   private UserManagerSI userManagerS;


   @RequestMapping({"/userOrganizationTreeData"})
   public ResponseObject userOrganizationTreeData() {
      ResponseObject resObj;
      try {
         List e = this.userOrganizationTreeService.getUserOrganizationTreeData();
         resObj = new ResponseObject(0, "success", e);
      } catch (Exception var3) {
         var3.printStackTrace();
         resObj = new ResponseObject(1, "failed>>>" + var3.getMessage(), (Object)null);
      }

      return resObj;
   }

   @GetMapping({"/userOrganizationArea"})
   public ResponseObject getUserOrganizationByNameTreeData(String area, String username) {
      System.err.println("-------------------------------------------属地单位：" + area);

      ResponseObject resObj;
      try {
         List e = this.userOrganizationTreeService.getUserOrganizationByName(area, username);
         resObj = new ResponseObject(0, "success", e);
      } catch (Exception var5) {
         var5.printStackTrace();
         resObj = new ResponseObject(1, "failed>>>" + var5.getMessage(), (Object)null);
      }

      return resObj;
   }

   @PostMapping({"/users"})
   public ResultJson getUserByOrgid(String dept) {
      System.err.println("部门名称：" + dept);
      List result = this.userManagerS.findUserByDept(dept);
      return new ResultJson(result);
   }

   @GetMapping({"/userOrganizationOrgan"})
   public ResponseObject getUserOrganizationByOrgan(String organ, String username) {
      System.err.println("-------------------------------------------所在组织：" + organ);
      System.err.println("-------------------------------------------用户名：" + username);

      ResponseObject resObj;
      try {
         List e = this.userOrganizationTreeService.getUserOrganizationByOrgan(organ, username);
         resObj = new ResponseObject(0, "success", e);
      } catch (Exception var5) {
         var5.printStackTrace();
         resObj = new ResponseObject(1, "failed>>>" + var5.getMessage(), (Object)null);
      }

      return resObj;
   }
}
