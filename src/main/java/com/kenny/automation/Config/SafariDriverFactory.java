package com.kenny.automation.Config;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.safari.SafariDriver;
import io.appium.java_client.safari.options.SafariOptions;

public class SafariDriverFactory implements DriverFactory {

	
	private static final String SAFARIDRIVER_PATH = System.getProperty("user.dir") + "/web-driver/safaridriver";

	@Override
	public WebDriver createDriver() {
		System.setProperty("webdriver.safari.driver", SAFARIDRIVER_PATH);
        SafariOptions options = new SafariOptions();
        return new SafariDriver(options);
	}


}
