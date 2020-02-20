package cn.soa.dao.activity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.activity.IdentityLink;

@Mapper
public interface IdentityLinkMapper {
	
	/**   
	 * @Title: findCandidateByTsid   
	 * @Description: 根据任务tsid查询流程当前代办人  
	 * @return: List<IdentityLink>        
	 */  
	List<IdentityLink> findCandidateByTsid( String tsid );
	
	
	/**   
	 * @Title: findParticipantByPiid   
	 * @Description:  根据流程实例piid查询流程所有节点执行人 
	 * @return: List<IdentityLink>        
	 */  
	List<IdentityLink> findParticipantByPiid( String piid );
	
	/**   
	 * @Title: findConnectPiidByUserId   
	 * @Description:  查找与指定人相关的流程的piid 
	 * @return: List<String>        
	 */  
	List<IdentityLink> findConnectPiidByUserId( String userid );
}
