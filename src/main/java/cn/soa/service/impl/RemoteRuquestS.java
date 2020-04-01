package cn.soa.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.soa.entity.ResultJson;
import cn.soa.service.inter.RemoteRuquestSI;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RemoteRuquestS
 * @Description: 远程调用服务类
 * @author zhugang
 * @date 2020年3月23日
 */
@Slf4j
@Service
public class RemoteRuquestS implements RemoteRuquestSI{
	
	@Autowired
	private RestTemplate restTemplate; 
	
	/**   
	 * @Title: requestOpcPositionsByUrl   
	 * @Description: 远程请求获取数据
	 * @return: List<String>    结果转换模板：List<String> urls= (List<String>) r.getData();    
	 * 后续编写工具服务，get方式、post方式单独写
	 */  
	@SuppressWarnings("unchecked")
	@Override
	public Object remoteRequestByUrl( String url, Map<String,Object> param){
		log.info("根据url={},获取opc点位数据", url);
		/*
		 * url检查
		 */
		check( url);
		if( !url.contains("http://") ) {
			url = "http://" + url;
		}
		 
		/*
		 * 请求数据
		 */
		ResponseEntity<ResultJson> organ = restTemplate.postForEntity(url, param, ResultJson.class,param);
		ResultJson r = organ.getBody();
		log.info( r.getState()+"");
		log.info( r.getData().toString());
		if( r.getState() != 200 || r.getData() == null ) {
			
			log.error("url:{},请求数据失败，message={}", r.getMessage());
			throw new RuntimeException("url:"+ url +",请求数据失败,message=" + r.getMessage());
		}		
		Object results=  r.getData();  
		log.info("url:{url},请求数据成功", url);
		return results;
	}
	
	/**   
	 * @Title: check   
	 * @Description: url检查  
	 * @return: boolean        
	 */  
	private boolean check( String url ) {
		if( StringUtils.isBlank(url) ) throw new RuntimeException("参数的请求链接url为空或null");		
		return true;
	}
}
