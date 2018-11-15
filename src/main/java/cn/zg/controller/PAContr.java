package cn.zg.controller;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.activiti.engine.task.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.zg.entity.daoEntity.Emploee;
import cn.zg.entity.daoEntity.ProblemInspection;
import cn.zg.entity.dataExchange.ResultJson;
import cn.zg.service.impl.MechatronicsProcessServiceImpl;
import cn.zg.service.inter.PAServiceInter;
import cn.zg.service.inter.ProcessServiceInter;

@RestController
@RequestMapping( "/pureAssign" )
public class PAContr {
	private static Logger logger = LoggerFactory.getLogger( PAContr.class );
	
	@Autowired
	private PAServiceInter paService;
	
	@Autowired
	private ProcessServiceInter procService;
	
	@Autowired
	private MechatronicsProcessServiceImpl mechatService;	
	
	/**   
	 * @Title: getExecutorPA   
	 * @Description: 获取净化分配下一步-维修分配节点执行人 （维修技干）
	 * @param: @return      
	 * @return: ResultJson<ProblemInspection>        
	 */  
	@GetMapping( "/executor/problemRA" )
	public ResultJson<List<Emploee>> getExecutorPAContr(){
		logger.debug( "C-获取净化分配下一步-维修分配节点执行人 （维修技干）" );
		String roleName = "维修技干";
		List<Emploee> emploeeList =
				mechatService.getExecutorPAService( roleName );
		if( emploeeList == null ) {
			return new ResultJson<List<Emploee>>( 1, "查询组织不存在", null );
		}
		return new ResultJson<List<Emploee>>( 0, "查询成功", emploeeList );
	}
	
	/**   
	 * @Title: dealPAContr   
	 * @Description: 净化分配节点处理   
	 * @param: @param piid
	 * @param: @param nextExecutor
	 * @param: @param _comment
	 * @param: @param hiddenEvent
	 * @param: @return      
	 * @return: ResultJson<String>        
	 */  
	public ResultJson<String> dealPAContr(
			@PathVariable( "piid" ) String piid,
			@PathVariable( "nextExecutor" ) String nextExecutor,
			@RequestParam( "comment" ) String _comment,
			@RequestParam( "hiddenEvent" ) String hiddenEvent ){
		logger.debug( 
				"piid-nextExecutor-hiddenEvent:" + piid + "," + nextExecutor + "," + hiddenEvent);
		/*
		 * 根据piid找到当前taskId
		 */
		ProblemInspection inspection = paService.findInspeByPiidServ( piid );
		logger.debug( "C-inspection:" + inspection.toString() );
		
		/*
		 * 完成当前任务
		 */
		String taskId = inspection.getCurrentTsid().trim();
		if( inspection == null || taskId.isEmpty() ) {
			return new ResultJson<String>( 1, "流程的piid或taskid未找到", null );
		}
		try {
			procService.compeletTask( inspection.getCurrentTsid(), nextExecutor);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultJson<String>( 1, "完成本节点失败", null );
		}
			
		/*
		 * 保存处理信息
		 */
		Comment comment = procService.saveCommentService( taskId , _comment);
		if( comment == null ) {
			return new ResultJson<String>( 1, "节点下一步成功，添加备注信息失败", null );
		}
		return new ResultJson<String>( 0, "节点下一步成功，添加备注信息完成", "" );
	}
	
}
