package cn.soa.service.impl.activity;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.soa.entity.ProblemInfo;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.utils.SpringUtils;

@Service
public class SetProblemState implements ExecutionListener{
	
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger( SetProblemState.class );

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		logger.info( "---------流程闭环状态---------" );	
		ProblemInfoSI problemInfoS= SpringUtils.getObject(ProblemInfoSI.class);
		String piid = execution.getProcessInstanceId();
		
		logger.info( "---------获取流程piid："+piid );	
		
		if( piid == null ) {
			logger.info( "---------获取流程piid为空或null------------" );			
		}else {
			
			ProblemInfo problemInfo = new ProblemInfo();
			problemInfo.setPiid(piid);
			problemInfo.setProblemstate("FINISHED");
			Integer row = problemInfoS.changeProblemDescribeByPiid(problemInfo);
			
			logger.info( "---------闭环更新行数------------" + row );
			if (row == 1) {
				logger.info( "---------流程闭环成功------------" );
			}else {
				logger.info( "---------流程闭环失败------------Problemstate修改行数：" + row);
			}
		}	
	}
	
}
