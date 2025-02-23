package com.shopme.site.utility;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.site.setting.EmailSettingsBag;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {
	public static String getSiteURL(HttpServletRequest request) {
		String currentRequestUrl = request.getRequestURL().toString();

		return currentRequestUrl.replace(request.getServletPath(), "");
	}

	public static JavaMailSenderImpl prepareMailSender(EmailSettingsBag emailSettingsBag) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(emailSettingsBag.getSmtpHost());
		mailSender.setPort(emailSettingsBag.getSmtpPort());
		mailSender.setUsername(emailSettingsBag.getUsername());
		mailSender.setPassword(emailSettingsBag.getPassword());

		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", emailSettingsBag.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", emailSettingsBag.getSmtpSecured());

		mailSender.setJavaMailProperties(mailProperties);

		return mailSender;
	}
}
