package cn.soa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.ReportPhoMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemReportpho;
import cn.soa.service.inter.ReportPhoSI;

@Service
public class ReportPhoS implements ReportPhoSI {
	
	@Autowired
	private ReportPhoMapper phoMapper;
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加一条问题图片数据
	 * @return: boolean   true-表示添加成功，false-表示添加失败   
	 */
	@Override
	public boolean addOne(ProblemReportpho reportPho) {
		
		try {
			Integer rows = phoMapper.insertOne(reportPho);
			if(rows != 1) {
				return false;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
