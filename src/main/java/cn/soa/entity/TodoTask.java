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
public class TodoTask implements Serializable {
	private String tsid ;
	private String piid ;
	private String dfid ;
	private String name ;
	private String tip ;
	private String describle ;
	private String area;
	private String currentnode ;
	private String reporttime ;
	private String reportperson ;
}	
