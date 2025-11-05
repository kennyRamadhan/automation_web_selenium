package com.kenny.doitpay.automation.Web;

import org.testng.annotations.Test;
import org.testng.Assert;
import java.util.Map;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.kenny.doitpay.automation.Helper.UtilsDataDriven;
import com.kenny.doitpay.automation.Listeners.LogHelper;
import com.kenny.doitpay.automation.Page.Checkout;
import com.kenny.doitpay.automation.Page.Dashboard;
import com.kenny.doitpay.automation.Page.Login;
import com.opencsv.exceptions.CsvException;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;



/**
 * CheckoutTest adalah class test yang mencakup skenario pengujian terkait proses checkout
 * pada aplikasi web e-commerce.
 * <p>
 * Test menggunakan pendekatan Data-Driven Testing (DDT) dengan data yang disuplai melalui CSV.
 * Test ini mencakup pemilihan produk, verifikasi keranjang, reset app state, dan alur checkout
 * End-to-End (E2E) dengan skenario positif maupun negatif.
 * </p>
 * @author Kenny
 * @since 2025
 */
public class CheckoutTest extends BaseTest {
	
	private Login login;
	private Dashboard dashboard;
	private Checkout checkout;
	private SoftAssert softAssert;

	
	  /**
     * Inisialisasi objek Page Object sebelum setiap test method dijalankan.
     */
	@BeforeMethod(alwaysRun = true)
	public void setUpPage() {

		login = new Login();
		dashboard = new Dashboard();
		checkout = new Checkout();
		softAssert = new SoftAssert();
	}
	
	
	/**
	 * Provider data untuk membaca file excel maupun csv, gunakan path dan sheet
	 * name untuk excel dan gunakan path file serta kosongkan sheet name untuk csv
	 * 
	 * @return csv files/excel files
	 * @throws CsvException
	 */
	@DataProvider(name = "csvData")
	public Object[][] getCsvData() throws CsvException {
		String csvPath = System.getProperty("user.dir")
				+ "/src/main/java/com/kenny/doitpay/automation/Resources/data.csv";
		return UtilsDataDriven.getTestData(csvPath, null);
	}
	
	
	
	
	
	
    /**
     * Test case untuk memverifikasi proses penambahan produk ke keranjang
     * melalui halaman detail produk menggunakan pendekatan Data-Driven Testing (DDT).
     * <p>
     * Data disediakan melalui {@code csvData} yang berisi:
     * <ul>
     *   <li><b>productname</b> - Nama produk yang akan dipilih dari halaman dashboard.</li>
     * </ul>
     * 
     * <p>
     * Alur pengujian:
     * <ol>
     *   <li>Login menggunakan kredensial hardcoded (standard_user).</li>
     *   <li>Pilih produk berdasarkan nama yang diambil dari file CSV.</li>
     *   <li>Verifikasi lanjutan seperti penambahan ke cart dapat dilakukan di tahap berikutnya.</li>
     * </ol>
     * 
     * <p>
     * Test ini difokuskan pada navigasi dan pemilihan produk secara dinamis
     * tanpa hardcode nama produk, memastikan fleksibilitas terhadap data eksternal.
     *
     * @param data Map yang berisi pasangan key-value hasil pembacaan file CSV.
     *             Minimal berisi key <b>productname</b>.
     *
     * @see com.kenny.doitpay.automation.Web.DashboardPage#selectProduct(String)
     * @see #getCsvData() untuk definisi DataProvider yang menyuplai data CSV
     */
	@Epic("Checkout")
	@Feature("Select Products")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "csvData")
	public void addProductToCartFromDetailProduct(Map<String, String> data) {
		
		login.performLoginWithHardcoded("standard_user");
        softAssert.assertTrue(login.isLoginSuccess(), "Login gagal padahal user valid.");
		dashboard.selectProduct(data.get("productname"));
		
	}
	
	
	
	
	
	
	
	
	 /**
     * Test case untuk memverifikasi bahwa fitur "Reset App State" berfungsi dengan benar.
     * <p>
     * Tujuan utama test ini adalah memastikan bahwa seluruh item di keranjang
     * dihapus setelah dilakukan reset, dan kondisi aplikasi kembali seperti semula.
     * 
     * <p>
     * Alur pengujian:
     * <ol>
     *   <li>Login menggunakan kredensial valid.</li>
     *   <li>Tambahkan seluruh produk ke keranjang.</li>
     *   <li>Pastikan keranjang berisi item sebelum reset.</li>
     *   <li>Buka menu hamburger dan pilih "Reset App State".</li>
     *   <li>Logout, kemudian login kembali.</li>
     *   <li>Verifikasi bahwa keranjang kosong setelah reset.</li>
     * </ol>
     * 
     * <p>
     * Seluruh verifikasi dilakukan menggunakan {@link org.testng.asserts.SoftAssert}
     * untuk memastikan hasil akhir tetap dievaluasi meskipun salah satu langkah gagal.
     *
     * @param data Map hasil pembacaan CSV (opsional, digunakan untuk kompatibilitas DDT).
     *
     * @see com.kenny.doitpay.automation.Web.DashboardPage#selectAllProductsToCart()
     * @see com.kenny.doitpay.automation.Web.DashboardPage#resetStateApp()
     * @see com.kenny.doitpay.automation.Web.DashboardPage#verifyCartIsEmptyAfterReset()
     * @see #getCsvData() untuk definisi DataProvider CSV
     */
	
	@Epic("Reset APP")
	@Feature("Reset APP State")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "csvData")
	public void verifyResetAppState(Map<String, String> data) {
		login.performLoginWithHardcoded("standard_user");
        softAssert.assertTrue(login.isLoginSuccess(), "Login gagal padahal user valid.");
		dashboard.selectAllProductsToCart();
        int cartCountBefore = dashboard.getCartItemCount();
		softAssert.assertTrue(cartCountBefore > 0, "Keranjang seharusnya berisi item sebelum reset");
		dashboard.openHamburgerMenu();
		dashboard.resetAppState();
		dashboard.logout();
		login.performLoginWithHardcoded("standard_user");
        softAssert.assertTrue(login.isLoginSuccess(), "Login gagal padahal user valid.");
	    int cartCountAfter = dashboard.getCartItemCount();
	    softAssert.assertEquals(cartCountAfter, 0, "Keranjang seharusnya kosong setelah reset app state.");
	    softAssert.assertAll();
	}
	
	
	
	
	
	
    /**
     * End-to-End test untuk proses checkout produk pada aplikasi web
     * menggunakan pendekatan Data-Driven Testing (DDT).
     * <p>
     * Test ini membaca data dari CSV (melalui DataProvider bernama {@code csvCredentials})
     * yang berisi field seperti:
     * <ul>
     *   <li><b>firstname</b> - Nama depan pelanggan</li>
     *   <li><b>lastname</b> - Nama belakang pelanggan</li>
     *   <li><b>postalcode</b> - Kode pos pelanggan</li>
     *   <li><b>productname</b> - Nama produk yang akan di-checkout</li>
     *   <li><b>type</b> - Jenis skenario: "positive" atau "negative"</li>
     * </ul>
     * <p>
     * Alur pengujian mencakup:
     * <ol>
     *   <li>Login menggunakan kredensial valid.</li>
     *   <li>Menambahkan semua produk ke cart.</li>
     *   <li>Menghitung subtotal yang diharapkan dari cart.</li>
     *   <li>Melakukan proses checkout dengan data dari CSV.</li>
     *   <li>Jika type = <b>positive</b>:
     *     <ul>
     *       <li>Verifikasi bahwa informasi checkout valid.</li>
     *       <li>Bandingkan subtotal dan grand total antara cart dan summary.</li>
     *       <li>Selesaikan pemesanan dan verifikasi pesan sukses.</li>
     *     </ul>
     *   </li>
     *   <li>Jika type = <b>negative</b>:
     *     <ul>
     *       <li>Verifikasi bahwa sistem menampilkan validasi kesalahan input.</li>
     *     </ul>
     *   </li>
     *   <li>Jika kolom 'type' tidak valid, test akan gagal dengan pesan eksplisit.</li>
     * </ol>
     *
     * <p>
     * Semua verifikasi numerik dilakukan menggunakan {@link org.testng.asserts.SoftAssert}
     * dengan toleransi delta 0.001 agar akurat untuk nilai desimal.
     *
     * <p>
     * Exception selama eksekusi akan menyebabkan test gagal dengan log terperinci.
     *
     * @param data Map yang merepresentasikan satu baris data dari file CSV, 
     *             berisi pasangan key-value sesuai kolom CSV.
     *
     * @see #getCsvData() untuk definisi DataProvider yang menyediakan data CSV
     * @see com.kenny.doitpay.automation.helper.LogHelper untuk logging langkah pengujian
     * @see com.kenny.doitpay.automation.Web.CheckoutPage untuk tindakan checkout
     */
	
	@Epic("Checkout")
	@Feature("Select Products & Checkout")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "csvData")
	public void flowCheckoutProductsE2E(Map<String, String> data) {
		

	    try {
	    	login.performLoginWithHardcoded("standard_user");
	        softAssert.assertTrue(login.isLoginSuccess(), "Login gagal padahal user valid.");
	        dashboard.selectAllProductsToCart();

	        Double expectedSubTotal = checkout.getTotalPriceInCart();

	        checkout.checkoutProducts();
	        checkout.inputFirstName(data.get("firstname"));
	        checkout.inputLastName(data.get("lastname"));
	        checkout.inputPostalCode(data.get("postalcode"));

	        boolean infoValid = checkout.submitInformation();

	        if (data.get("type").equalsIgnoreCase("positive")) {
	            Assert.assertTrue(infoValid, "Form informasi checkout seharusnya valid tapi muncul error!");

	            checkout.scrollToFinishOrder();

	            Double actualSubTotal = checkout.getSubTotal();
	            Double tax = checkout.getTax();
	            Double expectedGrandTotal = actualSubTotal + tax;
	            Double actualGrandTotal = checkout.getGrandTotal();

	            LogHelper.step("Verifikasi SubTotal dan Grand Total");
	            softAssert.assertEquals(actualSubTotal, expectedSubTotal, 0.001,
	                    "Subtotal tidak sesuai antara cart dan summary.");
	            softAssert.assertEquals(actualGrandTotal, expectedGrandTotal, 0.001,
	                    "Grand Total tidak sesuai.");

	            checkout.finishOrder();
	            softAssert.assertTrue(checkout.isSuccessOrderDisplayed(),
	                    "Pesan 'Thank you for your order!' tidak muncul.");
	        }

	        else if (data.get("type").equalsIgnoreCase("negative")) {
	        	   softAssert.assertFalse(infoValid,
	                    "Form checkout seharusnya invalid tetapi tidak muncul pesan error!");
	        }

	        else {
	        	   softAssert.fail("Nilai kolom 'type' tidak valid. Gunakan 'positive' atau 'negative'.");
	        }

	    } catch (Exception e) {
	        softAssert.fail("Test case gagal karena exception: " + e.getMessage());
	    } finally {
	        softAssert.assertAll();
	    }
	    
	}
	


}
