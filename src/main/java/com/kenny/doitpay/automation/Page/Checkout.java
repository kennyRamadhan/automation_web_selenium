package com.kenny.doitpay.automation.Page;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.kenny.doitpay.automation.Config.WebDriverManager;
import com.kenny.doitpay.automation.Helper.CustomCommand;
import com.kenny.doitpay.automation.Listeners.LogHelper;

/**
 * <h1>Checkout Page Object</h1>
 * <p>
 * Kelas ini merepresentasikan halaman <b>Checkout</b> pada aplikasi Swag Labs.
 * Semua aksi dan elemen di halaman checkout ditangani melalui metode di kelas ini.
 * <br><br>
 * Sesuai prinsip <b>Page Object Model (POM)</b>, kelas ini:
 * <ul>
 *   <li>Tidak berisi assertion atau logika verifikasi (hanya tindakan dan pengambilan data).</li>
 *   <li>Seluruh validasi dilakukan di layer test (misalnya di CheckoutTest).</li>
 *   <li>Menangani interaksi UI, termasuk input form, klik tombol, dan ekstraksi teks.</li>
 * </ul>
 * </p>
 * 
 * @author Kenny
 * @since 2025-11
 */
public class Checkout {

    /** Utility class untuk aksi custom seperti click, scroll, dan validasi elemen. */
    private final CustomCommand utils;

    /** Konstruktor untuk inisialisasi PageFactory dan CustomCommand. */
    public Checkout() {
        this.utils = new CustomCommand();
        PageFactory.initElements(WebDriverManager.getDriver(), this);
    }

    // ============================== LOCATORS ==============================

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(xpath = "//h3[normalize-space(text())='Error: Postal Code is required']")
    private WebElement errorMessagePostalCode;

    @FindBy(xpath = "//h3[normalize-space(text())='Error: First Name is required']")
    private WebElement errorMessageFirstName;

    @FindBy(xpath = "//h3[normalize-space(text())='Error: Last Name is required']")
    private WebElement errorMessageLastName;

    @FindBy(id = "continue")
    private WebElement continueBtn;

    @FindBy(id = "finish")
    private WebElement finishBtn;

    @FindBy(xpath = "//h2[normalize-space()='Thank you for your order!']")
    private WebElement completeOrderSuccessMessage;

    @FindBy(xpath = "//div[@data-test=\"inventory-item-price\"]")
    private List<WebElement> priceListOnCart;

    @FindBy(xpath = "//div[@class='summary_subtotal_label']")
    private WebElement subTotalLabel;

    @FindBy(xpath = "//div[@class='summary_tax_label']")
    private WebElement taxLabel;

    @FindBy(xpath = "//div[@class='summary_total_label']")
    private WebElement grandTotal;

    // ============================== ACTION METHODS ==============================

    /**
     * Menghitung total harga seluruh produk di keranjang (belum termasuk pajak).
     * 
     * @return total harga dalam format Double
     */
    public Double getTotalPriceInCart() {
        double totalAmount = 0.0;
        int itemIndex = 1;

        LogHelper.step("Menghitung total harga dari " + priceListOnCart.size() + " item di keranjang.");

        for (WebElement priceElement : priceListOnCart) {
            Double price = extractPriceFromLabel(priceElement, "Item Keranjang ke-" + itemIndex);
            if (price != null) totalAmount += price;
            itemIndex++;
        }

        LogHelper.detail("Total harga di keranjang tanpa pajak: " + totalAmount);
        return totalAmount;
    }

    /**
     * Menavigasi dari halaman produk ke halaman Checkout.
     */
    public void checkoutProducts() {
        LogHelper.step("Menavigasi ke halaman Checkout");
        utils.scrollIntoText("Checkout");
        utils.clickWhenReady(checkoutBtn);
        LogHelper.detail("Berhasil menampilkan halaman Checkout");
    }

    /**
     * Mengisi kolom <b>First Name</b> pada form Checkout.
     * 
     * @param firstName nama depan pengguna
     */
    public void inputFirstName(String firstName) {
        LogHelper.step("Input First Name");
        utils.sendKeysWhenReady(firstNameField, firstName);
        LogHelper.detail("Berhasil Input First Name Dengan :" +firstName);
    }

    /**
     * Mengisi kolom <b>Last Name</b> pada form Checkout.
     * 
     * @param lastName nama belakang pengguna
     */
    public void inputLastName(String lastName) {
        LogHelper.step("Input Last Name");
        utils.sendKeysWhenReady(lastNameField, lastName);
        LogHelper.detail("Berhasil Input Last Name Dengan :" +lastName);
    }

    /**
     * Mengisi kolom <b>Postal Code</b> pada form Checkout.
     * 
     * @param postalCode kode pos pengguna
     */
    public void inputPostalCode(String postalCode) {
        LogHelper.step("Input Postal Code");
        utils.sendKeysWhenReady(postalCodeField, postalCode);
        LogHelper.detail("Berhasil Input Postal Code Dengan :" +postalCode);
    }

    /**
     * Melanjutkan proses checkout dengan menekan tombol <b>Continue</b>.
     * <br><br>
     * Metode ini akan mendeteksi apakah form valid atau muncul pesan error.
     * 
     * @return {@code true} jika form valid (tidak muncul error), {@code false} jika ada error input
     */
    public boolean submitInformation() {
        LogHelper.step("Klik tombol Continue");
        utils.clickWhenReady(continueBtn);

        boolean hasError = false;

        if (utils.isElementPresent(errorMessageFirstName)) {
            LogHelper.detail("Error: First Name is required");
            hasError = true;
        }
        if (utils.isElementPresent(errorMessageLastName)) {
            LogHelper.detail("Error: Last Name is required");
            hasError = true;
        }
        if (utils.isElementPresent(errorMessagePostalCode)) {
            LogHelper.detail("Error: Postal Code is required");
            hasError = true;
        }

        return !hasError;
    }

    /**
     * Melakukan scroll hingga elemen tombol <b>Finish</b> terlihat di layar.
     */
    public void scrollToFinishOrder() {
        LogHelper.step("Scroll ke tombol Finish");
        utils.scrollIntoView(finishBtn);
        LogHelper.detail("Berhasil Menampilkan Button Finish dan Detail Harga");
    }

    /**
     * Mengambil nilai subtotal (sebelum pajak) dari halaman ringkasan pembayaran.
     * 
     * @return nilai subtotal dalam format Double
     */
    public Double getSubTotal() {
    	
    	LogHelper.step("Ekstrak Sub Total");
        Double value = extractPriceFromLabel(subTotalLabel, "SubTotal");
        LogHelper.detail("Berhasil Extract Sub Total");
        return value;
    }

    /**
     * Mengambil nilai pajak dari halaman ringkasan pembayaran.
     * 
     * @return nilai pajak dalam format Double
     */
    public Double getTax() {
    	LogHelper.step("Ekstrak Tax");
        Double value = extractPriceFromLabel(taxLabel, "Tax");
        LogHelper.detail("Berhasil Extract Tax");
        return value;
    }

    /**
     * Mengambil nilai total keseluruhan (subtotal + pajak) dari halaman ringkasan pembayaran.
     * 
     * @return nilai total keseluruhan dalam format Double
     */
    public Double getGrandTotal() {
    	LogHelper.step("Ekstrak Grand Total");
        Double value = extractPriceFromLabel(grandTotal, "Total");
        LogHelper.detail("Berhasil Extract Grand Total");
        return value;
    }

    /**
     * Menekan tombol <b>Finish</b> untuk menyelesaikan proses pembelian.
     */
    public void finishOrder() {
        LogHelper.step("Klik tombol Finish untuk menyelesaikan order");
        utils.clickWhenReady(finishBtn);
        LogHelper.detail("Checkout Sukses");
    }

    /**
     * Mengecek apakah pesan sukses <b>"Thank you for your order!"</b> muncul setelah menyelesaikan pesanan.
     * 
     * @return {@code true} jika pesan muncul, {@code false} jika tidak
     */
    public boolean isSuccessOrderDisplayed() {
    	
        return utils.isElementPresent(completeOrderSuccessMessage);
        
    }

    /**
     * Ekstraksi harga numerik dari label teks elemen (misalnya "$29.99").
     * 
     * @param element elemen WebElement yang berisi teks harga
     * @param labelName nama label yang sedang diekstrak (untuk logging)
     * @return nilai harga dalam format Double, atau {@code null} jika parsing gagal
     */
    private Double extractPriceFromLabel(WebElement element, String labelName) {
        String rawText = CustomCommand.getTextWithJS(element);
        if (rawText == null || rawText.isEmpty()) {
            LogHelper.detail("Teks untuk '" + labelName + "' kosong atau null.");
            return null;
        }

        String cleanPrice = rawText.replaceAll("[^0-9.]", "");
        try {
            double price = Double.parseDouble(cleanPrice);
            LogHelper.detail("Harga '" + labelName + "': " + price);
            return price;
        } catch (NumberFormatException e) {
            LogHelper.detail("Gagal mem-parsing harga dari teks '" + rawText + "'");
            return null;
        }
    }
}
