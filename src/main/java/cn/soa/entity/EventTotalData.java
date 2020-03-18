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
public class EventTotalData implements Serializable {
	private String depet ;
	private Integer ticket_no ;
	private String problemclass ;
	private int coun;
}