package cn.soa.entity;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

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
public class Config implements Serializable {
	private String vcid ;
	private String varname ;
	private String varvalue ;
	private String nametype ;
	private String valuetype ;
	private String note ;
	private String remark1 ;
	private String remark2 ;
}	
