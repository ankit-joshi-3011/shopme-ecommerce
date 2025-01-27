package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;

@Service
public class SettingService {
	private SettingRepository settingRepository;

	public SettingService(SettingRepository settingRepository) {
		this.settingRepository = settingRepository;
	}

	public List<Setting> listAllSettings() {
		List<Setting> returnedSettings = new ArrayList<>();

		for (Setting storedSetting : settingRepository.findAll()) {
			returnedSettings.add(storedSetting);
		}

		return returnedSettings;
	}
}
