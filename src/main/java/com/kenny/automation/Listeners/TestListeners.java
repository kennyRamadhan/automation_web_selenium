package com.kenny.automation.Listeners;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.kenny.automation.Config.WebDriverManager;
import com.kenny.automation.Helper.CustomCommand;




/**
 * <h1>TestListeners</h1>
 * Implementasi custom TestNG {@link ITestListener} untuk mengelola event test 
 * seperti start, success, failure, skip, dan selesai eksekusi suite.
 *
 * <p><b>Fitur Utama:</b></p>
 * <ul>
 *   <li>Inisialisasi ExtentReports di awal suite</li>
 *   <li>Membuat node report untuk setiap test case</li>
 *   <li>Menangkap screenshot pada saat test sukses atau gagal</li>
 *   <li>Menambahkan log PASS/FAIL ke Extent Report</li>
 *   <li>Melakukan flush report setelah semua test selesai</li>
 * </ul>
 *
 * <p><b>Penggunaan:</b></p>
 * <pre>
 * &commat;Listeners(TestListeners.class)
 * public class AddNewClientTest {
 *     // Test case di sini
 * }
 * </pre>
 *
 * <p>class ini membantu membuat reporting lebih informatif dengan menambahkan screenshot 
 * dan log status setiap test case.</p>
 *
 * @author Kenny Ramadhan
 * @version 1.0
 */

public class TestListeners implements ITestListener{
	
	 ExtentReports extent = ExtentReportsManager.getExtentReports();
	 ExtentTest test;
	 
	 	/**
	     * Dipanggil sekali sebelum suite dimulai.
	     * Menginisialisasi ExtentReports dan menambahkan informasi suite.
	     */
	    @Override
	    public void onStart(ITestContext context) {
	        // Ambil instance dari ExtentReportsManager (sudah otomatis bikin folder & file)
	        extent = ExtentReportsManager.getExtentReports();

	        // Kalau perlu, update system info tambahan
	        extent.setSystemInfo("Test Suite", context.getSuite().getName());
	        
	     // --- Allure folder setup ---
	        String suiteName = context.getSuite().getName().toLowerCase();
	        String folder;
	        if (suiteName.contains("web")) {
	            folder = "allure-results-web";
	        } else if (suiteName.contains("api")) {
	            folder = "allure-results-api";
	        } else {
	            folder = "allure-results-other";
	        }

	        File dir = new File(folder);
	        if (!dir.exists()) dir.mkdirs();

	        System.setProperty("allure.results.directory", folder);
	        System.out.println("Allure results directory for suite: " + folder);
	    }

	    
	    /**
	     * Dipanggil setiap kali sebuah test method dimulai.
	     * Membuat node test baru di Extent Report dan reset counter log.
	     */
	    @Override
	    public void onTestStart(ITestResult result) {

	    	String testName = result.getMethod().getMethodName();
	    	System.out.println("[DEBUG] Creating Extent Test for: " + result.getMethod().getMethodName());
	    	ExtentNode.createTest(testName);
	    	LogHelper.resetCounter();
	    	LogHelper.setCurrentTestName(testName);
	    	
	    	// Ambil ExtentTest aktif untuk API logger
	        ExtentTest node = ExtentNode.getNode();
	    	ApiLogHelper apiLogger = new ApiLogHelper(node);
	        ApiLogManager.setLogger(apiLogger);
	    }
	    
	    
	    /**
	     * Dipanggil jika test berhasil.
	     * Menyimpan screenshot ke folder reports/pass dan melog status PASS.
	     */
	    @Override
	    public void onTestSuccess(ITestResult result) {
	    	
	    	 WebDriver webDriver = WebDriverManager.getDriver();
	        try {

	            if ( webDriver != null) {
	             	String screenshotBase64 = CustomCommand.captureScreenshotBase64(result.getMethod().getMethodName());
	             	
	                ExtentNode.getNode().addScreenCaptureFromBase64String(screenshotBase64,result.getMethod().getMethodName());
	               
	                LogHelper.pass("Test Success");
	            } else {
	                System.out.println("Driver is null, skipping screenshot for test: " + result.getMethod().getMethodName());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        
	        }
	        
			
	    }
	    
	    
	    /**
	     * Dipanggil jika test gagal.
	     * Menyimpan screenshot ke folder reports/fail dan melog status FAIL.
	     */
	    @Override
	    public void onTestFailure(ITestResult result) {    
	    	
	    	 WebDriver webDriver = WebDriverManager.getDriver();
	    	try {
	    		
	            if (webDriver != null) {
	            	String screenshotBase64 = CustomCommand.captureScreenshotBase64(result.getMethod().getMethodName());
	            	
	                ExtentNode.getNode().addScreenCaptureFromBase64String(screenshotBase64);
	              
	                LogHelper.fail("Test Failed");
	            } else {
	                System.out.println("Driver is null, skipping screenshot for test: " + result.getMethod().getMethodName());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	    	 ExtentNode.getNode().fail(result.getThrowable());
	    	 
	    	 ExtentTest node = ExtentNode.getNode();
	    	    if (node != null) {
	    	        node.fail(result.getThrowable());
	    	    } else {
	    	        System.out.println("[WARNING] ExtentNode.getNode() null. Logging to console instead.");
	    	        result.getThrowable().printStackTrace();
	    	    }
			
	    }
	    
	    
	    /**
	     * Dipanggil jika test dilewati (skip).
	     * Bisa ditambahkan log atau screenshot jika diperlukan.
	     */
	    @Override
	    public void onTestSkipped(ITestResult result) {
	    }
	    
	    

	    /**
	     * Dipanggil sekali setelah suite selesai dijalankan.
	     * Melakukan flush ExtentReports agar file report final dibuat.
	     */
	    @Override
	    public void onFinish(ITestContext context) {
	    	 System.out.println("Flushing Extent Report...");
	        extent.flush(); // Flush sekali di akhir suite
	        ExtentNode.remove();
	        System.out.println("Extent Report generated at: " +
	                System.getProperty("user.dir") + "/reports/");
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
