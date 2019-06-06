package cn.soa.service.impl.activity;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.controller.ProcessC;
import cn.soa.service.inter.activiti.ProcessStartHandler;

@Service
public class AutoJumpReportNode implements ProcessStartHandler {
	private static Logger logger = LoggerFactory.getLogger( AutoJumpReportNode.class );
	
	@Autowired
	private TaskService taskService;

	@Override
	public boolean before() {
		// TODO Auto-generated method stub
		return false;
	}

	/**   
	 * <p>Title: after</p>   
	 * <p>Description: </p>  流程执行后 
	 * @param piid
	 * @return   
	 * @see cn.soa.service.inter.activiti.ProcessStartHandler#after(java.lang.String)   
	 */ 
	@Override
	public boolean after( String piid ) {
		if( StringUtils.isBlank(piid) ) {
			logger.debug("---------piid为null或空-----------");
			return false;
		}
		Task task = taskService.createTaskQuery().processInstanceId(piid).singleResult();
		if( task != null ) {
			logger.debug("---------tsid-----------" + task.getId() );
			taskService.complete( task.getId() );
			return true;
		}
		logger.debug("---------task为null-----------");
		return false;
	}

}
