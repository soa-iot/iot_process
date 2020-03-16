package cn.soa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 问题上报实体VO类
 * 
 * @author Jiang,Hang
 *
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class ProblemInfoForExcel implements Serializable {
	
	private String applypeople;
	private String problemtype;
	private String welName;
	private String profession;
	private String rfid;
	private String problemclass;
	private String remarkfive;
	private String remarksix;
	private String problemdescribe;
	private Date applydate;
	private String dept;
	private String problemstate;
}

