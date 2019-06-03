package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.UnsafeAction;


@Mapper
public interface UnsafeActionMapper {
	
	/**   
	 * @Title: findAll   
	 * @Description: 查找所有的不安全行为  
	 * @return: List<UnsafeAction>        
	 */  
	public List<UnsafeAction> findAll();
}
