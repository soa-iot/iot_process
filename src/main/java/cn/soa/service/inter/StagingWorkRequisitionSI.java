package cn.soa.service.inter;


import cn.soa.entity.StagingWorkRequisition;

public interface StagingWorkRequisitionSI {
	
	/**
	 * 根据piid查找脚手架搭拆申请作业单
	 * @param piid 流程id
	 * @return StagingWorkRequisition 脚手架搭拆申请作业单
	 */
	StagingWorkRequisition findByPIID(String piid);
	
	/**
	 * 添加/修改脚手架搭拆申请作业单数据
	 * @param StagingWorkRequisition 脚手架搭拆申请作业单实例对象
	 * @return Integer 受影响行数
	 */
	Integer addOrUpdate(StagingWorkRequisition staging);
	
}
