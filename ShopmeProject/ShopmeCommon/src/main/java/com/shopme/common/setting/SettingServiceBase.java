package com.shopme.common.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public abstract class SettingServiceBase {
	protected SettingRepository settingRepository;

	public SettingServiceBase(SettingRepository settingRepository) {
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
