package cn.soa.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
public class ProblemReportpho implements Serializable{

	private String tProblemPhoId;
	private String tProblemRepId;
	private String problemnum;
	private String phoName;
	private String phoAddress;
	private String phoDispiayName;
	private Date phoUploadDate;
	private String phoUploadPeople;
	private String remark;
	private String remarkone;
	private String remarktwo;
	private String remarkthree;
	private String piid;
}
