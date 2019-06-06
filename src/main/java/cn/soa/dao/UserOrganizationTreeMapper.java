package cn.soa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.soa.entity.UserOrganization;

@Mapper
public interface UserOrganizationTreeMapper {
	
	/**
	 * 查询所有数据
	 * @return
	 */
	List<UserOrganization> findAll(); 

   
}