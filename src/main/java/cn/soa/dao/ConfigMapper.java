package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.Config;

@Mapper
public interface ConfigMapper {
	
	/**   
	 * @Title: findAll   
	 * @Description: 查找所有的流程变量  
	 * @return: List<Config>        
	 */  
	public List<Config> findAll();
}
