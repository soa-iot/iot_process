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

import cn.soa.service.inter.AcitivityHistorySI;
import cn.soa.service.inter.ActivitySI;

@Service
public class AcitivityHistoryS implements AcitivityHistorySI{
	private static Logger logger = LoggerFactory.getLogger( AcitivityHistoryS.class );
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private TaskService taskService;
	 
	/**   
	 * @Title: getHisTaskNodesInfoByPiid   
	 * @Description:   根据流程piid，获取当前流程的任务节点信息
	 * @return: List<Map<String,Object>>        
	 */ 
	@Override
	public List<Map<String,Object>> getHisTaskNodesInfoByPiid( String piid ){
		logger.info( "---S--------根据流程piid，获取当前流程的任务节点信息-------------" );
		ArrayList<Map<String, Object>> allHistoryInfos = new ArrayList<Map<String,Object>>();
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------piid为null-------------" );
			return null;
		}
		
		String tsid = getTsidByPiid( piid ); 
		if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------tsid为null-------------" );
			return null;
		}
		
		/*
		 * 获取历史节点
		 */
		try {
			List<HistoricTaskInstance> historyNodes = getHisTaskNodesByPiid( piid );
			logger.info(historyNodes.toString());
			for( HistoricTaskInstance h : historyNodes ) {
				HashMap<String, Object> tempMap = new HashMap<String, Object>();
				String taskid = h.getId();
				List<Comment> comments = taskService.getTaskComments( taskid );
				if( comments.size() > 0 ) {
					for( Comment c : comments ) {
						Object o = tempMap.get( "nodeComment");
						if( o != null ) {
							tempMap.put( "nodeComment", o + c.getFullMessage() );
						}else {
							tempMap.put( "nodeComment",  c.getFullMessage() );
						}					
					}				
				}else {
					tempMap.put( "nodeComment", "" );
				}
					
				tempMap.put( "nodeId", h.getId() );
				tempMap.put( "nodeName", h.getName() );
				tempMap.put( "nodeExecutor", h.getAssignee() );
				tempMap.put( "nodeEndTime", h.getEndTime() );
				allHistoryInfos.add( tempMap );
			}
			return allHistoryInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**   
	 * @Title: getHisTaskNodesByPiid   
	 * @Description: 根据流程piid，获取当前流程的任务节点  
	 * @return: List<HistoricTaskInstance>        
	 */  
	public List<HistoricTaskInstance> getHisTaskNodesByPiid( String piid ){
		logger.info( "---S--------根据流程piid，获取当前流程的任务节点-------------" );
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	try {
    		List<HistoricTaskInstance> lists = historyService
    				.createHistoricTaskInstanceQuery()
    				.processInstanceId( piid )
    				.orderByTaskCreateTime()
    				.asc()
    				.list();
    		if( lists != null && lists.size()>0 ) {
    			logger.info( lists.toString() );
    		}else {
    			logger.info( "---------根据流程piid，查询该流程的历史任务节点  为null或空-------------" );
    		}
    		
    		return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}   	
    }
	
	 /**   
     * @Title: getTsidByPiid   
     * @Description:  根据piid查询当前任务节点的tsid 
     * @return: String        
     */ 
    //@Override
    public String getTsidByPiid( String piid ) {
    	if( StringUtils.isBlank( piid )) {
    		logger.info( "------piid为null--------" );
    		return null;
    	}
    	try {
    		List<Task> tasks = taskService.createTaskQuery().processInstanceId(piid).list();
    		if( tasks.size() > 0 ) {
    			logger.info( tasks.toString() );
    			return tasks.get(0).getId();
    		}
    		logger.info( "------未正确找到task对象-------" );
    		return null;    		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    
    
}
