package cn.soa.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 问题属地对应区域实体类
 * @author Jiang,Hang
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class ProblemTypeArea implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer problemID;
	private String problemName;
	private Integer isParent;
	private Integer parentID;
	private List<ProblemTypeArea> problemAreas;
}
