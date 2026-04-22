package com.example.service.impl;

import com.example.entity.UserAccount;
import com.example.repository.UserAccountRepository;
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

/**
 * 邮件服务实现类
 * 使用 Spring JavaMailSender 发送邮件
 */
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private UserAccountRepository userAccountRepository;

    @Value("${captcha.email.from}")
    private String fromEmail;

    @Value("${captcha.email.subject:【系统通知】绑定验证码}")
    private String emailSubject;

    @Override
    public String getEmailByUserId(String userId) {
        logger.info("根据工号获取邮箱: userId={}", userId);

        return userAccountRepository.findByUsername(userId)
                .filter(account -> account.getIsActive() != null && account.getIsActive() == 1)
                .map(UserAccount::getEmail)
                .orElseGet(() -> {
                    logger.warn("未找到工号{}对应的激活账号", userId);
                    return userId + "@smart.org.cn";
                });
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
     * 构建邮件内容（HTML格式 - 中英文双语）
     */
    private String buildEmailContent(String captcha, String userId) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("<meta charset='UTF-8'>");
        content.append("<style>");
        content.append("body { font-family: 'Microsoft YaHei', Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f5f7fa; }");
        content.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        content.append(".header { background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%); color: white; padding: 24px; text-align: center; border-radius: 8px 8px 0 0; }");
        content.append(".header h2 { margin: 0 0 8px 0; font-size: 22px; }");
        content.append(".header p { margin: 0; font-size: 14px; opacity: 0.9; }");
        content.append(".content { background-color: #fff; padding: 30px; border: 1px solid #e8e8e8; }");
        content.append(".greeting { margin-bottom: 20px; }");
        content.append(".greeting p { margin: 5px 0; }");
        content.append(".captcha-box { background: linear-gradient(135deg, #e6f7ff 0%, #f0f5ff 100%); border: 2px dashed #1890ff; border-radius: 8px; padding: 24px; text-align: center; margin: 24px 0; }");
        content.append(".captcha-code { font-size: 36px; font-weight: bold; color: #1890ff; letter-spacing: 8px; font-family: 'Courier New', monospace; }");
        content.append(".info { color: #666; font-size: 14px; margin-top: 20px; padding: 16px; background-color: #fafafa; border-radius: 4px; }");
        content.append(".info p { margin: 8px 0; }");
        content.append(".warning { color: #ff4d4f; font-size: 13px; margin-top: 16px; padding: 12px; background-color: #fff2f0; border-left: 3px solid #ff4d4f; border-radius: 4px; }");
        content.append(".warning p { margin: 5px 0; }");
        content.append(".divider { height: 1px; background: linear-gradient(90deg, transparent, #e8e8e8, transparent); margin: 24px 0; }");
        content.append(".footer { text-align: center; color: #999; font-size: 12px; padding: 20px; }");
        content.append(".footer p { margin: 5px 0; }");
        content.append(".lang-en { color: #666; font-style: italic; }");
        content.append("</style>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div class='container'>");
        
        // Header - 双语标题
        content.append("<div class='header'>");
        content.append("<h2>用户绑定验证码</h2>");
        content.append("<p>User Binding Verification Code</p>");
        content.append("</div>");
        
        content.append("<div class='content'>");
        
        // Greeting - 双语问候
        content.append("<div class='greeting'>");
        content.append("<p>尊敬的用户（工号：<strong>").append(userId).append("</strong>）：</p>");
        content.append("<p class='lang-en'>Dear User (Employee ID: <strong>").append(userId).append("</strong>):</p>");
        content.append("</div>");
        
        // Introduction - 双语说明
        content.append("<p>您好！您正在进行用户绑定操作，请使用以下验证码完成验证：</p>");
        content.append("<p class='lang-en'>Hello! You are performing a user binding operation. Please use the following verification code to complete verification:</p>");
        
        // Verification Code Box
        content.append("<div class='captcha-box'>");
        content.append("<div class='captcha-code'>").append(captcha).append("</div>");
        content.append("</div>");
        
        // Validity Info - 双语有效期
        content.append("<div class='info'>");
        content.append("<p><strong>验证码有效期为 5 分钟，请尽快完成验证。</strong></p>");
        content.append("<p class='lang-en'>The verification code is valid for <strong>5 minutes</strong>. Please complete the verification as soon as possible.</p>");
        content.append("</div>");
        
        // Warning - 双语安全提示
        content.append("<div class='warning'>");
        content.append("<p><strong>安全提示 / Security Notice：</strong></p>");
        content.append("<p>如非本人操作，请忽略此邮件，您的账号安全不会受到影响。</p>");
        content.append("<p class='lang-en'>If you did not initiate this request, please ignore this email. Your account security will not be affected.</p>");
        content.append("</div>");
        
        content.append("</div>");
        
        // Footer - 双语页脚
        content.append("<div class='footer'>");
        content.append("<p>此邮件为系统自动发送，请勿直接回复。</p>");
        content.append("<p class='lang-en'>This is an automated message, please do not reply directly.</p>");
        content.append("</div>");
        
        content.append("</div>");
        content.append("</body>");
        content.append("</html>");
        return content.toString();
    }
}