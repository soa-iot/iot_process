package cn.soa.dao.activity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.soa.entity.activity.HistoryAct;
import cn.soa.entity.activity.HistoryTask;
import cn.soa.entity.activity.IdentityLink;

@Mapper
public interface HisActMapper {
	
	/**   
	 * @Title: findAllHisTasksBypiid   
	 * @Description: 根据任务piid,查询当前流程实例的所有节点（包括完成和未完成）  
	 * @return: List<HistoryTask>        
	 */  
	List<HistoryAct> findAllHisActsBypiid( String piid );
	
	/**   
	 * @Title: findLastActByPiids   
	 * @Description: 根据任务piid,查询当前流程实例的最后一个节点  
	 * @return: List<HistoryAct>        
	 */  
	List<HistoryAct> findLastActByPiids(@Param("piids") List<String> piids );
	
	/**   
	 * @Title: updateOprateName   
	 * @Description: 给每个节点增加操作名称
	 * @return: int        
	 */  
	int updateOprateName(@Param("piid") String piid, @Param("tsid") String tsid, @Param("operateName") String operateName);
}
