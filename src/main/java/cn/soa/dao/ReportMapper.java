package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;

@Mapper
public interface ReportMapper {
	
	/**   
	 * @Title: findByResavepeople   
	 * @Description: 根据当前登录人查找问题报告  
	 * @return: ProblemInfo        
	 */ 
	public ProblemInfoVO findByResavepeople(String resavepeople);
	
	/**   
	 * @Title: findByRepId
	 * @Description: 根据问题报告主键id查找问题报告  
	 * @return: ProblemInfo        
	 */ 
	public ProblemInfo findByRepId(String RepId);
	
	/**   
	 * @Title: insertOne   
	 * @Description: 添加一条问题报告数据 
	 * @return: Integer        
	 */ 
	public Integer insertOne(ProblemInfo info);
	
	/**   
	 * @Title: updateOne   
	 * @Description: 更新一条问题报告数据 
	 * @return: Integer        
	 */ 
	public Integer updateOne(ProblemInfo info);
	
}
