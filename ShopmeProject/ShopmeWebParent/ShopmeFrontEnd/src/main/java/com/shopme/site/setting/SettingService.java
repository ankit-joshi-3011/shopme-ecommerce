package com.shopme.site.setting;

import org.springframework.stereotype.Service;

import com.shopme.common.setting.SettingRepository;
import com.shopme.common.setting.SettingServiceBase;

@Service
public class SettingService extends SettingServiceBase {
	public SettingService(SettingRepository settingRepository) {
		super(settingRepository);
	}
}
