package cn.soa.service.impl;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.soa.service.inter.ActivityUtilsSI;

public class ActivityUtilsS{
	private static Logger logger = LoggerFactory.getLogger( ActivityUtilsS.class );
	
	@Autowired
    private static TaskService taskService;
	
	 /**   
     * @Title: getTsidByPiid   
     * @Description:  根据piid查询当前任务节点的tsid 
     * @return: String        
     */ 
    public static String getTsidByPiid( String piid ) {
    	if( StringUtils.isBlank( piid )) {
    		logger.info( "------piid为null--------" );
    		return null;
    	}
    	try {
    		List<Task> tasks = taskService.createTaskQuery().processInstanceId(piid).list();
    		if( tasks.size() > 0 ) {
    			logger.info( "tasks.toString()--------------------------"+tasks.toString() );
    			return tasks.get(0).getId();
    		}
    		logger.info( "------未正确找到task对象-------" );
    		return null;    		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**   
     * @Title: getPiidByTsid   
     * @Description:  根据tsid查询当前任务节点的  piid
     * @return: String        
     */
    public static String getPiidByTsid( String tsid ) {
    	if( StringUtils.isBlank( tsid )) {
    		logger.info( "------tsid为null--------" );
    		return null;
    	}
    	
    	try {
    		Task task = taskService.createTaskQuery().taskId(tsid).singleResult();
    		if( task != null ) {
    			logger.info( task.toString() );
    			return task.getProcessInstanceId();
    		}
    		logger.info( "------未正确找到task对象-------" );
    		return null;    		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
}
