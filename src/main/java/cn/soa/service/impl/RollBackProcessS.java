package cn.soa.service.impl;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.soa.dao.IdempotentMapper;
import cn.soa.dao.MonitorMapper;
import cn.soa.entity.Monitor;
import cn.soa.entity.TodoTask;
import cn.soa.entity.User;
import cn.soa.entity.bo.MQIdempotent;
import cn.soa.service.inter.RollBackProcessInter;
import cn.soa.utils.JavaUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RollBackProcessS
 * @Description: 删除人员后的流程回退服务
 * @author zhugang
 * @date 
 */
@Service
@Slf4j
public class RollBackProcessS implements RollBackProcessInter{
	
	@Autowired
    private ActivityS activityS;
	
	@Autowired
    private MonitorMapper monitorMapper;
	
	@Value("${process.rollback.url}")
	private String savePath;
	
	@Autowired
    private IdempotentMapper idempotentMapper;
	
	
	/**   
	 * @Title: rollBackByUserid   
	 * @Description:   删除人员后的该人员的任务流程回退 
	 * @return: void        
	 */  
	@Override
	public boolean rollBackByUserid( User user) {	
		log.info("---S--user-" + user);
		
		//检查
		if( check(user) ) {;
			log.info("---S--删除人员后的该人员的任务流程回退 -消息重复");
			return false;
		}
		
		//查找所有的代办任务
		List<TodoTask> unfinishedTasks = activityS.getAllTasksByUsername(user.getName());
		log.info("---S--unfinishedTasks-" + unfinishedTasks);
			
		//task过滤(规则：1、两个人执行的不会退； 2、一个人执行的+上一个节点执行人只有他)
		
		
		//执行回退
		List<String> piids = new ArrayList<String>();
		unfinishedTasks.forEach(t ->{
			piids.add(t.getPiid());
		});
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("comment", "因员工 (" + user.getName() + ")离职，检维修流程自动回退，请您重新处理该流程 ");
		try {			
			excuteRollBack(piids, map, user);
		} catch (Exception e) {
			e.printStackTrace();	
			return false;
		}
		return true;	
	}
	
	/**   
	 * @Title: check   
	 * @Description:  检查等 
	 * @return: boolean        
	 */  
	boolean check( User user) {
		String idempotent = user.getRemark2();
		int i = idempotentMapper.countByKey(idempotent);
		return i>0 ? true:false;
	}
	
	
	/**   
	 * @Title: excuteRollBack   
	 * @Description: 循环回退  
	 * @return: void        
	 */  
	@Transactional
	public void excuteRollBack( List<String> piids, 
			Map<String,Object> map,  User user ) {	
		List<String> errorPiids = new ArrayList<String>();
		map.put("operateName", "离职回退");
		
		//保存等
		if (!saveIdempotent(user)) {
			//回退失败的流程，记录
			try {
				saveRollBackErrorProcess( piids, user);
			} catch (Exception e2) {
				e2.printStackTrace();
				log.info("保存等失败,piids:");
				piids.forEach(System.out::println);
			}
			throw new RuntimeException("流程回退任务未执行，保存等失败");
		}
		
		//循环回退
		piids.forEach(piid ->{
			try {
				activityS.backToBeforeNodeByPiidInGroup(piid, map);
			} catch (Exception e) {
				e.printStackTrace();
				errorPiids.add(piid);
			}
		});
		
		//回退失败的流程，记录
		if( errorPiids.size()>0) {
			try {
				saveRollBackErrorProcess( piids, user);
			} catch (Exception e2) {
				e2.printStackTrace();
				log.info("记录回退失败的/流程失败,piid" + piids);
				piids.forEach(System.out::println);
			}	
		}			
	}
	
	/**   
	 * @Title: saveIdempotent   
	 * @Description: 保存等  
	 * @return: boolean        
	 */  
	boolean saveIdempotent(User user) {
		try {
			MQIdempotent m = new MQIdempotent();
			m.setClassName("RollBackProcessS");
			m.setDescribe("员工离职后，该员工检维修流程中的待办任务自动回退时检查");
			m.setKey(user.getRemark2());
			idempotentMapper.insert(m);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
		
	/**   
	 * @Title: saveRollBackErrorProcess   
	 * @Description: 记录 回退失败的流程
	 * @return: void        
	 */  
	public void saveRollBackErrorProcess( List<String> piids,  User user) {		
		List<Monitor> monitors = new ArrayList<Monitor>();
		piids.forEach(piid -> {
			Monitor m = new Monitor();
			m.setPiid(piid);
			m.setType(1);
			m.setDescribe("删除离厂员工("+ user.getName() +")后，回退该员工的待办流程任务失败");
			m.setRecordTime(new Date());
			m.setOperator("系统自动(" + user.getName() +")");
			m.setRule(1);
			m.setState(1);
			monitors.add(m);
		});
		log.info("删除人员后流程回退失败记录：" + monitors);
		
		//保存回退失败流程到数据库
		List<Monitor> monitorLefts = new ArrayList<Monitor>();
		monitors.forEach(monitor ->{
			try {
				monitorMapper.insertAll(monitor);
			} catch (Exception e) {
				e.printStackTrace();
				monitorLefts.add(monitor);
			}
			
		});
		
		//数据库保存失败，则保存失败流程到本地文件
		if(monitorLefts.size()>0) {
			saveInLocal( monitorLefts, user);
		}
		
	}
	
 
	/**   
	 * @Title: saveInLocal   
	 * @Description: 保存失败流程到本地文件  
	 * @return: boolean        
	 */  
	boolean saveInLocal( List<Monitor> monitorLefts, User user) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
		String fileName = "离职回退失败-"+ user.getName() + "-" + sdf.format(new Date())  + ".txt";
		try {
			JavaUtils.saveObjectByJson(savePath, fileName, monitorLefts);
			return true;
		} catch (FileNotFoundException e) {
			log.info("数据库保存失败，保存失败流程到本地文件失败！！！");
			e.printStackTrace();
			return false;
		}	
	}
		
}
