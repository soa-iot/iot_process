package cn.soa.entity;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private String reporttime ;
	private String reportperson ;
}	
