package cn.soa.service.inter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.soa.entity.ProblemInfo;
import cn.soa.entity.ProblemInfoVO;
import cn.soa.entity.ProblemTypeArea;

public interface ReportSI {
	
	/**   
	 * @Title: addOne   
	 * @Description: 添加一条问题报告数据
	 * @return: String   生成的主键id   
	 */
	public String addOne(ProblemInfo problemInfo, String[] imgList);
	
	/**   
	 * @Title: getByResavepeopleOrPiid   
	 * @Description: 根据当前登录用户或piid查找问题报告数据
	 * @return: ProblemInfoVO  查到的问题报告数据 
	 */
	public ProblemInfoVO getByResavepeopleOrPiid(String resavepeople, String piid);
	
	/**   
	 * @Title: verifyApplyPeople   
	 * @Description: 校验问题上报申请人
	 * @return: String  验证结果
	 */
	public String verifyApplyPeople(String[] userList);
	
	/**   
	 * @Title: getProblemInfoByPage   
	 * @Description: 根据条件分页查询出问题上报信息列表
	 * @return: ProblemInfo  查到的问题报告数据列表 
	 */
	public List<ProblemInfo> getProblemInfoByPage(ProblemInfo problemInfo, Integer page,
			Integer limit, String startTime, String endTime, String sortField, String sortType,String equiType);
	
	/**   
	 * @Title: ProblemCount   
	 * @Description: 根据条件查询出问题上报信息的条数
	 * @return: Integer   查询数据的条数
	 */
	public Map<String, Object> ProblemCount(ProblemInfo problemInfo, String startTime, String endTime,String equiType);
	
	/**
	 * 查找问题属地对应区域
	 * @param  无
	 * @return List<ProblemTypeArea> 问题属地对应区域列表
	 */
	List<ProblemTypeArea> findProblemTypeArea();
	
	/**   
	 * 根据问题上报主键id删除问题上报记录   
	 * @param repid 问题上报主键
	 * @return: int 删除数据的条数
	 */
	Integer deleteByReportid(String repid);
	
	/**
	 * 读取excel表批量问题上报
	 */
	String massProblemReport(InputStream is, String fileName, String resavepeople, String depet);
	
	/**   
	 * @Title: deleteProblemInfo   
	 * @Description: 删除问题上报记录
	 * @return: int        
	 */
	Boolean deleteProblemInfo(String tProblemRepId, String deleteComment, String piid, String resavepeople, boolean isFinished);
	
	/**
	 * 查询问题完成情况统计
	 * @param startDate - 开始日期
	 * @param endDate - 截止日期
	 * @return
	 */
	List<Map<String,String>> getReportFinishRecords(String startDate, String endDate);
}
