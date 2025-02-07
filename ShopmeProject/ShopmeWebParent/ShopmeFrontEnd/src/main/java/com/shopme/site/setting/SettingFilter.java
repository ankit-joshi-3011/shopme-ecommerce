package com.shopme.site.setting;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.shopme.common.entity.Setting;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class SettingFilter implements Filter {
	private SettingService settingService;

	public SettingFilter(SettingService settingService) {
		this.settingService = settingService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String url = servletRequest.getRequestURL().toString();

		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")) {
			chain.doFilter(request, response);
			return;
		}

		List<Setting> generalSettings = settingService.getGeneralSettings();
		generalSettings.forEach(setting -> request.setAttribute(setting.getKey(), setting.getValue()));

		chain.doFilter(request, response);
	}
}
