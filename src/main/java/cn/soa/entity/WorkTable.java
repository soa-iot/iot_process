package cn.soa.entity;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class WorkTable implements Serializable {

	private static final long serialVersionUID = 1L;
	private String tableNo;
	private String tableContent;
	private String tableVersion;
	private String tableNumber;
	private String tableComment;
	
}
