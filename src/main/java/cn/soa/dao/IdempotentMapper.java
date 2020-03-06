package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.bo.MQIdempotent;

@Mapper
public interface IdempotentMapper {

	/**   
	 * @Title: countByKey   
	 * @Description: 查询key是否存在  
	 * @return: int        
	 */  
	int countByKey(String idempotent);

	/**   
	 * @Title: insert   
	 * @Description: 保存幂等  
	 * @return: void        
	 */  
	void insert(MQIdempotent m);

}
