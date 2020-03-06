package cn.soa.service.inter;

import cn.soa.entity.User;

public interface RollBackProcessInter {

	boolean rollBackByUserid(User user);

}
