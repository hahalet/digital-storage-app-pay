package com.zhongqijia.pay;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.zhongqijia.pay")//项目主路径
@EnableCreateCacheAnnotation
public class DigitalStoragePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalStoragePayApplication.class, args);
	}

}
