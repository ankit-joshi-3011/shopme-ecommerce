package com.shopme.admin.setting.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.setting.CurrencyService;
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
		if (!multipartFile.isEmpty()) {
			final String siteLogoDirectory = "/site-logo/";

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			String filePath = siteLogoDirectory + fileName;
			settingService.getGeneralSettings().updateSiteLogo(filePath);

			String uploadDirectory = ".." + siteLogoDirectory;
			FileUploadUtility.cleanDirectory(uploadDirectory);
			FileUploadUtility.saveFile(uploadDirectory, fileName, multipartFile);
		}

		attributes.addFlashAttribute("message", "General settings have been saved successfully");

		return "redirect:/settings";
	}
}
