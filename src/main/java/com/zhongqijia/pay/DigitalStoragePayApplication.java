package com.zhongqijia.pay;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.zhongqijia.pay.utils.CertUtil;
import com.zhongqijia.pay.utils.SDKConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.zhongqijia.pay")//项目主路径
@EnableCreateCacheAnnotation
public class DigitalStoragePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalStoragePayApplication.class, args);
		// 加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		// 加载证书
		try {
			CertUtil.init(SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
