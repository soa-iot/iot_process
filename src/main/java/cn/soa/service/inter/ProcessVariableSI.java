package cn.soa.service.inter;

import java.util.Map;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.entity.ProblemInfo;

@Service
public interface ProcessVariableSI {

	
	/**   
	 * @Title: addVarsAtFirstNode   
	 * @Description: 在启动流程时，增加流程变量接口   
	 * @return: Map<String,Object>   返回需要加入流程中的变量     
	 */  
	public Map<String,Object> addVarsStartProcess( ProblemInfo problemInfo );
}
