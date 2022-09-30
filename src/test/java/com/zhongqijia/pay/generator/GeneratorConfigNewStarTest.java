package com.zhongqijia.pay.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

public class GeneratorConfigNewStarTest {

    @Test
    public void testGenerator(){
        //1、全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)//开启AR模式
                .setAuthor("Luolaigang")//设置作者
                //生成路径(一般都是生成在此项目的src/main/java下面)
                .setOutputDir("C:\\Users\\llg\\Desktop\\mybatis-generator")
                .setFileOverride(true)//第二次生成会把第一次生成的覆盖掉
                .setIdType(IdType.AUTO)//主键策略
                .setServiceName("%sService")//生成的service接口名字首字母是否为I，这样设置就没有I
                .setEntityName("%s")
                .setBaseResultMap(true)//生成resultMap
                .setBaseColumnList(true);//在xml中生成基础列
        //2、数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)//数据库类型
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl("jdbc:mysql://rm-wz9809d7qsc0971h7bo.mysql.rds.aliyuncs.com:3306/yipin?autoReconnect=true&allowMultiQueries=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull")
                .setUsername("panda_rw")
                .setPassword("^Ta3hss&s##uPm$r");
        //3、策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)//开启全局大写命名
//                .setDbColumnUnderline(true)//表名字段名使用下划线
                .setNaming(NamingStrategy.underline_to_camel)//下划线到驼峰的命名方式
//                .setTablePrefix("tb_")//表名前缀
                .setEntityLombokModel(true)//使用lombok
                .setInclude("log_bind_card");//逆向工程使用的表
        //4、包名策略配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.zhongqijia.pay")//设置包名的parent
                .setMapper("mapper")
                .setService("service")
                .setEntity("bean");//设置pojo
        //5、整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig);
        //6、执行
        autoGenerator.execute();
    }
}
