package cn.soa.entity;

import java.io.Serializable;
import java.util.List;

import lombok.NoArgsConstructor;

/**
 * @ClassName: UnsafeType
 * @Description: 不安全行为实体类-不安全行为类型
 * @author jianghang
 * @date 2019年5月29日
 *
 */

@NoArgsConstructor
public class UnsafeType implements Serializable {

	/**   
	 * @Fields serialVersionUID : 序列化   
	 */  
	private static final long serialVersionUID = 1L;
	
	private String typesID;
	private String typesName;
	private Integer typesCode;
	private List<UnsafeAction> unsafeAction;
	
	public String getTypesID() {
		return typesID;
	}
	
	public void setTypesID(String typesID) {
		this.typesID = typesID;
	}
	
	public String getTypesName() {
		return typesName;
	}
	
	public void setTypesName(String typesName) {
		this.typesName = typesName;
	}
	
	public Integer getTypesCode() {
		return typesCode;
	}
	
	public void setTypesCode(Integer typesCode) {
		this.typesCode = typesCode;
	}
	
	public List<UnsafeAction> getUnsafeAction() {
		return unsafeAction;
	}

	public void setUnsafeAction(List<UnsafeAction> unsafeAction) {
		this.unsafeAction = unsafeAction;
	}

	@Override
	public String toString() {
		return "UnsafeType [typesID=" + typesID + ", typesName=" + typesName + ", typesCode=" + typesCode
				+ ", unsafeAction=" + unsafeAction + "]";
	}
	
}
