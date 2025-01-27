package com.shopme.admin.setting.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.setting.CurrencyService;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

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
}
