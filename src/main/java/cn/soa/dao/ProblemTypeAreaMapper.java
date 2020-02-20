package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.ProblemTypeArea;
import cn.soa.entity.RoleVO;

/**
 * 问题属地对应区域持久层接口
 * @author Jiang, Hang
 *
 */
@Mapper
public interface ProblemTypeAreaMapper{
	
	/**
	 * 查找问题属地对应区域
	 * @param  无
	 * @return List<ProblemTypeArea> 问题属地对应区域列表
	 */
	List<ProblemTypeArea> findAll();	
}
