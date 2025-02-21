package com.shopme.admin.setting.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.setting.CurrencyService;
import com.shopme.admin.setting.GeneralSettingsBag;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingController {
	private SettingService settingService;
	private CurrencyService currencyService;

	public SettingController(SettingService settingService, CurrencyService currencyService) {
		this.settingService = settingService;
		this.currencyService = currencyService;
	}

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = settingService.listAllSettings();
		List<Currency> listCurrencies = currencyService.listAllCurrencies();

		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		model.addAttribute("listCurrencies", listCurrencies);

		return "settings/settings";
	}

	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes attributes) throws IOException {
		GeneralSettingsBag settingsBag = settingService.getGeneralSettingsBag();

		if (!multipartFile.isEmpty()) {
			final String siteLogoDirectory = "/site-logo/";

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			String filePath = siteLogoDirectory + fileName;
			settingsBag.updateSiteLogo(filePath);

			String uploadDirectory = ".." + siteLogoDirectory;
			FileUploadUtility.cleanDirectory(uploadDirectory);
			FileUploadUtility.saveFile(uploadDirectory, fileName, multipartFile);
		}

		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Currency currencyDb = currencyService.findById(currencyId);

		if (currencyDb != null) {
			settingsBag.updateCurrencySymbol(currencyDb.getSymbol());
		}

		updateSettingValues(request, settingsBag.list());

		settingService.saveAll(settingsBag.list());

		attributes.addFlashAttribute("message", "General settings have been saved successfully");

		return "redirect:/settings";
	}

	@PostMapping("/settings/save_mailserver")
	public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes attributes) throws IOException {
		List<Setting> mailServerSettings = settingService.getMailServerSettings();

		updateSettingValues(request, mailServerSettings);

		settingService.saveAll(mailServerSettings);

		attributes.addFlashAttribute("message", "Mail server settings have been saved successfully");

		return "redirect:/settings";
	}

	private void updateSettingValues(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());

			if (value != null) {
				setting.setValue(value);
			}
		}
	}
}
