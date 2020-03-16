package cn.soa.service.impl.activity;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetReceivorToComplementor implements ExecutionListener{
	private static Logger logger = LoggerFactory.getLogger( SetReceivorToComplementor.class );

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.info( "---------设置接受作业节点执行人为完成作业节点执行人------------" );	
		Object receivorObj = execution.getVariable( "receivor" );
		
		if( receivorObj == null ) {
			logger.info( "---------流程变量-接收作业执行人为空或null------------" );			
		}else {
			String receivor = receivorObj.toString();
			logger.info( "---------流程变量-接收作业执行人------------" + receivor );	
			execution.setVariable( "complementor", receivor );
		}	
	}
	
}
