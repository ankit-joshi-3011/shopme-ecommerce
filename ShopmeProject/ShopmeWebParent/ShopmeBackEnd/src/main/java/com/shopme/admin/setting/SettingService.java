package com.shopme.admin.setting;

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

	public List<Setting> listAllSettings() {
		List<Setting> returnedSettings = new ArrayList<>();

		for (Setting storedSetting : settingRepository.findAll()) {
			returnedSettings.add(storedSetting);
		}

		return returnedSettings;
	}

	public GeneralSettingsBag getGeneralSettingsBag() {
		return new GeneralSettingsBag(this.getGeneralSettings());
	}

	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}

	public List<Setting> getMailServerSettings() {
		List<Setting> mailServerSettings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);

		List<Setting> returnedSettings = new ArrayList<>();

		for (Setting storedSetting : mailServerSettings) {
			returnedSettings.add(storedSetting);
		}

		return returnedSettings;
	}
}
