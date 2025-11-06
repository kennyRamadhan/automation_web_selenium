package com.kenny.automation.Config;

import org.openqa.selenium.WebDriver;



public class WebDriverManager {
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	   // Inisialisasi driver baru
	    public static void initDriver(DriverFactory factory)  {
	    	if (driver.get() == null) {
	    		 driver.set(factory.createDriver());
	    		
	        }
	    }
	    
	    // Ambil driver yang aktif
	    public static WebDriver getDriver() {
	        return driver.get();
	    }

	    // Tutup driver
	    public static void quitDriver() {
	        if (driver.get() != null) {
	            driver.get().quit();
	            driver.remove();
	        }
	    }
}
