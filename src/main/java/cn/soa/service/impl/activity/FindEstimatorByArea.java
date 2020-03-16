package cn.soa.service.impl.activity;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.soa.entity.UserOrganization;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.utils.SpringUtils;

/**
 * @ClassName: FindEstimatorByArea
 * @Description: 根据流程变量area，获取问题评估节点处理人
 * @author zhugang
 * @date 2019年6月4日
 */
@Service
public class FindEstimatorByArea implements ExecutionListener{
	private static Logger logger = LoggerFactory.getLogger( FindEstimatorByArea.class );

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		UserManagerSI userManagerS= SpringUtils.getObject(UserManagerSI.class);
		String area  = (String) execution.getVariable("area");
		logger.info( "------流程变量属地单位-11111111-----" );
		logger.info( "------流程变量属地单位------" + area );
		logger.info( "------流程变量属地单位---userManagerS---" + userManagerS );

		List<UserOrganization> users = userManagerS.findUserByArea( area );
		logger.info( "------问题评估变量执行人------" + users.toString() );
		
		String userStr = "";
		ObjectMapper mapper = new ObjectMapper();
		for( int i = 0; i < users.size(); i++  ) {
			UserOrganization resource = mapper.convertValue(users.get(i), UserOrganization.class);
			userStr = resource.getName().trim() + ","  + userStr;
		}
		userStr = userStr.substring( 0, userStr.length() - 1 );
		logger.info( "------问题评估变量执行人------" + userStr);
		execution.setVariable( "estimators", userStr );
	}

}
