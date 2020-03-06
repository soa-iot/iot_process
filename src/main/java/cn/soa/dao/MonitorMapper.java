package cn.soa.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.Monitor;

@Mapper
public interface MonitorMapper {
	
	int insertAll(Monitor monitor);
}
