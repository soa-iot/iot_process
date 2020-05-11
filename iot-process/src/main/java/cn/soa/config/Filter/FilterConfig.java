package cn.soa.config.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.soa.config.Myfilter;

@Configuration
public class FilterConfig {
	
	@Bean
	public FilterRegistrationBean<Myfilter> httpFilter(){
		FilterRegistrationBean<Myfilter> filterRegistrationBean = new FilterRegistrationBean<Myfilter>();
		// 设置filter
		filterRegistrationBean.setFilter(new Myfilter());
		// 拦截规则
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
}
