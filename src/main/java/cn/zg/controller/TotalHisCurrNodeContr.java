package cn.zg.controller;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.zg.entity.dataExchange.ResultJson;
import cn.zg.service.impl.MechatronicsProcessServiceImpl;
import cn.zg.service.inter.PAServiceInter;

@RestController
@RequestMapping( "/totalHisCurrNode" )
public class TotalHisCurrNodeContr {
private static Logger logger = LoggerFactory.getLogger( PAContr.class );
	
	@Autowired
	private PAServiceInter paService;
	
	@Autowired
	private MechatronicsProcessServiceImpl mechatService;	
	
	/**   
	 * @Title: getHisActByPiid   
	 * @Description: 根据业务id，获取该流程历史节点信息     
	 * @param: @param piid
	 * @param: @return      
	 * @return: ResultJson<List<Map<String,String>>>        
	 */  
	@GetMapping( "/processNode/id/name/{piid}" )
	public ResultJson<Map<String, String>>  getHisActByPiid( 
			@PathVariable( "piid") @NotBlank String piid ){
		logger.debug( "piid:" + piid );
		Map<String, String> map = paService.getHisActByPiid( piid );
		if( map.size() < 0 ) {
			return new ResultJson<Map<String, String>>( 1, "请求数据为空", map );
		}
		return new ResultJson<Map<String, String>>( 0, "请求数据成功", map );
	}
}
