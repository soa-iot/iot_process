package cn.soa.service.inter;

import java.util.Map;

public interface RemoteRuquestSI {

	Object remoteRequestByUrl(String url, Map<String, Object> param);

}
