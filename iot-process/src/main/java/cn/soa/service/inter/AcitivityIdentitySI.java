package cn.soa.service.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.soa.entity.activity.IdentityLink;

@Service
public interface AcitivityIdentitySI {

	/**   
	 * @Title: findCandidateByTsid   
	 * @Description:  根据任务tsid查询流程当前代办人   
	 * @return: List<IdentityLink>        
	 */  
	List<IdentityLink> findCandidateByTsid(String tsid);

	/**   
	 * @Title: findParticipantByPiid   
	 * @Description: 根据流程实例piid查询流程所有节点执行人     
	 * @return: List<IdentityLink>        
	 */  
	List<IdentityLink> findParticipantByPiid(String piid);

	/**   
	 * @Title: getConnectPiidByUserId   
	 * @Description: 查找与指定人相关的流程的piid    
	 * @return: List<String>        
	 */  
	List<String> getConnectPiidByUserId(String userid);

	/**   
	 * @Title: findCandidateByPiid   
	 * @Description:  根据流程实例piid，查询流程当前代办人     
	 * @return: List<IdentityLink>        
	 */  
	List<IdentityLink> findCandidateByPiid(String piid);

}
