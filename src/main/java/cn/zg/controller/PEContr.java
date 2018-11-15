package cn.zg.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.zg.entity.daoEntity.SimpleTreeData;
import cn.zg.entity.dataExchange.ResultJson;
import cn.zg.service.impl.MechatronicsProcessServiceImpl;
import cn.zg.service.inter.PEServiceInter;
import cn.zg.service.inter.ProcessServiceInter;

@RestController
@RequestMapping( "/problemEstimate" )
public class PEContr {
	private static Logger logger = LoggerFactory.getLogger( PEContr.class );
	
	@Autowired
	PEServiceInter peService;

	@Autowired
	private ProcessServiceInter processService;
	
	/**   
	 * @Title: findOrgan   
	 * @Description: 根据父节点，获取所有子节点   
	 * @param: @param pid
	 * @param: @return      
	 * @return: ResultJson<SimpleTreeData>        
	 */  
	@GetMapping( "/exector/{parentId}" )
	public ResultJson<List<SimpleTreeData>> findOrgan( 
			@PathVariable( "parentId" ) @NotBlank String pid ){
		logger.debug( "C-根据父节点，获取所有子节点  -pid:" + pid );
		List<SimpleTreeData> simpleTreeDatas = peService.findOrganByPid( pid );
		if( simpleTreeDatas == null ) {
			return new ResultJson<List<SimpleTreeData>>( 1, "查询失败,未知错误", null );
		}		
		return new ResultJson<List<SimpleTreeData>>( 0, "查询成功", simpleTreeDatas );
	}
	
	/**   
	 * @Title: transCanTaskToPerContr   
	 * @Description: 转办组任务  
	 * @param: @param userName
	 * @param: @param taskId
	 * @param: @return      
	 * @return: ResultJson<String>        
	 */  
	@PostMapping( "/exector/{transferName}" )
	public ResultJson<String> transCanTaskToPerContr(
			@PathVariable( "transferName" ) @NotBlank String userName,
			@RequestParam( "currentTsid" ) @NotBlank String taskId ){
		logger.debug( "C-转办组任务 ……userName-taskId：" + userName + "-" + taskId  );
		processService.transCanTaskToPer( taskId,  userName );
		return new ResultJson<String>( 0, "转办任务成功", "11");
	}
}
