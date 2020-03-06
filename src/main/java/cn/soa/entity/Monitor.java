package cn.soa.entity;

import java.util.Date;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: Monitor
 * @Description: 维护监控实体类
 * @author zhugang
 * @date 2020年2月23日
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class Monitor {
	private String mid;
	private String piid;
	private Integer type; 
	private String describe;
	private Date recordTime;
	private String operator;
	private Integer rule;
	private Integer state;
	private String note;
	private String backup1;
	private String backup2;
	
}
