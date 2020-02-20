package cn.soa.service.impl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.soa.entity.ProblemInfo;
import cn.soa.service.impl.AcitivityTaskS;
import cn.soa.service.impl.ActivityS;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.utils.SpringUtils;

/**
 * 责任人后置任务
 * @author Bru.Lo
 *
 */
@Service
public class SetProblemResavepeople implements ExecutionListener{
	
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger( SetProblemResavepeople.class );

	/**
	 * 将责任人写入数据库
	 */
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		//execution.getVariable(variableName);
		logger.info( "---------获取责任人后置任务---------" );	
		
		Map<String, String> map = new HashMap<>();
		map.put("estimate", "estimators");
		map.put("report", "reporter");
		map.put("pure", "puror");
		map.put("repair", "repairor");
		map.put("arrange", "arrangor");
		map.put("receive", "receivor");
		map.put("complement", "complementor");
		map.put("check", "checkor");
		//map.put("end", "");
		
		
		String activityId = execution.getCurrentActivityId();
		logger.info( "---------后置任务当前流程id："+activityId );
		
		String piid = execution.getProcessInstanceId();
		logger.info( "---------获取流程piid："+piid );	
		String name = null;
		logger.info( "---------获取流程piid："+!("end".equals(activityId)) );	
		
		if (!("end".equals(activityId))) {
			name = execution.getVariable(map.get(activityId)).toString();
		} else {

			AcitivityTaskS acitivityTaskS = SpringUtils.getObject(AcitivityTaskS.class);
			
			ActivityS activityS = SpringUtils.getObject(ActivityS.class);
			
			String tsid = activityS.getTsidByPiid(piid);
			logger.info("---------后置任务当前流程tsid：" + tsid);
				
			//获取责任人名
			List<IdentityLink> identityLinks = acitivityTaskS.getGroupTaskCandidateByTsid(tsid);
			List<String> candidateNames = new ArrayList<>();
			
			for (IdentityLink identityLink : identityLinks) {
				if ("candidate".equals(identityLink.getType())) {
					candidateNames.add(identityLink.getUserId());
					logger.info("---------闭环候选人信息identityLink：" + identityLink);
				}

			}
			name = StringUtils.join(candidateNames, ",");

		}
		
		ProblemInfoSI problemInfoS= SpringUtils.getObject(ProblemInfoSI.class);
		
		logger.info( "---------后置任务候选人名："+name );	
		
		
		if( piid == null ) {
			logger.info( "---------获取流程piid为空或null------------" );			
		}else {		
			ProblemInfo problemInfo = new ProblemInfo();
			problemInfo.setPiid(piid);
			problemInfo.setMaintenanceman (name);
			Integer row = problemInfoS.changeProblemDescribeByPiid(problemInfo);			
			logger.info( "---------责任人后置任务更新行数------------" + row );			
		}	
	}
	
}
