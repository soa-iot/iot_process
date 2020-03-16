package cn.soa.service.inter;


import cn.soa.entity.AnticorWorkRequisition;

public interface AnticorWorkRequisitionSI {
	
	/**
	 * 根据piid查找防腐保温申请作业单
	 * @param piid 流程id
	 * @return AnticorWorkRequisition 防腐保温申请作业单
	 */
	AnticorWorkRequisition findByPIID(String piid);
	
	/**
	 * 插入防腐保温申请作业单
	 * @param AnticorWorkRequisition 防腐保温申请作业单实例对象
	 * @return Integer 受影响行数
	 */
	Integer insertOne(AnticorWorkRequisition anticor);
	
	/**
	 * 删除防腐保温申请作业单数据
	 * @param piid 流程id
	 * @return Integer 受影响行数
	 */
	Integer deleteByPiid(String piid);
	
}
