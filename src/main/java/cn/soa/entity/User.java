package cn.soa.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings( "serial" )
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors( chain=true )
public class User  implements Serializable{

	private static final long serialVersionUID = 1L;

	//主键
	private String orgid;
	
	private String name;
	
	private String parent_id;
	
	private String usernum;
	
	private String user_password;
	
	private Integer is_parent; 
	
	//用户状态(1-在职；2-离职；)
	private Integer state;
	private String note;
	private Integer remark1;
	
	//分布式事务id
	private String remark2;
}

