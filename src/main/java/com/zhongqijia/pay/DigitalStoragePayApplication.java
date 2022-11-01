package com.zhongqijia.pay;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.zhongqijia.pay.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.zhongqijia.pay")//项目主路径
@EnableCreateCacheAnnotation
@Slf4j
public class DigitalStoragePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalStoragePayApplication.class, args);

		try {
			log.info("加载C2C配置文件");
			// 加载配置文件
			ConfigLoader.getConfig();

			log.info("加载C2B配置文件");
			// 加载配置文件
			SandSDKConfig.getConfig().loadPropertiesFromSrc();
			// 加载证书
			SandCertUtil.init(SandSDKConfig.getConfig().getSandCertPath(), SandSDKConfig.getConfig().getSignCertPath(), SandSDKConfig.getConfig().getSignCertPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
