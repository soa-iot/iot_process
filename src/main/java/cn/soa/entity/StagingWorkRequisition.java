package cn.soa.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 脚手架搭拆申请作业单 实体类
 * @author Hang
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class StagingWorkRequisition implements Serializable {

	private static final long serialVersionUID = 1L;
	private String stagingWorkID;
	private String piid;
	private String applydept;
	private String applydate;
	private String stagingLocation;
	private String applypeople;
	private String deptowner;
	private String workdescription;
	private String tips;
	private String action;
	private String caculator;
	private String workAmount;
	private String stagingComment;
	
}
