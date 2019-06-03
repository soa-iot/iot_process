package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	
	/**
	 * 根据流程标识字段更新问题问题描述字段
	 * @param piid 流程标识字段
	 * @return 数据库更新数量
	 */
	Integer updateProblemDescribeByPiid(@Param("piid")String piid,@Param("problemdescribe")String problemdescribe);
}
