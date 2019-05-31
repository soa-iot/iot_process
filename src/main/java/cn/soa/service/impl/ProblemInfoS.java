package cn.soa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.ProblemInfoMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.service.inter.ProblemInfoSI;

/**
 * 问题评估业务逻辑层实现类
 * 
 * @author Bru.Lo
 *
 */
@Service
public class ProblemInfoS implements ProblemInfoSI {

	@Autowired
	private ProblemInfoMapper problemInfoMapper;

	/**
	 * 根据流程标识字段查询问题评估信息
	 * 
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	@Override
	public ProblemInfo getByPiid(String piid) {
		return findByPiid(piid);

	}

	/**
	 * 持久层方法实现发
	 * 
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	private ProblemInfo findByPiid(String piid) {
		try {
			ProblemInfo problemInfo = problemInfoMapper.findByPiid(piid);
			return problemInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
