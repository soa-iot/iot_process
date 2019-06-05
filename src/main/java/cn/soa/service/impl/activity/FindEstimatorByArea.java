package cn.soa.service.impl.activity;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.entity.UserOrganization;
import cn.soa.service.inter.UserManagerSI;

/**
 * @ClassName: FindEstimatorByArea
 * @Description: 根据流程变量area，获取问题评估节点处理人
 * @author zhugang
 * @date 2019年6月4日
 */
@Service
public class FindEstimatorByArea implements ExecutionListener{
	private static Logger logger = LoggerFactory.getLogger( FindEstimatorByArea.class );
	
	@Autowired
	private UserManagerSI userManagerS;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String area  = (String) execution.getVariable("area");
		logger.debug( "------流程变量属地单位------" + area );

		List<UserOrganization> users = userManagerS.findUserByArea( area );
		logger.debug( "------问题评估变量执行人------" + users.toString() );
		
		String userStr = "";
		for( UserOrganization u : users ) {
			userStr = u.getName().trim() + ","  + userStr;
		}
		userStr = userStr.substring( 0, userStr.length() - 1 );
		logger.debug( "------问题评估变量执行人------" + userStr);
		execution.setVariable( "estimators", userStr );
	}

}
