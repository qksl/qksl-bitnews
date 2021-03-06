package io.bitnews.framework.sms.sender;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.bitnews.framework.sms.SmsParameter;
import io.bitnews.framework.sms.SmsSendResult;
import io.bitnews.framework.sms.SmsSender;
import io.bitnews.framework.sms.properties.SmsProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 阿里云短信发送器
 */
@Data
@Slf4j
public class AliyunSmsSender implements SmsSender {

    @Autowired
    private SmsProperties smsProperties;

    private String STATUS_OK = "OK";

    public AliyunSmsSender(){
        log.info("初始化阿里云接口:" + this);
    }

    @Override
    public SmsSendResult send(SmsParameter parameter) {

        SmsSendResult result = new SmsSendResult();
        result.setSuccess(true);

        final String product = "Dysmsapi";
        // 短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";
        // 短信API产品域名（接口地址固定，无需修改）

        // 替换成你的AK
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 使用post提交
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(parameter.getPhoneNumber());
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(smsProperties.getSignName());
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsProperties.getTemplateCode());
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(parameter.getParams());
        //request.setOutId("yourOutId");

        // 请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("SEND_SMS_FAILURE");
            throw new RuntimeException("发送短信发生错误：" + e);
        }
        if (sendSmsResponse.getCode() == null || !STATUS_OK.equals(sendSmsResponse.getCode())) {
            log.error("发送短信失败：" + sendSmsResponse.getMessage());
            result.setSuccess(false);
            result.setCode(sendSmsResponse.getCode());
            return result;
        }
        log.info("发送短信成功：" + sendSmsResponse.getCode());
        return result;
    }

}
