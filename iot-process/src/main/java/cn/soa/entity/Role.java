package cn.soa.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String rolID;
	private String name;
	private Integer state;
	private Date createTime;
	private Date lastModifyTime;
	private String note;
	private String remark1;
	private String remark2;
	
}
