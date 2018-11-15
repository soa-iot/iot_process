package cn.zg.entity.daoEntity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
public class SimpleTreeData implements Serializable{

	/**   
	 * @Fields serialVersionUID : 序列化  
	 */  
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String pid;
	
	private String name;
	
	private String open;
	
	private String isParent;

	public SimpleTreeData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SimpleTreeData(String id, String pid, String name, String open, String isParent) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.open = open;
		this.isParent = isParent;
	}

	/**  
	 * @Title:  getId <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	@Id
	public String getId() {
		return id;
	}

	/**  
	 * @Title:  getPid <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	@Column( name = "pid" )
	public String getPid() {
		return pid;
	}

	/**  
	 * @Title:  getName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	@Column( name = "name" )
	public String getName() {
		return name;
	}

	/**  
	 * @Title:  getOpen <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	@Column( name = "open" )
	public String getOpen() {
		return open;
	}

	/**  
	 * @Title:  getIsParent <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	@Column( name = "isParent" )
	public String getIsParent() {
		return isParent;
	}

	/**  
	 * @Title:  setId <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**  
	 * @Title:  setPid <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	/**  
	 * @Title:  setName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**  
	 * @Title:  setOpen <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setOpen(String open) {
		this.open = open;
	}

	/**  
	 * @Title:  setIsParent <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	/**   
	 * <p>Title: toString</p>   
	 * <p>Description: </p>   
	 * @return   
	 * @see java.lang.Object#toString()   
	 */ 
	@Override
	public String toString() {
		return "SimpleTreeData [id=" + id + ", pid=" + pid +
				", name=" + name + ", open=" + open + ", isParent="
				+ isParent + "]";
	}
	
	
}
