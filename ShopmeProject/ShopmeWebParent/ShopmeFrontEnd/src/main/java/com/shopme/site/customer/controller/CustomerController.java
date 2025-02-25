package com.shopme.site.customer.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.dto.CountryDTO;
import com.shopme.common.entity.Customer;
import com.shopme.site.customer.CustomerService;
import com.shopme.site.setting.EmailSettingsBag;
import com.shopme.site.setting.SettingService;
import com.shopme.site.utility.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {
	private CustomerService customerService;
	private SettingService settingService;

	public CustomerController(CustomerService customerService, SettingService settingService) {
		this.customerService = customerService;
		this.settingService = settingService;
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		List<CountryDTO> listCountries = customerService.listAllCountries();

		model.addAttribute("listCountries", listCountries);
		model.addAttribute("customer", new Customer());

		return "register/registration_form";
	}

	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);

		EmailSettingsBag emailSettingsBag = settingService.getEmailSettingsBag();
		JavaMailSenderImpl mailSenderImpl = Utility.prepareMailSender(emailSettingsBag);

		String toAddress = customer.getEmail();
		String subject = emailSettingsBag.getCustomerVerifySubject();

		String content = emailSettingsBag.getCustomerVerifyContent();
		content = content.replace("[[name]]", customer.getFullName());

		String verifyUrl = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyUrl);

		MimeMessage message = mailSenderImpl.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);

		messageHelper.setFrom(emailSettingsBag.getFromAddress(), emailSettingsBag.getSenderName());
		messageHelper.setTo(toAddress);
		messageHelper.setSubject(subject);
		messageHelper.setText(content, true);

		mailSenderImpl.send(message);

		return "register/register_success";
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean wasVerificationSuccessful = customerService.verifyCustomer(code);

		return "register/" + (wasVerificationSuccessful ? "verify_success" : "verify_fail");
	}

}
