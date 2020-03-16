package cn.soa.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.ProblemInfoMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.BussinessSI;

@Service
public class BussinessS implements BussinessSI{
	private static Logger logger = LoggerFactory.getLogger( BussinessS.class );

	@Autowired
	private ProblemInfoMapper problemInfoMapper;
	
	/**   
	 * <p>Title: dealProblemReport</p>   
	 * <p>Description: </p>   流程启动节点（问题上报）业务处理逻辑方法
	 * @param bussiness
	 * @return   
	 * @see cn.soa.service.inter.BussinessSI#dealProblemReport(java.util.Map)   
	 */ 

	@Override
	public String dealProblemReport( ProblemInfo problemInfo ) {	
		//保存问题上报信息
		Integer i = problemInfoMapper.insertOne( problemInfo );		
		if( i > 0 ) {
			logger.debug( "------流程启动节点（问题上报）业务处理逻辑方法-----主键---" +problemInfo.getTProblemRepId() );
			return problemInfo.getTProblemRepId();
		}
		return null;
	}

	/**   
	 * <p>Title: nextNode</p>   
	 * <p>Description: </p>  下一步操作时，业务处理，分发处理节点 
	 * @param nodeid
	 * @param bussiness
	 * @return   
	 * @see cn.soa.service.inter.BussinessSI#nextNode(java.lang.String, java.util.Map)   
	 */ 
	@Override
	public String nextNode(String nodeid, Map<String, Object> bussiness) {
		logger.debug( "--S---------执行流程的下一步------------" );
		if( "".equals( nodeid.trim() )) {
			 node1();
		}else if( "".equals( nodeid.trim() )) {
			
		}else if( "".equals( nodeid.trim() )) {
			
		}else {
			logger.debug( "--S---------流程节点id和方法名未匹配------------" );
		}
		return null;
	}
	
	public String node1() {
		return null;
	}
	
	public String node2() {
		return null;
	}
}
