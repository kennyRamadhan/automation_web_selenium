package com.kenny.doitpay.automation.Web;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;

import com.kenny.doitpay.automation.Config.ChromeDriverFactory;
import com.kenny.doitpay.automation.Config.WebDriverManager;

/**
 * BaseTest adalah kelas dasar (base class) untuk semua test case Web menggunakan Selenium WebDriver.
 * <p>
 * class ini menyediakan setup dan teardown WebDriver secara otomatis sebelum dan setelah setiap metode
 * test menggunakan anotasi TestNG {@link BeforeMethod} dan {@link AfterMethod}.
 * </p>
 * <p>
 * Driver yang digunakan dikelola oleh {@link WebDriverManager}, sehingga setiap thread memiliki instance
 * driver yang aman untuk parallel test execution.
 * </p>
 * @author Kenny Ramadhan
 * @version 1.0
 */
public class BaseTest {

    /**
     * Instance WebDriver yang akan digunakan di test case turunannya.
     */
    protected WebDriver driver;

    /**
     * Menyiapkan WebDriver sebelum setiap metode test dijalankan.
     * <p>
     * - Menginisialisasi WebDriver melalui {@link WebDriverManager} menggunakan {@link ChromeDriverFactory}.
     * - Memaksimalkan jendela browser.
     * - Membuka URL default aplikasi (https://www.saucedemo.com).
     * </p>
     * @throws InterruptedException 
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.initDriver(new ChromeDriverFactory());
        driver = WebDriverManager.getDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");
    }

    /**
     * Menutup dan membersihkan WebDriver setelah setiap metode test selesai dijalankan.
     * <p>
     * Memastikan driver dihentikan dengan benar dan dihapus dari {@link WebDriverManager} untuk
     * mencegah memory leak.
     * </p>
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            WebDriverManager.quitDriver();  
            System.out.println("Driver closed and removed successfully.");
        } catch (Exception e) {
            System.out.println("Warning during driver quit: " + e.getMessage());
        }
    }
}
