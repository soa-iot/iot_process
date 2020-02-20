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

import cn.soa.service.inter.UserManagerSI;

/**
 * @ClassName: FindUserPostByNum
 * @Description: 根据用户的id查询用户的岗位  
 * @author zhugang
 * @date 2019年5月17日
 */
@Service
public class FindUserPostByNum implements ExecutionListener{
	private static Logger logger = LoggerFactory.getLogger( FindUserPostByNum.class );
	
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
			String post = userManagerS.findUserPostByNum(impVarvalue);
			logger.info( "----------post-----------" );
			logger.info( post.toString() );
			execution.setVariable(expVarvalue, post);
		}
		logger.info( "--------FindUserPostByNum执行完毕-------------" );	
		
	}

}
