package cn.soa.entity.activity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings( "serial" )
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors( chain=true )
public class HistoryAct implements Serializable {
	private String ID_;
	private String PROC_DEF_ID_;
	private String PROC_INST_ID_;
	private String EXECUTION_ID_;
	private String ACT_ID_;
	private String TASK_ID_;
	private String CALL_PROC_INST_ID_;
	private String ACT_NAME_;
	private String ACT_TYPE_;
	private String ASSIGNEE_;
	private Date START_TIME_;
	private Date END_TIME_;
	private String DURATION_;
	private String TENANT_ID_;
}
