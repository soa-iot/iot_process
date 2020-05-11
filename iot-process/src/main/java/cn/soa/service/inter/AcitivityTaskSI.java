package cn.soa.service.inter;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
public interface AcitivityTaskSI {

	/**   
	 * @Title: getGroupTaskCandidateByTsid   
	 * @Description:  根据任务tsid，获取当前任务的候选人 
	 * @return: List<IdentityLink>        
	 */  
	List<IdentityLink> getGroupTaskCandidateByTsid(String tsid);

	/**   
	 * @Title: getTaskAssigneeByTsid   
	 * @Description:根据任务tsid，获取当前个人任务的执行人   
	 * @return: Task        
	 */  
	Task getTaskAssigneeByTsid(String tsid);

	/**   
	 * @Title: getTaskByTsid   
	 * @Description:  根据任务tsid，获取当前任务   
	 * @return: Task        
	 */  
	Task getTaskByTsid(String tsid);

	/**   
	 * @Title: getActiveTsidByPiid   
	 * @Description: 根据piid，查找当前活动任务的tsid   
	 * @return: String        
	 */  
	String getActiveTsidByPiid(String piid);

}
