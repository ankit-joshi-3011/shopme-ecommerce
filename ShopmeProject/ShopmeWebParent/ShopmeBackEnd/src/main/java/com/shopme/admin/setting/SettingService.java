package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.setting.SettingRepository;
import com.shopme.common.setting.SettingServiceBase;

@Service
public class SettingService extends SettingServiceBase {
	public SettingService(SettingRepository settingRepository) {
		super(settingRepository);
	}

	public List<Setting> listAllSettings() {
		List<Setting> returnedSettings = new ArrayList<>();

		for (Setting storedSetting : settingRepository.findAll()) {
			returnedSettings.add(storedSetting);
		}

		return returnedSettings;
	}

	public GeneralSettingsBag getGeneralSettingsBag() {
		return new GeneralSettingsBag(super.getGeneralSettings());
	}

	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}
}
