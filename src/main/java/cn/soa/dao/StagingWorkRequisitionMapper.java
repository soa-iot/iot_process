package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.RoleVO;
import cn.soa.entity.StagingWorkRequisition;

/**
 * 脚手架搭拆申请作业单持久层接口
 * @author Jiang, Hang
 *
 */
@Mapper
public interface StagingWorkRequisitionMapper {
	
	/**
	 * 根据piid查找脚手架搭拆申请作业单
	 * @param piid 流程id
	 * @return StagingWorkRequisition 脚手架搭拆申请作业单
	 */
	StagingWorkRequisition findByPIID(String piid);
	
	/**
	 * 插入脚手架搭拆申请作业单数据
	 * @param StagingWorkRequisition 脚手架搭拆申请作业单实例对象
	 * @return Integer 受影响行数
	 */
	Integer insertOne(StagingWorkRequisition staging);
	
	/**
	 * 更新脚手架搭拆申请作业单数据
	 * @param StagingWorkRequisition 脚手架搭拆申请作业单实例对象
	 * @return Integer 受影响行数
	 */
	Integer updateOne(StagingWorkRequisition staging);
	
	
}
