package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {
	private CurrencyRepository repository;

	@Autowired
	public CurrencyRepositoryTests(CurrencyRepository repository) {
		this.repository = repository;
	}

	@Test
	public void testCreateCurrencies() {
		for (Currency currency : new Currency[] {
			new Currency("United States Dollar", "$", "USD"),
			new Currency("British Pound", "£", "GBP"),
			new Currency("Japanese Yen", "¥", "JPY"),
			new Currency("Euro", "€", "EUR"),
			new Currency("Russian Ruble", "₽", "RUB"),
			new Currency("South Korean Won", "₩", "KRW"),
			new Currency("Chinese Yuan", "¥", "CNY"),
			new Currency("Brazilian Real", "R$", "BRL"),
			new Currency("Australian Dollar", "$", "AUD"),
			new Currency("Canadian Dollar", "$", "CAD"),
			new Currency("Vietnamese đồng", "đ", "VND"),
			new Currency("Indian Rupee", "₹", "INR"),
		}) {
			Currency savedCurrency = repository.save(currency);

			assertThat(savedCurrency.getId()).isGreaterThan(0);
		}
	}

	@Test
	public void testFindAllByOrderByNameAsc() {
		List<Currency> currencies = repository.findAllByOrderByNameAsc();

		currencies.forEach(currency -> System.out.println(currency.getName()));
	}
}
