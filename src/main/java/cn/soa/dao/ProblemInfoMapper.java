package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.ProblemInfo;

/**
 * 问题评估持久层接口
 * @author Bro.Lo
 *
 */
@Mapper
public interface ProblemInfoMapper {

	/**
	 * 根据流程标识字段查询问题评估信息
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	ProblemInfo findByPiid(String piid);
	
	//findByUsernum();
}
