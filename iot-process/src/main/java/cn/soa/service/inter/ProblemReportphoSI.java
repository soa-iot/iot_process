package cn.soa.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.soa.entity.ProblemReportpho;

/**
 * 图片信息业务逻辑层接口
 * @author Bru.Lo
 *
 */
@Service
public interface ProblemReportphoSI {

	/**
	 * 根据流程标识字段查询图片信息
	 * @param piid 流程标识字段
	 * @return 图片信息实体
	 */
	List<ProblemReportpho> getByPiid(String piid,String remark);
}
