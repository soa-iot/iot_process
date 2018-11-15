package cn.zg.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zg.dao.inter.ProcessInsectionRepository;
import cn.zg.dao.inter.PurificationSchemeDao;
import cn.zg.entity.daoEntity.ProblemInspection;
import cn.zg.service.inter.PAServiceInter;
import cn.zg.service.inter.ProcessServiceInter;

/**
 * @ClassName: PAServiceImpl
 * @Description: 净化分配节点控制层
 * @author zhugang
 * @date 2018年11月14日
 */
@Service
public class PAServiceImpl implements PAServiceInter{
private static Logger logger = LoggerFactory.getLogger( PAServiceImpl.class );
	
	@Autowired
	private ProcessServiceInter processService;
	
	@Autowired
	private ProcessInsectionRepository processInsectRepo;

	/**   
	 * @Title: getHisActByPiid   
	 * @Description: 根据业务id，获取该流程历史节点信息  
	 * @param: @param piid
	 * @param: @return      
	 * @return: List<Map<String,String>>        
	 */  
	public Map<String, String> getHisActByPiid( String piid ){
		/*
		 * 获取taskId
		 */
		ProblemInspection problemInspect = processInsectRepo.findByPiid( piid );
		String taskId = problemInspect.getCurrentTsid();
		
		/*
		 * 查找历史节点
		 */
		if( taskId == null && taskId.trim().isEmpty() ) {
			
		}
		List<HistoricActivityInstance> historicActivityInstances = processService
				.getHisActBytaskId( taskId );
		
		/*
		 * 格式化数据
		 */
		Map<String,String> map = new HashMap<String,String>();
		for( HistoricActivityInstance h : historicActivityInstances) {			
			map.put( h.getActivityId().trim(), h.getActivityName().trim() );
		}
		logger.debug( "" + map );
		return map;
	}
	
	/**   
	 * @Title: findInspeByPiidServ   
	 * @Description: 根据  piid，查询taskId
	 * @param: @param piid
	 * @param: @return      
	 * @return: ProblemInspection        
	 */  
	public ProblemInspection findInspeByPiidServ( String piid ){
		ProblemInspection inspection = processInsectRepo.findByPiid( piid );
		return inspection;
	}
}
