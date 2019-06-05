package cn.soa.service.inter;

import cn.soa.entity.ProblemReportpho;

public interface ReportPhoSI {
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加一条问题图片数据
	 * @return: boolean   true-表示添加成功，false-表示添加失败   
	 */
	public boolean addOne(ProblemReportpho reprotPho);
	
	/**   
	 * @Title: updateTempPho   
	 * @Description: 更新暂存图片的流程实例PIID和问题上报流程任务表主键T_PROBLEM_REP_ID
	 * @return: boolean   true-表示添加成功，false-表示添加失败   
	 */
	public boolean updateTempPho(String tProblemRepId, String tempRepId, String piid, String[] imgList);
}
