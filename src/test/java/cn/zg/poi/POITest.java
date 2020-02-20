package cn.zg.poi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import cn.soa.entity.ProblemInfoForExcel;
import cn.soa.entity.ResultJson;
import cn.soa.utils.ImportExcelUtil;

public class POITest {
	
	@Test
	public void test() throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook("D:\\问题批量上报模板表.xlsx");
		ImportExcelUtil excel = new ImportExcelUtil();
		
		try {
			List<MultiValueMap<String, String>> result = excel.readExcelValue(workbook, (short)0, null, null);
			RestTemplate rest = new RestTemplate();
			//解决中文乱码
			rest.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
			for(int i=0;i<result.size();i++) {
				System.out.println(result.get(i));
				
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String,String>>(result.get(i), headers);
				ResultJson<String> json = rest.postForObject("http://192.168.3.11:10238/iot_process/process/", request, ResultJson.class);
				System.out.println(json.getState());
				System.out.println(json.getData());
			}
			
		}catch (Exception e) {
			System.err.println(e.getMessage());
			
		}
	}
}
