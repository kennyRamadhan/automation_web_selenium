package com.kenny.automation.Config;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
public class ChromeDriverFactory implements DriverFactory {

    @Override
    public WebDriver createDriver() {
    	
    	WebDriverManager.chromedriver().setup();
    	
        // Nonaktifkan logging yang tidak perlu
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--incognito");

        // Headless mode jika dijalankan di Jenkins
        String browserMode = System.getProperty("BROWSER_MODE", "normal");
        if ("headless".equalsIgnoreCase(browserMode)) {
        	
            options.addArguments("--headless"); // new headless di Chrome >109
            
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        // Disable password manager & popups
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

     

        return new ChromeDriver(options);
    }
}
