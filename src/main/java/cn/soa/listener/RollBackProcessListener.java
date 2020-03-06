/**  
 * @Title: RollBackProcessListener.java
 * @Package cn.soa.listener
 * @Description: TODO(用一句话描述该文件做什么)
 * @author zhugang
 * @date 2020年3月3日
 * @version V1.0  
 */
package cn.soa.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.soa.entity.User;
import cn.soa.service.impl.RollBackProcessS;
import cn.soa.service.inter.RollBackProcessInter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RollBackProcessListener
 * @Description: 回滚流程监听
 * @author zhugang
 * @date 2020年3月3日
 */
@Component
@RocketMQMessageListener(topic = "process-back",consumerGroup = "process-back-receive")
@Slf4j
public class RollBackProcessListener implements RocketMQListener<String>{

	@Autowired
	private RollBackProcessInter rollBackProcessS;
	
	@Override
	public void onMessage(String s) {
		final JSONObject jsonObject = JSON.parseObject(s);
		User user = JSONObject.parseObject(jsonObject.getString("user"),User.class);
		log.info("---MQ监听---获取数据--" + user.toString());
		
		//回滚流程
		rollBackProcessS.rollBackByUserid(user);
	 }
}
