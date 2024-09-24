package com.easytrip.utils;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.easytrip.entity.config.AppConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
@Component("smsUtil")
public class SmsUtil {
    @Resource
    private AppConfig appConfig;
    public  boolean sendSms(String phone, String code) {
        // 整合阿里云短信服务发送
        // 设置相关参数
        DefaultProfile profile = DefaultProfile.getProfile(appConfig.getRegionId(),appConfig.getAccessKeyID(),appConfig.getAccessKey());
        IAcsClient client = new DefaultAcsClient(profile);
        // 创建通用的请求对象
        CommonRequest request = new CommonRequest();
        // 指定请求方式
        request.setMethod(MethodType.POST);
        // 固定写法阿里云短信地址
        request.setDomain("dysmsapi.aliyuncs.com");
        // 签名算法版本  固定
        request.setVersion("2017-05-25");
        // 请求api的名称
        request.setAction("SendSms");

        //手机号--下面几个参数key值不允许修改
        request.putQueryParameter("PhoneNumbers", phone);
        //签名名称
        request.putQueryParameter("SignName", appConfig.getSMSSign());
        //模板code
        request.putQueryParameter("TemplateCode", appConfig.getSMSTemplate());
        //验证码  使用json格式   {"code":"123456"}
        Map<String,Object> param = new HashMap();
        param.put("code",code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        // 调用方法进行发送短信
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
