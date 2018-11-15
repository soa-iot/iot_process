package cn.zg.entity.daoEntity;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * @ClassName: executorTreeNode
 * @Description: 人员组织结构-净化分配执行人实体类
 * @author zhugang
 * @date 2018年11月7日
 */

public class executorTreeNode implements Serializable {

	/**   
	 * @Fields serialVersionUID : 序列化 
	 */  
	private static final long serialVersionUID = 1L;
	
	private String key;
	
	private String excutorName;	

	public executorTreeNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public executorTreeNode(  String key, String excutorName) {
		super(); 
		this.key = key; 
		this.excutorName = excutorName;
	}

	/**  
	 * @Title:  getKey <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getKey() {
		return key;
	}

	/**  
	 * @Title:  getExcutorName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getExcutorName() {
		return excutorName;
	}

	/**  
	 * @Title:  setKey <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setKey( String key ) {
		this.key = key;
	}

	/**  
	 * @Title:  setExcutorName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setExcutorName(String excutorName) {
		this.excutorName = excutorName;
	}

	/**   
	 * <p>Title: toString</p>   
	 * <p>Description: </p>   
	 * @return   
	 * @see java.lang.Object#toString()   
	 */ 
	@Override
	public String toString() {
		return "executorTreeNode [key=" + key + ", excutorName=" + excutorName + "]";
	}

	
	
	
}
