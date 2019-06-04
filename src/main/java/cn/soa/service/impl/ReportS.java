package cn.soa.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.soa.dao.ProblemInfoMapper;
import cn.soa.dao.ProblemReportphoMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemReportpho;
import cn.soa.service.inter.ReportSI;


@Service
public class ReportS implements ReportSI {
	
	@Autowired
	private ProblemInfoMapper reportMapper;
	@Autowired
	private ProblemReportphoMapper phoMapper;
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加/更新一条问题报告数据
	 * @return: void   无返回值   
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public boolean addOne(ProblemInfo problemInfo, String[] imgList) {
		
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
				//获取最大编号，再自动+1
				Integer maxNum = reportMapper.findMaxProblemNum();
				maxNum = (maxNum == null ? 1 : maxNum+1);
				problemInfo.setProblemnum(maxNum);
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
				Integer rows1 = reportMapper.updateOne(problemInfo);
				
				if(imgList != null && imgList.length > 0) {
					@SuppressWarnings("rawtypes")
					Map phoMap = new HashMap();
					phoMap.put("tProblemRepId", RepId);
					phoMap.put("imgList", imgList);
					phoMapper.deleteList(phoMap);
				}
				if(rows1 != 1) {
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
