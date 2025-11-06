package com.kenny.automation.Config;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.openqa.selenium.WebDriver;

public class WebDriverPool {
    private static final int MAX_POOL_SIZE = 3;
    private static final Queue<WebDriver> available = new LinkedList<>();
    private static final Set<WebDriver> inUse = new HashSet<>();

    static {
        try {
            DriverFactory factory = new ChromeDriverFactory();
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                available.add(factory.createDriver());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize WebDriverPool", e);
        }
    }

    public synchronized static WebDriver acquireDriver() throws InterruptedException {
        while (available.isEmpty()) {
            WebDriverPool.class.wait();
        }
        WebDriver driver = available.poll();
        inUse.add(driver);
        return driver;
    }

    public synchronized static void releaseDriver(WebDriver driver) {
        if (driver != null) {
            inUse.remove(driver);
            available.add(driver);
            WebDriverPool.class.notifyAll();
        }
    }
}
