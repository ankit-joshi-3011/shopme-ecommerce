package com.shopme.common.entity;

import java.util.List;
import java.util.Map;

public class SettingsBag {
	private Map<String, Setting> mapSettings;

	public SettingsBag(List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			mapSettings.put(setting.getKey(), setting);
		}
	}

	public Setting get(String key) {
		return mapSettings.getOrDefault(key, null);
	}

	public String getValue(String key) {
		Setting setting = mapSettings.getOrDefault(key, null);

		if (setting == null) {
			return null;
		}

		return setting.getValue();
	}

	public void update(String key, String value) {
		Setting setting = mapSettings.getOrDefault(key, null);

		if (setting != null && value != null) {
			setting.setValue(value);
		}
	}

	public List<Setting> list() {
		return mapSettings.values().stream().toList();
	}
}
