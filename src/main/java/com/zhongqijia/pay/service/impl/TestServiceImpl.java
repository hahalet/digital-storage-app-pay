package com.zhongqijia.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeepay.yop.sdk.exception.YopClientException;
import com.yeepay.yop.sdk.model.YopRequestConfig;
import com.yeepay.yop.sdk.service.common.YopClient;
import com.yeepay.yop.sdk.service.common.YopClientBuilder;
import com.yeepay.yop.sdk.service.common.request.YopRequest;
import com.yeepay.yop.sdk.service.common.response.YosUploadResponse;
import com.zhongqijia.pay.bean.TestBean;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.mapper.TestMapper;
import com.zhongqijia.pay.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;

@Service
@Slf4j
public class TestServiceImpl extends ServiceImpl<TestMapper, TestBean> implements TestService {

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public int getCount() {
        redisUtil.set("RedisTest:test", "111");
        // 初始化client，该Client线程安全，请使用单例模式，多次请求共用
        YopClient yopClient = YopClientBuilder.builder().build();
        // 指定要请求的API地址和请求方式
        YopRequest request = new YopRequest("/rest/v1.0/file/upload", "POST");

        YopRequestConfig requestConfig = request.getRequestConfig();
        // 请求级别appkey设置(可选)，否则取默认appKey
        //requestConfig.setAppKey("your appkey");
        // 指定单次请求获取数据的超时时间, 单位：ms(可选，默认采用配置文件中的设置)
        //requestConfig.setReadTimeout(3000);
        // 指定单次请求建立连接的超时, 单位：ms(可选，默认采用配置文件中的设置)
        //requestConfig.setConnectTimeout(3000);
        // 普通参数传递
        request.addParameter("orderNo", "2020112412341123");
        String pdfFilePath = "config\\yop_sdk_config_default.json";
        Resource resource = new ClassPathResource(pdfFilePath);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String fileString = FileCopyUtils.copyToString(reader);
            log.info("易宝api fileString:{}",fileString);
            // 本地文件参数传递
            request.addMutiPartFile("merQual", byte2File(fileString.getBytes(),"config.json",this.getClass()));
            // 如果是：普通请求
            //YopResponse response = yopClient.request(request);

            // 如果是：文件上传
            YosUploadResponse uploadResponse = yopClient.upload(request);
        } catch (Exception e) {
            log.info("易宝api调用错误:{}",e.getMessage());
        }

        return testMapper.getCount();
    }

    /**
     * byte 转file
     */
    public static File byte2File(byte[] buf, String fileName,Class classStr){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String filePath = getPath(classStr);
        log.info("filePath:{}",filePath);
        try{
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()){
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }catch (Exception e){
            log.info("易宝api文件保存错误:{}",e.getMessage());
        }
        finally{
            if (bos != null){
                try{
                    bos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static String getPath(Class classStr) {
        String path = classStr.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1, path.length());
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }
}
