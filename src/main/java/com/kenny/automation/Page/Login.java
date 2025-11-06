package com.kenny.automation.Page;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.kenny.automation.Config.WebDriverManager;
import com.kenny.automation.Helper.CustomCommand;
import com.kenny.automation.Listeners.LogHelper;

/**
 * Page Object class yang merepresentasikan halaman Login.
 * 
 * Class ini hanya berfokus pada interaksi UI dan pengambilan state.
 * Tidak ada assertion di sini — seluruh verifikasi dilakukan di layer test.
 */
public class Login {

    private final CustomCommand utils;
    private final Map<String, String> credentials = new HashMap<>();

    public Login() {
        this.utils = new CustomCommand();
        PageFactory.initElements(WebDriverManager.getDriver(), this);

        credentials.put("standard_user", "secret_sauce");
        credentials.put("locked_out_user", "secret_sauce");
        credentials.put("problem_user", "secret_sauce");
        credentials.put("performance_glitch_user", "secret_sauce");
        credentials.put("error_user", "secret_sauce");
        credentials.put("visual_user", "secret_sauce");
    }

    // ======================= Locators =======================
    @FindBy(id = "user-name")
    private WebElement inputUserName;

    @FindBy(id = "password")
    private WebElement inputPassword;

    @FindBy(id = "login-button")
    private WebElement loginBtn;

    @FindBy(xpath = "//span[@class='title']")
    private WebElement verifySuccessLogin;

    @FindBy(xpath = "//button[@class='error-button']")
    private WebElement errorMessageLogin;

    // ======================= Methods =======================

    /**
     * Melakukan login dengan kredensial hardcoded.
     */
    public void performLoginWithHardcoded(String username) {
        LogHelper.step("Melakukan login dengan user: " + username);
        if (!credentials.containsKey(username)) {
            throw new IllegalArgumentException("Username '" + username + "' tidak dikenali.");
        }

        String password = credentials.get(username);
        utils.sendKeysWhenReady(inputUserName, username);
        utils.sendKeysWhenReady(inputPassword, password);
        utils.clickWhenReady(loginBtn);
        utils.sleep(1000);
    }

    /**
     * Melakukan login menggunakan data eksternal (Data Driven).
     */
    public void performLogin(String username, String password) {
        LogHelper.step("Melakukan login dengan data eksternal: " + username);
        utils.sendKeysWhenReady(inputUserName, username);
        utils.sendKeysWhenReady(inputPassword, password);
        utils.clickWhenReady(loginBtn);
    }
    
    
    

    // ======================= State Checkers =======================

    /**
     * Mengecek apakah login berhasil.
     */
    public boolean isLoginSuccess() {
        return utils.isElementPresent(verifySuccessLogin);
    }

    /**
     * Mengecek apakah login gagal (error message muncul).
     */
    public boolean isLoginFailed() {
        return utils.isElementPresent(errorMessageLogin);
    }

    /**
     * Mengambil teks error (jika ada).
     */
    public String getErrorMessage() {
        return utils.getTextIfPresent(errorMessageLogin);
    }
}
