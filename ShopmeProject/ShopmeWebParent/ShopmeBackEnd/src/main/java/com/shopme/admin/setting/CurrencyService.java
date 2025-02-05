package com.shopme.admin.setting;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.Currency;

@Service
public class CurrencyService {
	private CurrencyRepository currencyRepository;

	public CurrencyService(CurrencyRepository currencyRepository) {
		this.currencyRepository = currencyRepository;
	}

	public List<Currency> listAllCurrencies() {
		return currencyRepository.findAllByOrderByNameAsc();
	}

	public Currency findById(Integer currencyId) {
		Optional<Currency> currency = currencyRepository.findById(currencyId);

		if (currency.isEmpty()) {
			return null;
		}

		return currency.get();
	}
}
