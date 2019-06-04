package cn.soa;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//spring注解扫描
@SpringBootApplication(scanBasePackages = "cn.soa",exclude = SecurityAutoConfiguration.class)
public class IotprocessApplication extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(IotprocessApplication.class, args);
	}			

	 @Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	        // 注意这里要指向原先用main方法执行的Application启动类
	        return builder.sources(IotprocessApplication.class); 
	 }
}
