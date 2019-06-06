package cn.soa.service.inter;

import org.springframework.stereotype.Service;

import cn.soa.entity.ProblemInfo;

/**
 * 问题评估业务逻辑层接口
 * @author Bru.Lo
 *
 */
@Service
public interface ProblemInfoSI {

	/**
	 * 根据流程标识字段查询问题评估信息
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	ProblemInfo getByPiid(String piid);
	
	/**
	 * 根据流程标识字段更新问题问题描述字段
	 * @param piid 流程标识字段
	 * @return 数据库更新数量
	 */
	Integer changeProblemDescribeByPiid(String piid,String problemdescribe);
	
	/**   
	 * @Title: ModifyEstiByPiid   
	 * @Description: 更新一条问题评估
	 * @return: Integer  受影响行数      
	 */ 
	Integer ModifyEstiByPiid(ProblemInfo info);
	
}
