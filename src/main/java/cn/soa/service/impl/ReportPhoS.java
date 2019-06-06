package cn.soa.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.ProblemReportphoMapper;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemReportpho;
import cn.soa.service.inter.ReportPhoSI;


@Service
public class ReportPhoS implements ReportPhoSI {
	
	@Autowired
	private ProblemReportphoMapper phoMapper;
	
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
	
	/**   
	 * @Title: updateTempPho   
	 * @Description: 更新暂存图片的流程实例PIID和问题上报流程任务表主键T_PROBLEM_REP_ID
	 * @return: boolean   true-表示添加成功，false-表示添加失败   
	 */
	@Override
	public boolean updateTempPho(String tProblemRepId, String tempRepId, String piid, String[] imgList) {
		try {
			
			if(imgList != null && imgList.length > 0) {
				phoMapper.deleteList(imgList);
			}
			
			Integer rows = phoMapper.updateTempPho(tProblemRepId, tempRepId, piid);
			if(rows == 0) {
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}

}

