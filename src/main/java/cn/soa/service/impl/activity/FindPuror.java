package cn.soa.service.impl.activity;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.UserRoleMapper;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 确定生产办评估人
 */
@Slf4j
public class FindPuror implements ExecutionListener{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		log.info( "---------确定生产办评估人-----------" );
		UserRoleMapper urMapper = SpringUtils.getObject(UserRoleMapper.class);
		
		List<String> purors = urMapper.findPurors();
		if(purors == null || purors.size() == 0) {
			log.info( "---------生产办评估人查询为null-----------" );
		}
		log.info( "---------生产办评估人为：{}-------", purors);
		
		execution.setVariable("puror", purors);
	}

}
