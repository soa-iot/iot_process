package cn.soa.service.impl.activity;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.service.inter.UserManagerSI;
import cn.soa.service.inter.activiti.ProcessStartHandler;
import cn.soa.utils.SpringUtils;

@Service
public class AddPiidToBussTable implements ExecutionListener{
	private static Logger logger = LoggerFactory.getLogger( AddPiidToBussTable.class );

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String bsid = execution.getProcessBusinessKey();
		logger.info( "------------业务主键2------------" + bsid );
		String piid = execution.getProcessInstanceId();
		logger.info( "------------流程实例id------------" + piid );
		
		if( StringUtils.isBlank( bsid ) ) {
			logger.info( "------------业务主键bsid为空或null------------" );
		}
		
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "------------业务主键piid为空或null------------" );
		}
		
		try {
			if( !StringUtils.isBlank( bsid ) && !StringUtils.isBlank( piid )) {
				ProblemInfoSI problemInfoS = SpringUtils.getObject(ProblemInfoSI.class);
				problemInfoS.updatePiidByBsid( bsid, piid );	
			}
					
		} catch (Exception e) {
			e.printStackTrace();
			logger.info( "------------业务表加入piid失败------------" );
		}
		
	}
	
}
