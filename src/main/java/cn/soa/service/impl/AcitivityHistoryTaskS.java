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

import cn.soa.dao.activity.HisTaskMapper;
import cn.soa.entity.activity.HistoryTask;
import cn.soa.service.inter.AcitivityHistorySI;
import cn.soa.service.inter.AcitivityHistoryTaskSI;
import cn.soa.service.inter.ActivitySI;

@Service
public class AcitivityHistoryTaskS implements AcitivityHistoryTaskSI{
	private static Logger logger = LoggerFactory.getLogger( AcitivityHistoryTaskS.class );
	
	@Autowired
    private HisTaskMapper hisTaskMapper;
	
	/**   
	 * @Title: findAllHisTasksBypiid   
	 * @Description: 根据任务piid,查询当前流程实例的所有节点（包括完成和未完成）  
	 * @return: List<HistoryTask>        
	 */  
	@Override
	public List<HistoryTask> findAllHisTasksBypiid( String piid ){
		logger.info( "-S----根据任务piid,查询当前流程实例的所有节点（包括完成和未完成）--------" );
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	
    	try {
    		List<HistoryTask> hisTasks = hisTaskMapper.findAllHisTasksBypiid( piid );
    		return hisTasks;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
