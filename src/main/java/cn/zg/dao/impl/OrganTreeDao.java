package cn.zg.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.zg.entity.daoEntity.SimpleTreeData;

@Repository
@Transactional
public class OrganTreeDao {
	private static Logger logger = LoggerFactory.getLogger( OrganTreeDao.class );
	
	@Autowired
	//@PersistenceContext
	EntityManager em;
	
	/**   
	 * @Title: findByParentID   
	 * @Description: 根据父节点，获取所有子节点    
	 * @param: @param parentId
	 * @param: @return      
	 * @return: List<SimpleTreeData>        
	 */  
	public List<SimpleTreeData> findByParentID( String parentId ){
		String sql = " select u.* "
				+ "	   from (    "
				+ "   	 SELECT ORG_ID as id,ORG_PARENTID as pid, ORG_NAME as name, "
				+ "        'true' as open,'true' as isParent"
				+ "      FROM process_organization "
				+ "		 WHERE FIND_IN_SET(ORG_PARENTID,queryChildrenOrgan('" + 
						 	parentId +  "')) ) u";	
		Session session = em.unwrap( org.hibernate.Session.class );
	    NativeQuery query = session.createNativeQuery( sql );
	    query.addScalar( "id", StandardBasicTypes.STRING );
	    query.addScalar( "pid", StandardBasicTypes.STRING );
	    query.addScalar( "name", StandardBasicTypes.STRING );
	    query.addScalar( "open", StandardBasicTypes.STRING );
	    query.addScalar( "isParent", StandardBasicTypes.STRING );
	    query.unwrap( NativeQueryImpl.class )
	    	.setResultTransformer( Transformers.aliasToBean( SimpleTreeData.class ) );
	    em.close();
	    return query.getResultList();
	}
	
	/**   
	 * @Title: findByPids   
	 * @Description: 根据父节点s,查找具体人员 
	 * @param: @param pidStr
	 * @param: @return      
	 * @return: List<SimpleTreeData>        
	 */  
	public List<SimpleTreeData> findByPids( String pidStr ){
		String sql = " select eo.EMP_ID as id, eo.ORG_ID as pid,e.EMP_NAME as name , "
				+ "	     'false' as open,'false' as isParent    "
				+ "    from process_employee_organization eo "
				+ "       LEFT JOIN process_employee e on eo.EMP_ID = e.EMP_ID "
				+ "     where eo.ORG_ID in (" + pidStr + ")";	
		Session session = em.unwrap( org.hibernate.Session.class );
	    NativeQuery query = session.createNativeQuery( sql );
	    query.addScalar( "id", StandardBasicTypes.STRING );
	    query.addScalar( "pid", StandardBasicTypes.STRING );
	    query.addScalar( "name", StandardBasicTypes.STRING );
	    query.addScalar( "open", StandardBasicTypes.STRING );
	    query.addScalar( "isParent", StandardBasicTypes.STRING );
	    query.unwrap( NativeQueryImpl.class )
	    	.setResultTransformer( Transformers.aliasToBean( SimpleTreeData.class ) );
	    em.close();
	    return query.getResultList();
	}
}
