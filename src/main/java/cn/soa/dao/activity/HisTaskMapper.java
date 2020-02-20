package cn.soa.dao.activity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.activity.HistoryTask;
import cn.soa.entity.activity.IdentityLink;

@Mapper
public interface HisTaskMapper {
	
	/**   
	 * @Title: findAllHisTasksBypiid   
	 * @Description: 根据任务piid,查询当前流程实例的所有节点（包括完成和未完成）  
	 * @return: List<HistoryTask>        
	 */  
	List<HistoryTask> findAllHisTasksBypiid( String piid );
}
