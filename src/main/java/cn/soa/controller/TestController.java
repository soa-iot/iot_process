package cn.soa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.soa.entity.User;
import cn.soa.service.inter.RollBackProcessInter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test/process")
@Slf4j
public class TestController {
	
	@Autowired
	private RollBackProcessInter rollBackProcessInter;
	
	@PostMapping("/nodes/before")
	public boolean testRollBackProcessC(User user ){
		log.info("---测试指定人员的流程回退 --User={}",user.toString());
		rollBackProcessInter.rollBackByUserid(user);
		return false;
	}
}
