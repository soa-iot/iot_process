package cn.zg.service.inter;

import java.util.List;

import cn.zg.entity.daoEntity.SimpleTreeData;

/**
 * @ClassName: PEServiceInter
 * @Description: 问题评估节点业务层接口
 * @author zhugang
 * @date 2018年11月9日
 */
public interface PEServiceInter {
	
	/**   
	 * @Title: findOrganByPid   
	 * @Description: 根据父节点，获取所有子节点  
	 * @param: @param pid
	 * @param: @return      
	 * @return: List<SimpleTreeData>        
	 */  
	public List<SimpleTreeData> findOrganByPid( String pid );
}
