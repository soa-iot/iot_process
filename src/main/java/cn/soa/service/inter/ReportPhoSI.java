package cn.soa.service.inter;

import cn.soa.entity.ProblemReportpho;

public interface ReportPhoSI {
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加一条问题图片数据
	 * @return: boolean   true-表示添加成功，false-表示添加失败   
	 */
	public boolean addOne(ProblemReportpho reprotPho);
	

}
