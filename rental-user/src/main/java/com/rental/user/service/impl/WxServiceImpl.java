package com.rental.user.service.impl;

import com.rental.common.exception.BusinessException;
import com.rental.user.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 微信服务实现
 * 注意：生产环境应配置真实的微信 AppID 和 AppSecret
 */
@Slf4j
@Service
public class WxServiceImpl implements WxService {

    @Value("${wechat.appid:wx1234567890abcdef}")
    private String appid;

    @Value("${wechat.secret:abcdef1234567890abcdef1234567890}")
    private String secret;

    private static final String WX_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public String getOpenid(String code) {
        try {
            // TODO: 生产环境调用真实微信API
            // 这里返回模拟的 openid 用于测试
            // 实际应调用: https://api.weixin.qq.com/sns/jscode2session?appid=XXX&secret=XXX&js_code=XXX&grant_type=authorization_code
            
            log.info("微信登录: code={}, appid={}", code, appid);
            
            // 模拟返回 openid（开发环境使用）
            // 实际生产环境请调用真实微信 API
            return "mock_openid_" + code;
            
        } catch (Exception e) {
            log.error("获取openid失败: {}", e.getMessage());
            throw new BusinessException("微信登录失败");
        }
    }
}
