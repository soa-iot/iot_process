package cn.soa.service.inter;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.soa.entity.EquipmentInfo;
import cn.soa.entity.ProblemInfo;

public interface EquipmentInfoSI {
	
	/**   
	 * @Title: getEquipmentInfo   
	 * @Description: 根据条件查找出设备信息数据
	 * @return: List<EquipmentInfo> 返回设备数据列表   
	 */
	public List<EquipmentInfo> getEquipmentInfo(EquipmentInfo info, Integer page, Integer limit);
	
	
	/**   
	 * @Title: countEquipmentInfo   
	 * @Description: 根据条件查统计设备信息条数
	 * @return: List<EquipmentInfo> 返回条数   
	 */
	public Integer countEquipmentInfo(EquipmentInfo info);
	
	/**
	 * 根据条件查询下拉选数据
	 * @param info
	 * @return
	 */
	List<String> findSelectWelUnit(EquipmentInfo info);
	List<String> findSelectSecondclassEquipment(EquipmentInfo info);
	List<String> findSelectEquMemoOne(EquipmentInfo info);

}
