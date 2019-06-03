package cn.soa.service.inter;

import java.util.List;

import cn.soa.entity.UnsafeType;

public interface UnsafeSI {
	
	/**   
	 * @Title: getList   
	 * @Description: 查询出所有不安全行为数据 
	 * @return: List<UnsafeType>   返回不安全行为数据列表   
	 */
	public List<UnsafeType> getList();
	
}
