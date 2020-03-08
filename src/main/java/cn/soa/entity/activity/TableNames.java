package cn.soa.entity.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings( "serial" )
@AllArgsConstructor
@Data
@Accessors( chain=true )
public class TableNames implements Serializable{
	
	private String id;
	
	private final List<String> tablenames = new ArrayList<String>();
	
	private final List<String> sequences = new ArrayList<String>();
	
	private String remark1;

	public TableNames() {
		tablenames.add( "ACT_GE_BYTEARRAY" );
		tablenames.add( "ACT_GE_PROPERTY" );
		tablenames.add( "ACT_HI_ACTINST" );
		tablenames.add( "ACT_HI_ATTACHMENT" );
		tablenames.add( "ACT_HI_COMMENT" );
		tablenames.add( "ACT_HI_DETAIL" );
		tablenames.add( "ACT_HI_IDENTITYLINK" );
		tablenames.add( "ACT_ID_USER" );
		tablenames.add( "ACT_ID_MEMBERSHIP" );
		tablenames.add( "ACT_ID_INFO" );
		tablenames.add( "ACT_ID_GROUP" );
		tablenames.add( "ACT_HI_VARINST" );
		tablenames.add( "ACT_HI_TASKINST" );
		tablenames.add( "ACT_HI_PROCINST" );
		tablenames.add( "ACT_RU_VARIABLE" );
		tablenames.add( "ACT_RU_TASK" );
		tablenames.add( "ACT_RU_JOB" );
		tablenames.add( "ACT_RU_IDENTITYLINK" );
		tablenames.add( "ACT_RU_EXECUTION" );
		tablenames.add( "ACT_RU_EVENT_SUBSCR" );
		tablenames.add( "ACT_RE_PROCDEF" );
		tablenames.add( "ACT_RE_MODEL" );
		tablenames.add( "ACT_RE_DEPLOYMENT" );
		tablenames.add( "ACT_PROCDEF_INFO" );
		
		sequences.add( "" );
	}
	
	
}
