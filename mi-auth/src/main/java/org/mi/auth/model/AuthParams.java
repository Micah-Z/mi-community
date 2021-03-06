package org.mi.auth.model;

import lombok.Data;

/**
 * @program: mi-community
 * @description:
 * @author: Micah
 * @create: 2020-12-02 22:07
 **/
@Data
public class AuthParams {
    private String clientId;

    private String clientSecret;

    /**
     * 登录成功后的回调地址
     */
    private String redirectUri;
    /**
     * 支付宝公钥：当选择支付宝登录时，该值可用
     */
    private String alipayPublicKey = "";

    /**
     * 是否需要申请unionid，目前只针对qq登录
     * 注：qq授权登录时，获取unionid需要单独发送邮件申请权限。如果个人开发者账号中申请了该权限，可以将该值置为true，在获取openId时就会同步获取unionId
     * 参考链接：http://wiki.connect.qq.com/unionid%E4%BB%8B%E7%BB%8D
     * <p>
     * 1.7.1版本新增参数
     */
    private boolean unionId = false;

    /**
     * Stack Overflow Key
     * <p>
     *
     * @since 1.9.0
     */
    private String stackOverflowKey = "";

    /**
     * 企业微信，授权方的网页应用ID
     *
     * @since 1.10.0
     */
    private String agentId = "";
}
