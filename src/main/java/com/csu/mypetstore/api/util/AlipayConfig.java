package com.csu.mypetstore.api.util;

import ch.qos.logback.core.util.FileUtil;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
@Component
public class AlipayConfig {
//    @Value("${}")
//    private static String appId;
//    @Value("${}")
//    private static String gatewayHost;
//    @Value("${}")
//    private static String merchantPrivateKeyPath;
//    @Value("${}")
//    private static String alipayPublicKey;
//    @Value("${Alipay-easySDK.notifyUrl}")
//    private static String notifyUrl;
//    @Value("${}")
//    private static String encryptKey;

    static {
        try {
            Factory.setOptions(getOptions());
        } catch (IOException e) {
            log.error("读取私钥失败");
            throw new RuntimeException(e);
        }
    }

    private static Config getOptions() throws IOException {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ClassPathResource("alipay.yml"));
        Properties properties = yamlFactory.getObject();

        Config config = new Config();

        config.protocol = "https";
        config.gatewayHost = properties.getProperty("Alipay-easySDK.gatewayHost");  // 沙箱环境网关
        config.signType = "RSA2";

        config.appId = properties.getProperty("Alipay-easySDK.appId");
        String privateKayPath = properties.getProperty("Alipay-easySDK.merchantPrivateKeyPath");
        config.merchantPrivateKey = Files.readString(Paths.get(privateKayPath));
        config.alipayPublicKey = properties.getProperty("Alipay-easySDK.alipayPublicKey");
        config.encryptKey = properties.getProperty("Alipay-easySDK.encryptKey");

        return config;
    }
}
