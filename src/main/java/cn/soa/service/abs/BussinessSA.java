package cn.soa.service.abs;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @ClassName: BussinessS
 * @Description: 流程节点业务处理抽象类
 * @author zhugang
 * @date 2019年5月20日
 */
public abstract class BussinessSA {
	
	public abstract boolean node1Bussiness( Map<String,Object> map );
	
}
