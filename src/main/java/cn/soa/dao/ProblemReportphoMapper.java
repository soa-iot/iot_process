package cn.soa.dao;

import java.util.List;

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
	}
