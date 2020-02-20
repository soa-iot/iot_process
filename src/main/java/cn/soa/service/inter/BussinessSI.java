package cn.soa.service.inter;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.soa.entity.ProblemInfo;


@Service
public interface BussinessSI {
	
	/**   
	 * @Title: dealProblemReport   
	 * @Description: 流程第一个节点，执行业务处理接口（传入参数是前端问题上报的表单信息）
	 * @return: 返回业务主键id       
	 */  
	public String dealProblemReport( ProblemInfo problemInfo );
	
	public String nextNode( String nodeid, Map<String,Object> bussiness );
}
