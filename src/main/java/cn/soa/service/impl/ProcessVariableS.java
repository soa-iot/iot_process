package cn.soa.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.entity.ProblemInfo;
import cn.soa.service.inter.ProcessVariableSI;

@Service
public class ProcessVariableS implements ProcessVariableSI {
	private static Logger logger = LoggerFactory.getLogger( ProcessVariableS.class );

	/**   
	 * <p>Title: addVarsStartProcess</p>   
	 * <p>Description: </p> 流程启动节点进行流程变量的处理  
	 * @param problemInfo
	 * @return   
	 * @see cn.soa.service.inter.ProcessVariableSI#addVarsStartProcess(cn.soa.entity.ProblemInfo)   
	 */ 
	@Override
	public Map<String, Object> addVarsStartProcess( ProblemInfo problemInfo ) {
		Map<String, Object> vars = new HashMap<String,Object>();
		if( StringUtils.isBlank( problemInfo.getProblemtype() ) ) {
			logger.debug( "---------流程启动节点进行流程变量-属地单位的处理----------------  " );
			logger.debug( "---------属地单位--------------  " + problemInfo.getProblemtype() );
			return null;
		}
		if( StringUtils.isBlank( problemInfo.getApplypeople() ) ) {
			logger.debug( "---------流程启动节点进行流程变量-问题上报人的处理----------------  " );
			logger.debug( "---------上报人员--------------  " + problemInfo.getApplypeople() );
			return null;
		}
		
		vars.put( "area", problemInfo.getProblemtype().trim() );
		vars.put( "reporter", problemInfo.getApplypeople().trim() );
		return vars;
	}

}
