package cn.soa.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.activity.IdentityLinkMapper;
import cn.soa.entity.activity.IdentityLink;
import cn.soa.service.inter.AcitivityIdentitySI;
import cn.soa.service.inter.AcitivityRuntimeSI;
import cn.soa.service.inter.ActivitySI;

@Service
public class AcitivityRuntimeS implements AcitivityRuntimeSI{
	private static Logger logger = LoggerFactory.getLogger( AcitivityRuntimeS.class );
	
	@Autowired
	private ActivitySI activityS;
	
	/**   
     * @Title: judgePInstanceIsFinished   
     * @Description: 判断流程实例的状态，是否完成  
     * @return: Boolean        
     */  
    public Boolean judgePInstanceIsFinishedByPiid( String piid ) {
    	logger.info( "-S----判断流程实例的状态，是否完成 --------" );
    	if( StringUtils.isBlank( piid ) ) {
			logger.info( "---S--------任务piid为null-------------" );
			return null;
		}	
    	
    	try {
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	return false;
    }
}
