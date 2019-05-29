package cn.soa.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.ConfigMapper;
import cn.soa.entity.Config;
import cn.soa.service.inter.ConfigSI;

@Service
public class ConfigS implements ConfigSI{
	private static Logger logger = LoggerFactory.getLogger( ConfigS.class );
	
	@Autowired
	private ConfigMapper configMapper;
	
	/**   
	 * @Title: findAll   
	 * @Description: 查找所有的流程变量  
	 * @return: List<Config>        
	 */  
	@Override
	public List<Config> findAll(){
		try {
			List<Config> configs = configMapper.findAll();
			return configs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**   
	 * @Title: setVarsAtStart   
	 * @Description: 在流程启动时注入数据配置的流程变量  
	 * @return: Map<String,Object>        
	 */  
	@Override
	public Map<String,Object> setVarsAtStart() {
		Map<String,Object> vars = new HashMap<String,Object>();
		List<Config> configs = findAll();
		if( configs != null ) {
			if( configs.size() > 0 ) {
				for( Config c : configs ) {
					vars.put( c.getVarname(), c.getVarvalue() );
				}
			}
			logger.debug( vars.toString() );
			return vars;
		}else {
			return null;
		}		
	}
}
