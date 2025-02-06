package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.setting.SettingRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
	private SettingRepository repository;

	@Autowired
	public SettingRepositoryTests(SettingRepository repository) {
		this.repository = repository;
	}

	@Test
	public void testCreateGeneralSettings() {
		for (Setting generalSetting : new Setting[] {
			new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL),
			new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL),
			new Setting("COPYRIGHT", "Copyright (C) 2025 Shopme Ltd.", SettingCategory.GENERAL)
		}) {
			Setting savedSetting = repository.save(generalSetting);

			assertThat(savedSetting).isNotNull();
		}
	}

	@Test
	public void testCreateCurrencySettings() {
		for (Setting currencySetting : new Setting[] {
			new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY),
			new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY),
			new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY),
			new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY),
			new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY),
			new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY),
		}) {
			Setting savedSetting = repository.save(currencySetting);

			assertThat(savedSetting).isNotNull();
		}
	}

	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repository.findByCategory(SettingCategory.GENERAL);

		settings.forEach(setting -> System.out.println("Key: " + setting.getKey() + " Value: " + setting.getValue()));
	}
}
