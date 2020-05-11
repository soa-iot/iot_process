package cn.soa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.soa.dao.AnticorWorkRequisitionMapper;
import cn.soa.entity.AnticorWorkRequisition;
import cn.soa.service.inter.AnticorWorkRequisitionSI;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnticorWorkRequisitionS implements AnticorWorkRequisitionSI {
	
	@Autowired
	private AnticorWorkRequisitionMapper mapper;
	
	@Override
	public AnticorWorkRequisition findByPIID(String piid) {
		return mapper.findByPIID(piid);
	}

	@Override
	@Transactional
	public Integer insertOne(AnticorWorkRequisition anticor) {
		try {
			//1. 先删除已有的数据
			mapper.deleteByPiid(anticor.getPiid());
			//2. 再插入新数据
			Integer rows = mapper.insertOne(anticor);
			if(rows == null || rows < 1) {
				log.error("---防腐保温作业单保存失败。。");
				return null;
			}
			return rows;
		}catch (Exception e) {
			log.error("防腐保温作业单添加或修改失败。。");
			new RuntimeException(e.getMessage());
			return null;
		}
	}

	@Override
	public Integer deleteByPiid(String piid) {
		try {
			Integer row = mapper.deleteByPiid(piid);
			return row;
		}catch (Exception e) {
			log.error("防腐保温作业单删除失败。。");
			e.printStackTrace();
			return null;
		}
	}

}
