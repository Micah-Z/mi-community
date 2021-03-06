package org.mi.biz.tool.config;

import com.alibaba.alicloud.context.AliCloudProperties;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: mi-community
 * @description:
 * @author: Micah
 * @create: 2020-12-01 19:26
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "sms.config")
public class SmsConfig {


    private String signName;

    private String templateCode;


    @Bean
    public SendSmsRequest sendSmsRequest(){
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSignName(signName);
        sendSmsRequest.setTemplateCode(templateCode);
        return sendSmsRequest;
    }

}
