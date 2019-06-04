package cn.soa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 问题上报实体VO类
 * 
 * @author Jiang,Hang
 *
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class ProblemInfoVO implements Serializable {
	
	private String tProblemRepId;
	private String problemtype;
	private String problemnum;
	private String welName;
	private String problemdescribe;
	private String resavepeople;
	private String applypeople;
	private String remarkfive;
	private String remarksix;
	private String rfid;
	private String problemclass;
	private String profession;
	
	private List<ProblemReportpho> reportPhos;

	public ProblemInfoVO(String resavepeople, String profession, String welName, String problemdescribe, String remarkfive,
			String remarksix, String rfid, String problemtype, String problemclass, String applypeople) {
		this.resavepeople = resavepeople;
		this.profession = profession;
		this.welName = welName;
		this.problemdescribe = problemdescribe;
		this.remarkfive = remarkfive;
		this.remarksix = remarksix;
		this.rfid = rfid;
		this.problemclass = problemclass;
		this.applypeople = applypeople;
		this.problemtype = problemtype;
		this.problemnum = problemnum;
	}
	
}

