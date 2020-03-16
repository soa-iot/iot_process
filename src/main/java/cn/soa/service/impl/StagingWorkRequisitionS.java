package cn.soa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.StagingWorkRequisitionMapper;
import cn.soa.entity.StagingWorkRequisition;
import cn.soa.service.inter.StagingWorkRequisitionSI;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StagingWorkRequisitionS implements StagingWorkRequisitionSI {
	
	@Autowired
	private StagingWorkRequisitionMapper stagingWorkMapper;
	
	@Override
	public StagingWorkRequisition findByPIID(String piid) {
		return stagingWorkMapper.findByPIID(piid);
	}

	@Override
	public Integer addOrUpdate(StagingWorkRequisition staging) {
		//1.根据piid查找数据是否已存在
		if(staging == null) {
			log.error("-------脚手架作业单数据为null");
			return null;
		}
		String piid = staging.getPiid();
		StagingWorkRequisition result = stagingWorkMapper.findByPIID(piid);
		//不存在就插入此数据
		Integer row = null;
		try {
			row = (result == null) ? stagingWorkMapper.insertOne(staging):stagingWorkMapper.updateOne(staging);
			if(row != 1) {
				log.error("-------脚手架作业单数据插入/更新失败");
				return null;
			}
		}catch(Exception e) {
			log.error("-------脚手架作业单数据插入/更新时，数据库发生异常");
			e.printStackTrace();
			
		}
		return row;
	}


}
