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
	 * 根据流程标识字段更新问题问题描述字段
	 * @param piid 流程标识字段
	 * @return 数据库更新数量
	 */
	@Override
	public Integer changeProblemDescribeByPiid(String piid, String problemdescribe) {
		
		return updateProblemDescribeByPiid(piid, problemdescribe);
	}

	/**   
	 * @Title: ModifyEstiByPiid   
	 * @Description: 更新一条问题评估
	 * @return: Integer  受影响行数      
	 */ 
	public Integer ModifyEstiByPiid(ProblemInfo info) {
		return updateEstiByPiid(info);
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

	/**
	 * 持久层方法实现
	 * 
	 * 根据流程标识字段更新问题问题描述字段
	 * @param piid 流程标识字段
	 * @return 数据库更新数量
	 */
	public Integer updateProblemDescribeByPiid(String piid,String problemdescribe) {
		try {
			Integer rows = problemInfoMapper.updateProblemDescribeByPiid(piid, problemdescribe);
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**   
	 * @Title: updateEstiByPiid   
	 * @Description: 更新一条问题评估实现方法
	 * @return: Integer  受影响行数      
	 */ 
	private Integer updateEstiByPiid(ProblemInfo info) {
		
		try {
			Integer row = problemInfoMapper.updateEstiByPiid(info);
			return row;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
