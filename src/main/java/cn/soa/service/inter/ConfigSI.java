package cn.soa.service.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.soa.entity.Config;

@Service
public interface ConfigSI {

	/**   
	 * @Title: findAll   
	 * @Description: 查找所有的流程变量     
	 * @return: List<Config>        
	 */  
	List<Config> findAll();

	/**   
	 * @Title: setVarsAtStart   
	 * @Description:   在流程启动时注入数据配置的流程变量  
	 * @return: Map<String,Object>        
	 */  
	Map<String, Object> setVarsAtStart();
	
}
