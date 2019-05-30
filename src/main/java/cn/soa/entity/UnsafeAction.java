package cn.soa.entity;

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * @ClassName: UnsafeAction
 * @Description: 不安全行为实体类-具体行为
 * @author jianghang
 * @date 2019年5月29日
 *
 */

@NoArgsConstructor
public class UnsafeAction implements Serializable {

	private static final long serialVersionUID = 1L;

	private String actionsID;
	private String actionsName;
	private Integer actionsCode;
	
	public String getActionsID() {
		return actionsID;
	}
	
	public void setActionsID(String actionsID) {
		this.actionsID = actionsID;
	}
	
	public String getActionsName() {
		return actionsName;
	}
	
	public void setActionsName(String actionsName) {
		this.actionsName = actionsName;
	}
	
	public Integer getActionsCode() {
		return actionsCode;
	}
	
	public void setActionsCode(Integer actionsCode) {
		this.actionsCode = actionsCode;
	}

	@Override
	public String toString() {
		return "UnsafeAction [actionsID=" + actionsID + ", actionsName=" + actionsName + ", actionsCode=" + actionsCode
				+ "]";
	}
	
}
