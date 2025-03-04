package com.shopme.site.setting;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingsBag;

public class EmailSettingsBag extends SettingsBag {
	public EmailSettingsBag(List<Setting> listSettings) {
		super(listSettings);
	}

	public String getSmtpHost() {
		return super.getValue("MAIL_HOST");
	}

	public int getSmtpPort() {
		return Integer.parseInt(super.getValue("MAIL_PORT"));
	}

	public String getUsername() {
		return super.getValue("MAIL_USERNAME");
	}

	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}

	public String getSmtpAuth() {
		return super.getValue("SMTP_AUTH");
	}

	public String getSmtpSecured() {
		return super.getValue("SMTP_SECURED");
	}

	public String getFromAddress() {
		return super.getValue("MAIL_FROM");
	}

	public String getSenderName() {
		return super.getValue("MAIL_SENDER_NAME");
	}

	public String getCustomerVerifySubject() {
		return super.getValue("CUSTOMER_VERIFY_SUBJECT");
	}

	public String getCustomerVerifyContent() {
		return super.getValue("CUSTOMER_VERIFY_CONTENT");
	}
}
