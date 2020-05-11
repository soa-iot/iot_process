package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.UnsafeType;



@Mapper
public interface UnsafeTypeMapper {
	
	/**   
	 * @Title: findAll   
	 * @Description: 查找所有的不安全行为类型
	 * @return: List<UnsafeType>        
	 */  
	public List<UnsafeType> findAll();
}
