package cn.soa.entity.bo;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import cn.soa.entity.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings( "serial" )
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors( chain=true )
@Validated
public class MQIdempotent implements Serializable {
	private String mqid;
	private String className;
	private String describe;
	private String key;
	private String note;
	private String backup1;
	private String backup2;
}
