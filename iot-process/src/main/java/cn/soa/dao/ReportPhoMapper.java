package cn.soa.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	
	/**   
	 * @Title: deleteList   
	 * @Description: 删除问题图片数据 
	 * @return: Integer        
	 */ 
	public Integer deleteList(Map phos);
	
}
