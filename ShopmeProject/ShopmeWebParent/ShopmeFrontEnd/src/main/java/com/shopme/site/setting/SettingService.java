package com.shopme.site.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {
	private SettingRepository settingRepository;

	public SettingService(SettingRepository settingRepository) {
		this.settingRepository = settingRepository;
	}

	public List<Setting> getGeneralSettings() {
		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

		List<Setting> allSettings = new ArrayList<>();
		allSettings.addAll(generalSettings);
		allSettings.addAll(currencySettings);

		return allSettings;
	}
}
