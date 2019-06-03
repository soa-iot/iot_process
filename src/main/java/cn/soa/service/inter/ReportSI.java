package cn.soa.service.inter;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;

public interface ReportSI {
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加一条问题报告数据
	 * @return: void   无返回值   
	 */
	public boolean addOne(ProblemInfo problemInfo);
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加一条问题报告数据
	 * @return: void   无返回值   
	 */
	public ProblemInfoVO getByResavepeople(String resavepeople);
}
