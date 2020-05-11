package cn.soa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.activity.HisActMapper;
import cn.soa.dao.activity.HisTaskMapper;
import cn.soa.entity.activity.HistoryAct;
import cn.soa.entity.activity.HistoryTask;
import cn.soa.service.inter.AcitivityHistoryActSI;
import cn.soa.service.inter.AcitivityHistorySI;
import cn.soa.service.inter.AcitivityHistoryTaskSI;
import cn.soa.service.inter.AcitivityIdentitySI;
import cn.soa.service.inter.ActivitySI;

@Service
public class AcitivityHistoryActS implements AcitivityHistoryActSI{
	private static Logger logger = LoggerFactory.getLogger( AcitivityHistoryActS.class );
	
	@Autowired
    private HisActMapper hisActMapper;
	
	@Autowired
    private AcitivityIdentitySI acitivityIdentityS;
	
	
	/**   
	 * @Title: findAllHisTasksBypiid   
	 * @Description: 根据任务piid,查询当前流程实例的所有节点（包括完成和未完成）  
	 * @return: List<HistoryTask>        
	 */  
	@Override
	public List<HistoryAct> findAllHisActsBypiid( String piid ){
		logger.info( "-S----根据任务piid,查询当前流程实例的所有节点（包括完成和未完成）--------" );
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	
    	try {
    		List<HistoryAct> hisActs = hisActMapper.findAllHisActsBypiid( piid );
    		return hisActs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**   
	 * @Title: updateOprateNameS   
	 * @Description: 给每个节点增加操作名称  
	 * @return: int        
	 */  
	@Override
	public int updateOprateNameS( String piid, String tsid, String operateName ){
		//检查
		logger.info( "-S----给当前节点增加操作名称--------" );
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return 0;
		}
    	if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null-------------" );
			return 0;
		}
    	if( StringUtils.isBlank( operateName ) ) {
			logger.info( "---S--------任务operateName为null-------------" );
			return 0;
		}
		
    	//修改数据
		try {
			int i = hisActMapper.updateOprateName(piid, tsid, operateName);
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
