package cn.soa.service.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AcitivityHistorySI {

	/**   
	 * @Title: getHisTaskNodesInfoByPiid   
	 * @Description:  根据流程piid，获取当前流程的任务节点信息  
	 * @return: List<Map<String,Object>>        
	 */  
	List<Map<String, Object>> getHisTaskNodesInfoByPiid(String piid);

}
