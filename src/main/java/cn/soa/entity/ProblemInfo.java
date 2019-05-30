package cn.soa.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 问题上报实体类
 * 
 * @author Bru.Lo
 *
 */
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class ProblemInfo implements Serializable {
	
	private String tProblemRepId;
	private String piid;
	private String tasId;
	private String problemtype;
	private String welId;
	private String welName;
	private String adress;
	private String impacts;
	private String problemdescribe;
	private String resavepeople;
	private String applypeople;
	private Date applydate;
	private String problemstate;
	private String remark;
	private String remarkone;
	private String remarktwo;
	private String remarkthree;
	private String supervisoryperson;
	private String maintenanceman;
	private String rectificationmeasures;
	private Date rectificationperiod;
	private String isSms;
	private String smsConment;
	private Date smsDate;
	private String taskPiid;
	private String processtype;
	private Date supervisorydate;
	private String supervisorydesc;
	private String tasDistribute;
	private String departOrCentral;
	private Date terBeginworkDate;
	private Date terEndworkDate;
	private String terPlace;
	private String spId;
	private String spName;
	private String organizationSiteOffice;
	private String officeProblem;
	private String materialsApplication;
	private String applicationRemark;
	private String controleApplication;
	private String opeCmId;
	private String opecardNo;
	private String handleTicket;
	private String ticketNo;
	private String correctiveAction;
	private String action;
	private String others;
	private String remarkfive;
	private String remarksix;
	private String remarkseven;
	private String rfid;
	private String isdownload;
	private Integer problemnum;
	private String phoName;
	private String processDesc;
	private String phoDispiayName;
	private String rectifInstruction;
	private String phoAddress;
	private String gdName;
	private String workPermit;
	private String workPermitNumber;
	private String problemclass;
	private String profession;
	private String depet;
	private String interOrOuter;
	private String outerDealDescribe;

	public ProblemInfo(String piid, String profession, String welName, String problemdescribe, String remarkfive,
			String remarksix, String rfid, String problemclass) {
		this.piid = piid;
		this.profession = profession;
		this.welName = welName;
		this.problemdescribe = problemdescribe;
		this.remarkfive = remarkfive;
		this.remarksix = remarksix;
		this.rfid = rfid;
		this.problemclass = problemclass;
	}

}
