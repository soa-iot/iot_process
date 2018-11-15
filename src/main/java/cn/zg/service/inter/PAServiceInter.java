package cn.zg.service.inter;

import java.util.List;
import java.util.Map;

import cn.zg.entity.daoEntity.ProblemInspection;

/**
 * @ClassName: PAServiceInter
 * @Description: 净化分配业务层接口
 * @author zhugang
 * @date 2018年11月14日
 */
public interface PAServiceInter {
	/**   
	 * @Title: getHisActByPiid   
	 * @Description: 根据业务id，获取该流程历史节点信息  
	 * @param: @param piid
	 * @param: @return      
	 * @return: List<Map<String,String>>        
	 */  
	public Map<String, String> getHisActByPiid( String piid );
	
	/**   
	 * @Title: findInspeByPiidServ   
	 * @Description: 根据  piid，查询taskId
	 * @param: @param piid
	 * @param: @return      
	 * @return: ProblemInspection        
	 */  
	public ProblemInspection findInspeByPiidServ( String piid );
}
