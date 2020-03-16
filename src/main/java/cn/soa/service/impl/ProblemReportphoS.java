package cn.soa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.ProblemReportphoMapper;
import cn.soa.entity.ProblemReportpho;
import cn.soa.service.inter.ProblemReportphoSI;

/**
 * 图片信息业务逻辑层实现类
 * 
 * @author Bru.Lo
 *
 */
@Service
public class ProblemReportphoS implements ProblemReportphoSI {

	@Autowired
	private ProblemReportphoMapper problemReportphoMapper;

	/**
	 * 根据流程标识字段查询问题图片信息
	 * 
	 * @param piid 流程标识字段
	 * @return 图片信息实体
	 */
	@Override
	public List<ProblemReportpho> getByPiid(String piid,String remark) {
		return findByPiid(piid,remark);

	}

	/**
	 * 持久层方法实现发
	 * 
	 * @param piid 流程标识字段
	 * @return 图片信息实体
	 */
	private List<ProblemReportpho> findByPiid(String piid,String remark) {
		try {
			 List<ProblemReportpho> problemReportphos = problemReportphoMapper.findByPiid(piid,remark);
			return problemReportphos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
