package cn.soa.service.impl.activity;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.service.impl.ActivityS;
import cn.soa.service.inter.UserManagerSI;

/**
 * @ClassName: FindUsersByRoleId
 * @Description: 根据根据角色id查询角色所有的人，并加入流程变量中
 * @author zhugang
 * @date 2019年5月17日
 */
@Service
public class FindUsersByRoleId implements ExecutionListener{
	private static Logger logger = LoggerFactory.getLogger( FindUsersByRoleId.class );
	
	private Expression impVar;
	
	private Expression expVar;
	
	@Autowired
	private UserManagerSI userManagerS;

	@Override
	public void notify(DelegateExecution execution) throws Exception {		
		String impVarvalue  = impVar.getValue(execution).toString();
		String expVarvalue  = expVar.getValue(execution).toString();
		logger.info( impVarvalue );
		logger.info( expVarvalue );
		if( StringUtils.isBlank(impVarvalue)) {
			logger.info( "变量参数值为空，执行失败" );
		}else if( StringUtils.isBlank(expVarvalue) ){
			logger.info( "输出变量参数值为空，执行失败" );
		}else {
			List<String> users = userManagerS.findUsersByRoleId(impVarvalue);
			logger.info( "----------users-----------" );
			logger.info( users.toString() );
			execution.setVariable(expVarvalue, Arrays.asList(users));
		}
		logger.info( "--------ExecFindUsersByRoleId执行完毕-------------" );	
	}

}
