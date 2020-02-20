package cn.soa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource(value= {"classpath:/configProperties/fileDownUpload.properties"}, encoding="UTF-8")
public class ReportWebConfig implements WebMvcConfigurer {
	
	@Value("${problem.image.upload.path}")
	private String imagePath;  //从配置文件中读取图片存放位置

	/**
	 * 配置静态图片映射路径
	 */
	 @Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/image/**").addResourceLocations(imagePath);

	 }
}
