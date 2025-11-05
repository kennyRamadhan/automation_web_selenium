package com.kenny.doitpay.automation.Web;

import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.kenny.doitpay.automation.Helper.UtilsDataDriven;
import com.kenny.doitpay.automation.Listeners.LogHelper;
import com.kenny.doitpay.automation.Page.Login;
import com.opencsv.exceptions.CsvException;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Login Feature")
@Feature("Authentication")
public class LoginTest extends BaseTest {

    private Login login;
    private SoftAssert softAssert;

    @BeforeMethod
    public void setUpPage() {
        login = new Login();
        softAssert = new SoftAssert();
    }

    @DataProvider(name = "csvData")
    public Object[][] getCSVData() throws CsvException {
        String csvPath = System.getProperty("user.dir")
                + "/src/main/java/com/kenny/doitpay/automation/Resources/data.csv";
        return UtilsDataDriven.getTestData(csvPath, null);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "csvData")
    public void testLoginWithDataDrivenTesting(Map<String, String> data)
            throws MalformedURLException, URISyntaxException {

        String username = data.get("username");
        String password = data.get("password");

        login.performLogin(username, password);

        if (login.isLoginFailed()) {
            LogHelper.detail("Login gagal dengan user: " + username + " → Pesan: " + login.getErrorMessage());
            softAssert.fail("Login gagal dengan user: " + username);
        } else {
            softAssert.assertTrue(login.isLoginSuccess(),
                    "Login tidak berhasil padahal seharusnya sukses untuk user: " + username);
        }

        softAssert.assertAll();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginWithValidCredentials() {
        login.performLoginWithHardcoded("standard_user");
        softAssert.assertTrue(login.isLoginSuccess(), "Login gagal padahal user valid.");
        softAssert.assertAll();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginWithInvalidCredentials() {
    	login.performLogin("invalid_username", "wrong_password");
        softAssert.assertTrue(login.isLoginFailed(), "Login berhasil padahal user invalid.");
        softAssert.assertAll();
    }
}
