package cn.soa.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.EquipmentInfoMapper;
import cn.soa.dao.ProblemInfoMapper;
import cn.soa.dao.activity.HisActMapper;
import cn.soa.entity.EventTotal;
import cn.soa.entity.EventTotalData;
import cn.soa.entity.FinishedTotal;
import cn.soa.entity.ProblemInfo;
import cn.soa.entity.UserOrganization;
import cn.soa.entity.activity.HistoryAct;
import cn.soa.service.inter.ProblemInfoSI;

/**
 * 问题评估业务逻辑层实现类
 * 
 * @author Bru.Lo
 *
 */
@Service
public class ProblemInfoS implements ProblemInfoSI {
	private static Logger logger = LoggerFactory.getLogger( ProblemInfoS.class );

	@Autowired
	private ProblemInfoMapper problemInfoMapper;
	
	@Autowired
	private HisActMapper hisActMapper;
	
	@Autowired
	private EquipmentInfoMapper equipMapper;
	
	/**
	 * lixuefeng:新增接口用于问题查询，多个维度，可分页
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	@Override
	public List<ProblemInfo> queryProblempro(ProblemInfo problemInfo, Integer page, Integer pageSize,String startTime,String endTime) {
		return problemInfoMapper.queryProblempro(problemInfo, page, pageSize,startTime,endTime);
	}
	@Override
	public int count(ProblemInfo problemInfo,String startTime,String endTime) {
		return problemInfoMapper.count(problemInfo,startTime,endTime);
	}
	/**
	 * 根据流程标识字段查询问题评估信息
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	public List<Map<String ,Object>> statisticalTaskProblempro(String beginTime,String endTime){
		
		return problemInfoMapper.statisticalTaskProblempro(beginTime, endTime);
		
		
	};
	
	/**   
	 * <p>Title: statisticalTaskProblempro</p>   
	 * <p>查看问题超期数量  </p>  
	 * @param beginTime
	 * @param endTime
	 * @return   
	 * @see cn.soa.service.inter.ProblemInfoSI#statisticalTaskProblempro(java.lang.String, java.lang.String)   
	 */ 
	@Override
	public List<Map<String ,Object>> findTimeStateS(String beginTime,String endTime){
		return problemInfoMapper.findTimeState(beginTime, endTime);
		
		
	};
	/**
	 * 根据流程标识字段查询问题评估信息
	 * 
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	@Override
	public ProblemInfo getByPiid(String piid) {
		return findByPiid(piid);

	}

	/**
	 * 根据流程标识字段更新问题问题描述字段
	 * @param piid 流程标识字段
	 * @return 数据库更新数量
	 */
	@Override
	public Integer changeProblemDescribeByPiid(ProblemInfo problemInfo) {
		
		return updateProblemDescribeByPiid(problemInfo, null, null, null);
	}

	/**   
	 * @Title: ModifyEstiByPiid   
	 * @Description: 更新一条问题评估
	 * @return: Integer  受影响行数      
	 */ 
	public Integer ModifyEstiByPiid(ProblemInfo info) {
		return updateEstiByPiid(info);
	}
	
	
	/**
	 * 持久层方法实现发
	 * 
	 * @param piid 流程标识字段
	 * @return 问题评估信息实体
	 */
	private ProblemInfo findByPiid(String piid) {
		try {
			ProblemInfo problemInfo = problemInfoMapper.findByPiid(piid);
			return problemInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 持久层方法实现
	 * 
	 * 根据流程标识字段更新问题问题描述字段
	 * @param piid 流程标识字段
	 * @return 数据库更新数量
	 */
	public Integer updateProblemDescribeByPiid(ProblemInfo problemInfo, String equipName, String positionNum, String serialNum) {
		try {
			Integer rows = problemInfoMapper.updateProblemDescribeByPiid(problemInfo);
			if(problemInfo.getSupervisorydate() != null) {
				equipMapper.insertRepairInfo(problemInfo, equipName, positionNum, serialNum);
			}

			return rows;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 根据属地名称去找另外的属地
	 * @param problemtype
	 * @return
	 */
	public List<UserOrganization> getDeptByProblemtype(String problemtype){
		
		List<UserOrganization> list = findDeptByProblemtype(problemtype);
		String parentId = null;
		
		//如果是维修工段，则去掉净化工段和其子节点
		if ("维修工段".equals(problemtype)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("净化工段")) {
					parentId=list.get(i).getUsernum();
					list.remove(i);
				}
			}
		}
		if (parentId != null) {
			List<UserOrganization> list2 = new ArrayList<>();
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getParent_id().equals(parentId)) {
					list2.add(list.get(j));
				}
			}
			System.err.println(list2);
			list.removeAll(list2);
		}
		return list;
	}
	
	/**
	 * 根据用部分problemdescribe查询对应的数据
	 * @param problemdescribele
	 * @return ProblemInfo集合
	 */
	public List<ProblemInfo> getAutoFill(String problemdescribe){
		return findByProblemdescribe(problemdescribe);
	}
	
	/**
	 * 根据用部分problemdescribe查询对应的数据
	 * @param problemdescribele
	 * @return ProblemInfo集合
	 */
	private List<ProblemInfo> findByProblemdescribe(String problemdescribe){
		return problemInfoMapper.findByProblemdescribe(problemdescribe);
	}
	
	/**   
	 * @Title: updateEstiByPiid   
	 * @Description: 更新一条问题评估实现方法
	 * @return: Integer  受影响行数      
	 */ 
	private Integer updateEstiByPiid(ProblemInfo info) {
		
		try {
			Integer row = problemInfoMapper.updateEstiByPiid(info);
			return row;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 根据属地名称去找另外的属地
	 * @param problemtype
	 * @return
	 */
	private List<UserOrganization> findDeptByProblemtype(String problemtype){
		try {
			List<UserOrganization> userOrganizations = problemInfoMapper.findDeptByProblemtype(problemtype);
			return userOrganizations;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	/**   
	 * @Title: deleteByPiid   
	 * @Description: 根据piid删除问题上报记录 
	 * @return: int        
	 */  
	@Override
	public int deleteByPiid( String piid ) {
		try {
			int i = problemInfoMapper.deleteByPiid(piid);
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**   
	 * @Title: deleteByPiid   
	 * @Description: 根据tsid删除问题上报记录 
	 * @return: int        
	 */  
	@Override
	public int deleteByBsid( String bsid ) {
		try {
			int i = problemInfoMapper.deleteByBsid( bsid );
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**   
	 * @Title: updatePiidByBsid   
	 * @Description: 根据bsid，更新业务表数据的piid    
	 * @return: int        
	 */ 
	@Override
	public int updatePiidByBsid( String bsid, String biid ) {
		try {
			int i = problemInfoMapper.updatePiidByBsid( bsid, biid );
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**   
	 * @Title: modifyProblemState   
	 * @Description: 修改问题是否超期的状态  
	 * @return: boolean        
	 */  
	public boolean modifyNodeCurrentState() {
		logger.info( "---------修改问题是否超期的状态  ------------" );
		try {
			/*
			 * 查找所有未完成的任务和未超期的任务
			 */
			List<ProblemInfo> problems = problemInfoMapper.findUnfinishedAndTimeover();
			logger.info( "---------需要修改超期的状态的问题-----------"  + problems );
			
			if( problems == null ) return false;
			logger.info( "---------需要修改超期的状态的问题数  ------------" );
			logger.info( problems.size() + "" );
			
			/*
			 * 查找流程历史节点表，得到超期状态
			 */
			Map<String, String> timeState = new HashMap<String,String>();
			List<String> piids = new ArrayList<String>();
			for( ProblemInfo p : problems ) {
				if( p == null ) continue;
				if( p.toString() == null ) continue;
				piids.add( p.getPiid() );
			}
			logger.info( "---------需要修改超期的状态的问题的piid  ------------" );
			piids.forEach( p-> logger.info( p.toString() ));
			List<HistoryAct> lastActs = getLastActByPiidsS( piids );
			//判断状态
			for( HistoryAct h : lastActs ) {
				if( "endEvent".equals( h.getACT_TYPE_())  ) {
					timeState.put( h.getPROC_INST_ID_(), " "); 
					continue;
				}else if( h.getEND_TIME_() == null  ){
					Date startTime = h.getSTART_TIME_();
					//判断标准为2天
					long standard = 2*24*60*60*1000;
					long start = startTime.getTime();
					if( start - System.currentTimeMillis() > standard ) {
						timeState.put( h.getPROC_INST_ID_(), "超期"); 
					}else {
						timeState.put( h.getPROC_INST_ID_(), "未超期"); 
					}
					logger.info( "---------所有流程的超期状态  ------------" + timeState );
				}else {
					logger.info( "---------获取节点结束时间有误  ------------" + h.getEND_TIME_() );
				}				
			}			
			
			//修改超期状态
			for( Entry<String,String> e : timeState.entrySet() )  {
				String state = e.getKey();
				String piid = e.getValue();
				try {
					int i = problemInfoMapper.updateTimeoverState( state, piid );
					if( i <= 0 ) logger.info( "------更新失败，piid为  ------------" + piid );
				} catch (Exception e2) {
					e2.printStackTrace();
					continue;
				}			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**   
	 * @Title: getLastActByPiids   
	 * @Description: 根据任务piid,查询当前流程实例的最后一个节点    
	 * @return: List<HistoryAct>        
	 */  
	public List<HistoryAct> getLastActByPiidsS( List<String> piids ){
		logger.info( "--S-------根据任务piid,查询当前流程实例的最后一个节点  --------" );
		try {
			List<HistoryAct> lastActs = hisActMapper.findLastActByPiids(piids);
			logger.info( "--S-------查询当前流程实例的最后一个节点  --------" + lastActs );
			if( lastActs == null ) return null;
			lastActs.forEach( l-> logger.info( "--S------------" + l.toString() ));	
			return lastActs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**   
	 * @Title: modifyProblemState   
	 * @Description: 查询所有问题状态为‘未超期’和未完成的的问题  
	 * @return: boolean        
	 */  
	@Override
	public boolean modifyProblemState() {
		try {
			logger.info( "--------- 查询所有问题状态为‘未超期’和未完成的的问题  ------------" );
			/*
			 * 查找所有未完成的任务和未超期的任务
			 */
			List<ProblemInfo> problems = problemInfoMapper.findUnfinishedAndTimeover();
			logger.info( "---------需要检查超期的状态的问题-----------"  + problems );
			
			if( problems == null ) return false;
			logger.info( "---------需要检查超期的状态的问题数  ------------" );
			logger.info( problems.size() + "" );
			

			/*
			 * 循环判断得到超期状态
			 */
			Map<String, String> timeState = new HashMap<String,String>();
			for( ProblemInfo p : problems ) {
				try {
					if( p.getRemark() == null ) continue;
					if( "指定日期".equals( p.getRemark() ) ) {
						Date requireDate = p.getRectificationperiod();
						if( requireDate.getTime() + 1000*24*60*60 > System.currentTimeMillis() ) {
							timeState.put( p.getPiid(), "未超期" );
						}else {
							timeState.put( p.getPiid(), "超期" );
						}
					}else {
						timeState.put( p.getPiid(), "未超期" );
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			
			//修改超期状态
			for( Entry<String,String> e : timeState.entrySet() )  {
				String piid = e.getKey();
				String  state= e.getValue();
				try {
					int i = problemInfoMapper.updateTimeoverState( state, piid );
					if( i <= 0 ) {
						logger.info( "------更新失败，piid为  ------------" + piid );					
					}else {
						logger.info( "------更新成功，piid为  ------------" + piid );	
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					continue;
				}			
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public List<ProblemInfo> findUnfinishAndNoPositionS() {
		try {
			List<ProblemInfo> ps = problemInfoMapper.selectUnfinishAndNoPosition();
			return ps;
		} catch (Exception e2) {
			e2.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * 事故事件情况统计
	 * @param date
	 * @return
	 */
	@Override
	public List<EventTotal> findEventByApplydate(String date,String startTime,String endTime){
		try {
			List<EventTotalData> eventTotalDatas = problemInfoMapper.findEventByApplydate(date,startTime,endTime);
			List<EventTotal> eventTotals = new ArrayList<EventTotal>();
			
			 List<String> eventTotalNames = problemInfoMapper.getDepet();
			for (int i = 0; i < eventTotalNames.size(); i++) {
				EventTotal eventTotal = new EventTotal();
				eventTotal.setDepet(eventTotalNames.get(i));
				eventTotals.add(eventTotal);
			}
			
			for (int i = 0; i < eventTotals.size(); i++) {
				
				for (int j = 0; j < eventTotalDatas.size(); j++) {
					
					EventTotal eventTotal = eventTotals.get(i);
					EventTotalData eventTotalData = eventTotalDatas.get(j);
					
					int ticket_no = eventTotalData.getTicket_no();
					String problemclass = eventTotalData.getProblemclass();
					int con = eventTotalData.getCoun();
					
					if (eventTotal.getDepet().equals(eventTotalData.getDepet())) {
						
						eventTotal.setTotal(eventTotal.getTotal() + con);
						if (ticket_no == 0) {
							if ("其他".equals(problemclass)) {
								eventTotal.setOrdinaryevent(con);
							}else {
								eventTotal.setOrdinaryeventUnsafebehavior(con);
							}
						}else if (ticket_no == 1) {
							if ("其他".equals(problemclass)) {
								eventTotal.setAccidentevent(con);
							}else {
								eventTotal.setAccidenteventUnsafebehavior(con);
							}
						}else if (ticket_no == 2) {
							if ("其他".equals(problemclass)) {
								eventTotal.setRisksevent(con);
							}else {
								eventTotal.setRiskseventUnsafebehavior(con);
							}
						}
					}
					
				}
			}
			
			EventTotal eventTotal = null;
			
			for (int i = 0; i < eventTotals.size(); i++) {
				for (int j = i; j < eventTotals.size(); j++) {
					eventTotal = eventTotals.get(i);
					if (eventTotals.get(i).getTotal() < eventTotals.get(j).getTotal()) {
						eventTotals.set(i, eventTotals.get(j));
						eventTotals.set(j, eventTotal);
					}
				}
			}

			return eventTotals;
		} catch (Exception e2) {
			e2.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 问题完成情况统计
	 * @param date
	 * @return
	 */
	public List<FinishedTotal> findFinishedByApplydate(String date,String startTime,String endTime){
		try {
			List<FinishedTotal> finishedTotalData = problemInfoMapper.findFinishedByApplydate(date,startTime,endTime);
			
			
			 List<String> finishedTotalNames = problemInfoMapper.getDepet();
			List<FinishedTotal> finishedTotales =new ArrayList<FinishedTotal>();
			for (int i = 0; i < finishedTotalNames.size(); i++) {
				FinishedTotal finishedTotal = new FinishedTotal();
				finishedTotal.setDepet(finishedTotalNames.get(i));
				finishedTotales.add(finishedTotal);
			}
			
			for (int i = 0; i < finishedTotales.size(); i++) {
				for (int j = 0; j < finishedTotalData.size(); j++) {
					if (finishedTotalData.get(j).getDepet().equals(finishedTotales.get(i).getDepet())) {
						finishedTotales.set(i, finishedTotalData.get(j));
					}
				}
			}
			
			 FinishedTotal eventTotal = null;
			for (int i = 0; i < finishedTotales.size(); i++) {
				for (int j = i; j < finishedTotales.size(); j++) {
					eventTotal = finishedTotales.get(i);
					if (finishedTotales.get(i).getDepets() < finishedTotales.get(j).getDepets()) {
						finishedTotales.set(i, finishedTotales.get(j));
						finishedTotales.set(j, eventTotal);
					}
				}
			}
			return finishedTotales;
		} catch (Exception e2) {
			e2.printStackTrace();
			return null;
		}
	};
}
