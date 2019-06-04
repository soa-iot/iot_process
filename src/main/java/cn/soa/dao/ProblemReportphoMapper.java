package cn.soa.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.ProblemReportpho;

/**
 * 图片信息持久层接口
 * @author Bro.Lo
 *
 */
@Mapper
public interface ProblemReportphoMapper {

		/**
		 * 根据流程标识字段查询图片信息
		 * @param piid 流程标识字段
		 * @return 图片信息实体
		 */
	List<ProblemReportpho> findByPiid(String piid);
	
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
