package cn.soa.service.inter.activiti;

import cn.soa.entity.ProblemInfo;

public interface ProcessStartHandler {
	
	/**   
	 * @Title: before   
	 * @Description:  流程启动之前的流程启动逻辑处理 
	 * @return: boolean        
	 */  
	public boolean before( String bsid, ProblemInfo problemInfo ) ;
	
	/**   
	 * @Title: before   
	 * @Description:  流程启动之后的流程启动逻辑处理 
	 * @return: boolean        
	 */  
	public boolean after( String bsid, String piid, ProblemInfo problemInfo) ;
}
