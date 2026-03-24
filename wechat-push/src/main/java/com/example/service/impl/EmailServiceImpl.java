package com.example.service.impl;

import com.example.service.EmailService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件服务实现类
 * 使用 Spring JavaMailSender 发送邮件
 */
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource
    private JavaMailSender mailSender;

    @Value("${captcha.email.from}")
    private String fromEmail;

    @Value("${captcha.email.subject:【系统通知】绑定验证码}")
    private String emailSubject;

    /**
     * Mock 数据：工号与邮箱的映射
     * TODO: 实际项目中应从数据库或HR系统查询
     */
    private static final Map<String, String> USER_EMAIL_MAP = new HashMap<>();
    static {
        USER_EMAIL_MAP.put("10001", "zhangsan@smart.org.cn");
        USER_EMAIL_MAP.put("10002", "lisi@smart.org.cn");
        USER_EMAIL_MAP.put("10003", "wangwu@smart.org.cn");
    }

    @Override
    public String getEmailByUserId(String userId) {
        logger.info("根据工号获取邮箱: userId={}", userId);

        // TODO: 实际项目中应调用HR系统接口或查询数据库
        String email = USER_EMAIL_MAP.get(userId);

        if (email == null) {
            // 如果找不到，返回默认格式的邮箱（用于测试）
            email = userId + "@smart.org.cn";
            logger.info("未找到工号{}的邮箱", userId);
        }

        return email;
    }

    @Override
    public boolean sendCaptchaEmail(String email, String captcha, String userId) {
        logger.info("发送验证码邮件: email={}, userId={}", email, userId);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 设置发件人
            helper.setFrom(fromEmail);

            // 设置收件人
            helper.setTo(email);

            // 设置主题
            helper.setSubject(emailSubject);

            // 设置邮件内容（HTML格式）
            String content = buildEmailContent(captcha, userId);
            helper.setText(content, true);

            // 发送邮件
            mailSender.send(message);

            logger.info("验证码邮件发送成功: email={}", email);
            return true;

        } catch (MessagingException e) {
            logger.error("验证码邮件构建失败: email={}", email, e);
            return false;
        } catch (Exception e) {
            logger.error("验证码邮件发送失败: email={}", email, e);
            return false;
        }
    }

    /**
     * 构建邮件内容（HTML格式）
     */
    private String buildEmailContent(String captcha, String userId) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("<meta charset='UTF-8'>");
        content.append("<style>");
        content.append("body { font-family: 'Microsoft YaHei', Arial, sans-serif; line-height: 1.6; color: #333; }");
        content.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        content.append(".header { background-color: #1890ff; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }");
        content.append(".content { background-color: #f9f9f9; padding: 30px; border: 1px solid #e8e8e8; }");
        content.append(".captcha-box { background-color: #fff; border: 2px dashed #1890ff; border-radius: 5px; padding: 20px; text-align: center; margin: 20px 0; }");
        content.append(".captcha-code { font-size: 32px; font-weight: bold; color: #1890ff; letter-spacing: 5px; }");
        content.append(".info { color: #666; font-size: 14px; margin-top: 20px; }");
        content.append(".warning { color: #ff4d4f; font-size: 12px; margin-top: 15px; }");
        content.append(".footer { text-align: center; color: #999; font-size: 12px; padding: 20px; }");
        content.append("</style>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div class='container'>");
        content.append("<div class='header'>");
        content.append("<h2>用户绑定验证码</h2>");
        content.append("</div>");
        content.append("<div class='content'>");
        content.append("<p>尊敬的用户（工号：<strong>").append(userId).append("</strong>）：</p>");
        content.append("<p>您好！您正在进行用户绑定操作，请使用以下验证码完成验证：</p>");
        content.append("<div class='captcha-box'>");
        content.append("<div class='captcha-code'>").append(captcha).append("</div>");
        content.append("</div>");
        content.append("<div class='info'>");
        content.append("<p>验证码有效期为 <strong>5分钟</strong>，请尽快完成验证。</p>");
        content.append("</div>");
        content.append("<div class='warning'>");
        content.append("<p>如非本人操作，请忽略此邮件，您的账号安全不会受到影响。</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("<div class='footer'>");
        content.append("<p>此邮件为系统自动发送，请勿直接回复。</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("</body>");
        content.append("</html>");
        return content.toString();
    }
}