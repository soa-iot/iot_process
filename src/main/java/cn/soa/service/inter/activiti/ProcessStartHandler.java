package cn.soa.service.inter.activiti;

public interface ProcessStartHandler {
	
	/**   
	 * @Title: before   
	 * @Description:  流程启动之前的流程启动逻辑处理 
	 * @return: boolean        
	 */  
	public boolean before() ;
	
	/**   
	 * @Title: before   
	 * @Description:  流程启动之后的流程启动逻辑处理 
	 * @return: boolean        
	 */  
	public boolean after( String piid) ;
}
