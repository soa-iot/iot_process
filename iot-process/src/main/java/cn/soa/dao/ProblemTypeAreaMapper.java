package cn.soa.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.soa.entity.ProblemTypeArea;
import cn.soa.entity.RoleVO;

/**
 * 问题属地对应区域持久层接口
 * @author Jiang, Hang
 *
 */
@Mapper
public interface ProblemTypeAreaMapper{
	
	/**
	 * 查找问题属地对应区域
	 * @param  无
	 * @return List<ProblemTypeArea> 问题属地对应区域列表
	 */
	List<ProblemTypeArea> findAll();
	
	/**
	 * 查询问题完成情况统计
	 * @param startDate - 开始日期
	 * @param endDate - 截止日期
	 * @return
	 */
	List<Map<String,String>> findReportFinishRecords(
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 * 查询部门列表
	 * @return
	 */
	List<String> findDepartments();
}
