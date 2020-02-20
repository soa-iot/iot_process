package cn.soa.service.impl.activity;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.soa.entity.activity.IdentityLink;
import cn.soa.service.inter.AcitivityIdentitySI;
import cn.soa.service.inter.ActivitySI;
import cn.soa.service.inter.ProblemInfoSI;
import cn.soa.utils.SpringUtils;

/**
 * @ClassName: FindCheckor
 * @Description: 确定作业验收的执行人
 * @author zhugang
 * @date 2019年6月14日
 */
public class FindCheckor  implements ExecutionListener{
	private static Logger logger = LoggerFactory.getLogger( FindCheckor.class );

	private List<String> nodesName = new ArrayList<String>();
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.info( "---------确定作业验收的执行人-----------" );
		/*
		try {
			String piid = execution.getProcessInstanceId();
			
			logger.info( "---------piid-----------" + piid );
			ActivitySI activityS = SpringUtils.getObject(ActivitySI.class);
			List<HistoricTaskInstance> hisTasks = activityS.getHisTaskNodesByPiid(piid);
			
			//初始化
			nodesName.add( "问题评估" );
			nodesName.add( "净化分配" );
			nodesName.add( "维修分配" );
			
			if( hisTasks != null && hisTasks.size() > 0 ) {
				String executor = "";
				for( int i = hisTasks.size() -1 ; i >= 0; i-- ) {
					HistoricTaskInstance t = hisTasks.get(i);
					if( t != null && t.getName() != null && nodesName.contains( t.getName().toString().trim() ) ) {
						executor = "";
						String currentNodeName = t.getName().trim();
						switch ( currentNodeName ) {
							case "问题评估":
								executor = (String) execution.getVariable( "estimators" );
								break;
							case "净化分配":
								executor = (String) execution.getVariable( "puror" );
								break;
							case "维修分配":
								executor = (String) execution.getVariable( "repairor" );
								break;
						}
						
						logger.info( "---------executor-----------" + executor );
						if( StringUtils.isNotBlank(executor) ) {
							execution.setVariable( "checkor", executor);
							logger.info( "---------确定作业验收的执行人成功-----------" );
							break;
						}else {
							logger.info( "---------确定作业验收的执行人失败-----------" );
							break;
						}					
					}				
				}				
			}else {
				logger.info( "---------历史查询为空，确定作业验收的执行人失败-----------" );
			}					
		} catch (Exception e) {
			e.printStackTrace();
			logger.info( "---------确定作业验收的执行人失败-----------" );
		}
		*/
		
		try {
			String piid = execution.getProcessInstanceId();			
			logger.info( "---------piid-----------" + piid );
			
			
			//确定问题评估节点处理人作为问题验收的处理人
			String executor1 = "";
			ActivitySI activityS = SpringUtils.getObject(ActivitySI.class);
			AcitivityIdentitySI acitivityIdentityS = SpringUtils.getObject(AcitivityIdentitySI.class);
			List<HistoricTaskInstance> hisTasks = activityS.getHisTaskNodesByPiid(piid);
			if( hisTasks != null && hisTasks.size() > 0 ) {		
				for( int i = 0 ; i < hisTasks.size() ; i++ ) {
					HistoricTaskInstance t = hisTasks.get(i);
					if( t != null && t.getName() != null && "问题评估".contains( t.getName().toString().trim() ) ) {
						executor1 = "";
						String tsid = t.getId();
						logger.info( "---------问题评估节点tsid-----------" + tsid );
						List<IdentityLink> identitys = acitivityIdentityS.findCandidateByTsid(tsid);
						for( IdentityLink l : identitys ) {
							executor1 = executor1 + "," + l.getUSER_ID_() ;
						}
						if( executor1.length() > 1 ) {
							executor1 = executor1.substring( 1 );
						}
						
						logger.info( "---------问题评估潜在处理人-----------" + executor1 );
						break;
					}else {	
						logger.info( "---------HistoricTaskInstance、节点名称为空或null-----------" );
					}
				}
			}else {
				logger.info( "---------历史节点获取为空或null-----------" );
			}
			
			//动态根据属地确认净化或者维修分配的处理人作为问题验收的处理人
			String executor2 = "";
			String area = (String) execution.getVariable( "area" );		
			logger.info( "---------area-----------" + area );
			switch ( area ) {
				case "净化工段" :
				case "分析" :
					nodesName.add( "净化分配" );
					break;
				case "电工" :
				case "仪表" :
				case "机械" :
				case "电站" :
				case "维修工段" :
					nodesName.add( "维修分配" );
					break;
			}
			
			if( nodesName != null && nodesName.size() >= 1) {
				for( int i = 0 ; i < hisTasks.size() ; i++ ) {
					HistoricTaskInstance t = hisTasks.get(i);
					if( t != null && t.getName() != null && nodesName.contains( t.getName().toString().trim() ) ) {
						executor2 = executor2 + "," + t.getAssignee();	
						logger.info( "---------当前HistoricTaskInstance、节点名称-----------" + t.toString() );
					}else {
					}
				}
				if( StringUtils.isNotBlank( executor2) ) { 
					executor2 = executor2.substring( 1 );
				}			
				logger.info( "---------净化分配或维修分配处理人-----------" + executor2 );
			}
						
			
			//确定问题验收节点处理人
			String executor = "";
			if(  StringUtils.isNotBlank( executor1) && StringUtils.isNotBlank( executor2) ) {
				executor = executor1 + "," + executor2;
			}else if( StringUtils.isNotBlank( executor1) && StringUtils.isBlank( executor2)){
				executor = executor1;
			}else if( StringUtils.isBlank( executor1) && StringUtils.isNotBlank( executor2)){
				executor = executor2;
			}
			
			if( StringUtils.isNotBlank( executor) ) {
				execution.setVariable( "checkor", executor);
				logger.info( "---------确定作业验收的执行人成功-----------" + executor );
			}else {
				logger.info( "---------确定作业验收的执行人失败-----------" + executor  );
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info( "---------确定作业验收的执行人失败-----------" );
		}
	}

}
