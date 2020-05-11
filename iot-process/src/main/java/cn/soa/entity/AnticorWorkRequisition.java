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
 * 防腐（保温）作业申请单s 实体类
 * @author Hang
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class AnticorWorkRequisition implements Serializable {

	private static final long serialVersionUID = 1L;
	private String anticorWorkID;
	private String piid;
	private String applydept;
	private String applydate;
	private String applypeople;
	private String deptowner;
	private String requirement;
	private List<WorkTable> workTables;
	
}
