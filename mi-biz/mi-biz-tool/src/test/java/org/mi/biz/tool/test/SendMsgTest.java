package org.mi.biz.tool.test;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.mi.biz.tool.MiToolApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: mi-community
 * @description:
 * @author: Micah
 * @create: 2020-12-01 16:08
 **/
@SpringBootTest(classes = MiToolApplication.class)
public class SendMsgTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void senSms(){
        this.rocketMQTemplate.convertAndSend("sms:test","测试消息");
    }
}
