package com.zhongqijia.pay;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.zhongqijia.pay.utils.SandCertUtil;
import com.zhongqijia.pay.utils.SandSDKConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.zhongqijia.pay")//项目主路径
@EnableCreateCacheAnnotation
public class DigitalStoragePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalStoragePayApplication.class, args);
		// 加载配置文件
		SandSDKConfig.getConfig().loadPropertiesFromSrc();
		// 加载证书
		try {
			SandCertUtil.init(SandSDKConfig.getConfig().getSandCertPath(), SandSDKConfig.getConfig().getSignCertPath(), SandSDKConfig.getConfig().getSignCertPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
