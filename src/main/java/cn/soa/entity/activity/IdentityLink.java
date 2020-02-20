package cn.soa.entity.activity;

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
public class IdentityLink implements Serializable {
	private String ID_;
	private String GROUP_ID_;
	private String TYPE_;
	private String USER_ID_;
	private String TASK_ID_;
	private String PROC_INST_ID_;
}
