package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemReportpho;

@Mapper
public interface ReportPhoMapper {
	
	/**   
	 * @Title: findByRepId   
	 * @Description: 根据问题报告主键id查找问题图片 
	 * @return: ProblemReportpho        
	 */ 
	public ProblemReportpho findByRepId(String RepId);
	
	/**   
	 * @Title: insertOne   
	 * @Description: 添加一条问题图片数据 
	 * @return: Integer        
	 */ 
	public Integer insertOne(ProblemReportpho pho);
	
}
