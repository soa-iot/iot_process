package cn.soa.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.soa.dao.ReportMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.service.inter.ReportSI;
@Service
public class ReportS implements ReportSI {
	
	@Autowired
	private ReportMapper reportMapper;
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加/更新一条问题报告数据
	 * @return: void   无返回值   
	 */
	@Transactional
	@Override
	public boolean addOne(ProblemInfo problemInfo) {
		
		String RepId = problemInfo.getTProblemRepId();
		/*
		 * 先判断该问题报告数据是否已存在
		 * 存在就update，不存在则insert
		 */
		ProblemInfo result = reportMapper.findByRepId(RepId);
		problemInfo.setApplydate(new Date());
		if(result == null) {
			problemInfo.setProcesstype("7");
			
			try {
				Integer rows = reportMapper.insertOne(problemInfo);
				if(rows != 1) {
					return false;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return false;
			}	
		}else {
			try {
				Integer rows = reportMapper.updateOne(problemInfo);
				if(rows != 1) {
					return false;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	/**   
	 * @Title: getByResavepeople   
	 * @Description: 根据当前登录用户查找问题报告数据
	 * @return: ProblemInfo  查到的问题报告数据 
	 */
	@Override
	public ProblemInfoVO getByResavepeople(String resavepeople) {
		
		return reportMapper.findByResavepeople(resavepeople);
	}
}
