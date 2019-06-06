package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;

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

	/**   
	 * @Title: findByResavepeople   
	 * @Description: 根据当前登录人查找问题报告  
	 * @return: ProblemInfo 问题报告对象    
	 */ 
	public ProblemInfoVO findByResavepeople(String resavepeople);
	
	/**   
	 * @Title: findByRepId
	 * @Description: 根据问题报告主键id查找暂存状态的问题报告  
	 * @return: ProblemInfo 暂存状态的问题报告对象       
	 */ 
	public ProblemInfo findByRepId(String RepId);
	
	/**   
	 * @Title: findMaxProblemNum
	 * @Description: 获取最大问题编号
	 * @return: Integer 问题编号
	 */ 
	public Integer findMaxProblemNum();
	
	
	/**   
	 * @Title: insertOne   
	 * @Description: 添加一条问题报告数据 
	 * @return: Integer  受影响行数
	 */ 
	public Integer insertOne(ProblemInfo info);
	
	/**   
	 * @Title: updateOne   
	 * @Description: 更新一条问题报告数据 
	 * @return: Integer  受影响行数      
	 */ 
	public Integer updateOne(ProblemInfo info);
	
	/**   
	 * @Title: updateEstiByPiid   
	 * @Description: 更新一条问题评估
	 * @return: Integer  受影响行数      
	 */ 
	public Integer updateEstiByPiid(ProblemInfo info);
}
