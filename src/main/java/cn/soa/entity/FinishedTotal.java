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
public class FinishedTotal implements Serializable {
	private String depet ;
	private String parent_id;
	private int depets ;
	private int finished ;
	private int normalfinished ;
	private int directfinished ;
	private int unfinished ;
}	
