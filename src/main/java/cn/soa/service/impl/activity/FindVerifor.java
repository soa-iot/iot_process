package cn.soa.service.impl.activity;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.UserRoleMapper;
import cn.soa.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 确定领导批示人
 */
@Slf4j
public class FindVerifor implements ExecutionListener{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		log.info( "---------确定领导批示人-----------" );
		UserRoleMapper urMapper = SpringUtils.getObject(UserRoleMapper.class);
		
		List<String> verifors = urMapper.findVerifors();
		if(verifors == null || verifors.size() == 0) {
			log.info( "---------领导批示人查询为null-----------" );
		}
		log.info( "---------领导批示人为：{}-------", verifors);
		
		execution.setVariable("verifor", verifors);
	}

}
