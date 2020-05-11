package cn.soa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.activity.IdentityLinkMapper;
import cn.soa.entity.activity.IdentityLink;
import cn.soa.service.inter.AcitivityIdentitySI;
import cn.soa.service.inter.ActivitySI;

@Service
public class AcitivityIdentityS implements AcitivityIdentitySI{
	private static Logger logger = LoggerFactory.getLogger( AcitivityIdentityS.class );
	
	@Autowired
	private IdentityLinkMapper identityLinkMapper;
	
	@Autowired
	private ActivitySI activityS;
	
	/**   
	 * @Title: getConnectorByPiid   
	 * @Description: 查找与指定人相关的流程的piid  
	 * @return: List<String>        
	 */
	@Override
	public List<String> getConnectPiidByUserId( String userid ){
		logger.info( "---S--------查找与指定人相关的流程的piid  -------------" );
		if( StringUtils.isBlank( userid ) ) {
			logger.info( "---S--------任务userid为null或空-------------" );
			return null;
		}	
		
		try {
			List<String> piids = new ArrayList<String>();
			List<IdentityLink> idendtitys = identityLinkMapper.findConnectPiidByUserId( userid );
			for( IdentityLink i : idendtitys) {
				piids.add( i.getPROC_INST_ID_() );
			} 
			if( piids != null && piids.size() > 0 ) {
				logger.info( "---S--------piid查询成功，包括：-------------" );
				piids.forEach( p -> logger.info(  p.toString()) );
			}else {
				logger.info( "---S--------piid查询结果为null或空：-------------" );
			}
			return piids;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**   
	 * @Title: findCandidateByTsid   
	 * @Description: 根据任务tsid查询流程当前代办人  
	 * @return: List<IdentityLink>        
	 */ 
	@Override
	public List<IdentityLink> findCandidateByTsid( String tsid ){
		logger.info( "---S--------根据任务tsid查询流程当前代办人  -------------" );
		if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null或空-------------" );
			return null;
		}	
			
		try {
			List<IdentityLink> identitys = identityLinkMapper.findCandidateByTsid( tsid );
			return identitys;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**   
	 * @Title: findCandidateByPiid   
	 * @Description: 根据流程实例piid，查询流程当前代办人    
	 * @return: List<IdentityLink>        
	 */ 
	@Override
	public List<IdentityLink> findCandidateByPiid( String piid ){
		logger.info( "---S--------根据任务tsid查询流程当前代办人  -------------" );
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null或空-------------" );
			return null;
		}
		
		//根据piid查询当前流程实例的tsid
		String tsid = activityS.getTsidByPiid( piid );
		if( StringUtils.isBlank( tsid ) ) {
			logger.info( "---S--------任务tsid为null或空-------------" );
			return null;
		}
			
		try {
			List<IdentityLink> identitys = identityLinkMapper.findCandidateByTsid( tsid );
			return identitys;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	/**   
	 * @Title: findParticipantByPiid   
	 * @Description: 根据流程实例piid查询流程所有节点执行人   
	 * @return: List<IdentityLink>        
	 */ 
	@Override
	public List<IdentityLink> findParticipantByPiid( String piid ){
		logger.info( "---S--------根据流程实例piid查询流程所有节点执行人   -------------" );
		if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null或空-------------" );
			return null;
		}	
			
		try {
			List<IdentityLink> identitys = identityLinkMapper.findParticipantByPiid( piid );
			return identitys;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
