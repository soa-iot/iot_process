package cn.soa.service.impl.activity;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.UserRoleMapper;
import cn.soa.entity.activity.IdentityLink;
import cn.soa.service.inter.AcitivityIdentitySI;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FindCheckor
 * @Description: 确定作业验收的执行人
 */
@Slf4j
public class FindCheckor implements ExecutionListener{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		log.info( "---------确定作业验收的执行人-----------" );
		UserRoleMapper urMapper = SpringUtils.getObject(UserRoleMapper.class);
		
		List<String> checkors = urMapper.findCheckors();
		if(checkors == null || checkors.size() == 0) {
			log.info( "---------作业验收的执行人查询为null-----------" );
		}
		log.info( "--------作业验收的执行人为：{}-------", checkors);
		
		execution.setVariable("checkor", checkors);
	}

}
