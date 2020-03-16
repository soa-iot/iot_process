package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.AnticorWorkRequisition;
import cn.soa.entity.RoleVO;
import cn.soa.entity.StagingWorkRequisition;

/**
 * 脚手架搭拆申请作业单持久层接口
 * @author Jiang, Hang
 *
 */
@Mapper
public interface AnticorWorkRequisitionMapper {
	
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
