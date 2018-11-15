package cn.zg.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zg.dao.impl.OrganTreeDao;
import cn.zg.entity.daoEntity.SimpleTreeData;
import cn.zg.service.inter.PEServiceInter;

/**
 * @ClassName: PEServiceImpl
 * @Description: 问题评估节点业务层实现
 * @author zhugang
 * @date 2018年11月9日
 */
@Service
public class PEServiceImpl implements PEServiceInter {
	private static Logger logger = LoggerFactory.getLogger( PEServiceImpl.class );
	
	@Autowired
	private OrganTreeDao organTreeDao;
	
	/**   
	 * @Title: findOrganByPid   
	 * @Description: 根据父节点，获取所有子节点  
	 * @param: @param pid
	 * @param: @return      
	 * @return: List<SimpleTreeData>        
	 */  
	public List<SimpleTreeData> findOrganByPid( String pid ){
		/*
		 * 获取所有非子节点
		 */
		List<SimpleTreeData> simpleTreeDatas = organTreeDao.findByParentID( pid );
		
		/*
		 * 获取所有子节点
		 */
		String pids = "";
		for( SimpleTreeData s : simpleTreeDatas) {
			pids = pids + "'" + s.getId() + "'" + ",";
		}
		if( pids.length() < 0 ) {
			return new ArrayList<SimpleTreeData>();
		}
		pids = pids.substring( 0, pids.length() - 1 );
		logger.debug( "C-根据父节点，获取所有子节点 -pids:" + pids );
		List<SimpleTreeData> simpleTreeDatasTemp = organTreeDao.findByPids( pids );
		if( simpleTreeDatasTemp != null && simpleTreeDatasTemp.size() > 0) {
			simpleTreeDatas.addAll( simpleTreeDatasTemp );
		}
		logger.debug( "C-根据父节点，获取所有子节点 -simpleTreeDatas:" + simpleTreeDatas.toString() );
		return simpleTreeDatas;
	}
}
