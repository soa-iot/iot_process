package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;


@Mapper
public interface ReportMapper {
	
	/**   
	 * @Title: findByResavepeople   
	 * @Description: 根据当前登录人查找问题报告  
	 * @return: ProblemInfo 问题报告对象    
	 */ 
	public ProblemInfoVO findByResavepeople(String resavepeople);
	
	/**   
	 * @Title: findByRepId
	 * @Description: 根据问题报告主键id查找问题报告  
	 * @return: ProblemInfo  问题报告对象       
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
	
}
