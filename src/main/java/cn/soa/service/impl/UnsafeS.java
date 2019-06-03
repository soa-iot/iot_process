package cn.soa.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.soa.dao.UnsafeTypeMapper;
import cn.soa.entity.UnsafeType;
import cn.soa.service.inter.UnsafeSI;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UnsafeS implements UnsafeSI {
	
	@Autowired
	private UnsafeTypeMapper unsafeTypeMapper;
	
	/**   
	 * @Title: getList   
	 * @Description: 查询出所有不安全行为数据 
	 * @return: List<UnsafeType>   返回不安全行为数据列表   
	 */
	@Override
	public List<UnsafeType> getList() {
		log.info("开始查询出所有不安全行为数据");
		List<UnsafeType> result = null;
		try {
			result = unsafeTypeMapper.findAll();
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		log.info("查询不安全行为数据操作结束");
		return result;
	}

}
