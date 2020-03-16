package cn.soa.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	List<ProblemReportpho> findByPiid(@Param("piid")String piid,@Param("remark")String remark);
	
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
	public Integer deleteList(String[] imgList);
	
	/**   
	 * @Title: updateTempPho   
	 * @Description: 更新暂存图片的流程实例PIID和问题上报流程任务表主键T_PROBLEM_REP_ID
	 * @return: Integer        
	 */ 
	public Integer updateTempPho(
			@Param("tProblemRepId") String tProblemRepId,
			@Param("tempRepId") String tempRepId, 
			@Param("piid") String piid);
}
