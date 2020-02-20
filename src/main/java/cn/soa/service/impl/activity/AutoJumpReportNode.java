package cn.soa.service.impl.activity;

import java.util.HashMap;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.controller.ProcessC;
import cn.soa.entity.ProblemInfo;
import cn.soa.service.inter.AcitivityHistoryActSI;
import cn.soa.service.inter.activiti.ProcessStartHandler;

@Service
public class AutoJumpReportNode implements ProcessStartHandler {
	private static Logger logger = LoggerFactory.getLogger( AutoJumpReportNode.class );
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private AcitivityHistoryActSI acitivityHistoryActS;

	@Override
	public boolean before( String bsid, ProblemInfo problemInfo ) {
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
	public boolean after( String bsid, String piid, ProblemInfo problemInfo ) {
		if( StringUtils.isBlank(piid) ) {
			logger.info("---------piid为null或空-----------");
			return false;
		}
		
		Task task = taskService.createTaskQuery().processInstanceId(piid).singleResult();							
		if( task != null ) {
			//问题上报描述存入节点备注信息
			String comment = problemInfo.getProblemdescribe();
			String tsid = task.getId();
			if( StringUtils.isBlank( tsid )  ) {
				logger.info("---------tsid为null或空------------" );
			}
			logger.info("---------tsid-----------" + tsid );
			if( StringUtils.isBlank(comment) ) {
				logger.info("---------comment为null或空-----------");
			}else {
				try {
					Comment addComment = taskService.addComment( tsid, piid, comment );
					logger.info("---------问题上报增加备注信息成功：-----------" + addComment );
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("---------问题上报增加备注信息失败-----------");
				}				
			}
			
			//增加流程变量-业务主键\属地单位
			Object bsidVar = taskService.getVariable( tsid , "bsid");
			Object areaVar = taskService.getVariable( tsid , "area");
			
			if( bsidVar == null ) {
				logger.info("---------业务主键为null-----------"); 
				return false;
			}	
			logger.info("---------业务主键为-----------" + bsidVar.toString() ); 
			
			if( areaVar == null ) {
				logger.info("---------属地单位为null-----------"); 
				return false;
			}
			logger.info("---------业务主键为-----------" + areaVar.toString() ); 
			HashMap<String, Object> var = new HashMap<String,Object>();
			var.put( "bsid", bsidVar.toString() );
			var.put( "area", areaVar.toString() );
			taskService.complete( task.getId(), var, true);			
			
			//保存操作名称
			acitivityHistoryActS.updateOprateNameS( piid, tsid, "上报问题" );
			
			return true;
		}
		logger.info("---------task为null-----------");
		return false;
	}

}
