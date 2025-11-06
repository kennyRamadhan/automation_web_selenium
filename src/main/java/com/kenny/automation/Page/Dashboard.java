package com.kenny.automation.Page;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kenny.automation.Config.WebDriverManager;
import com.kenny.automation.Helper.CustomCommand;
import com.kenny.automation.Listeners.LogHelper;

/**
 * Dashboard adalah Page Object yang merepresentasikan halaman utama aplikasi e-commerce.
 * <p>
 * Class ini menyediakan metode untuk berinteraksi dengan elemen UI pada halaman dashboard,
 * termasuk memilih produk, menambahkan ke keranjang, membuka menu navigasi, reset aplikasi,
 * dan logout. 
 * </p>
 * <p>
 * Semua verifikasi (assertion) telah dipindahkan ke layer test agar
 * Page Object hanya fokus pada interaksi dan pengambilan data.
 * </p>
 * 
 * @author Kenny
 * @version 2.0 (clean POM)
 */
public class Dashboard {

    private final CustomCommand utils;

    /**
     * Konstruktor Dashboard.
     * <p>
     * Menginisialisasi elemen halaman menggunakan {@link PageFactory} dan membuat instance
     * {@link CustomCommand}.
     * </p>
     */
    public Dashboard() {
        this.utils = new CustomCommand();
        PageFactory.initElements(WebDriverManager.getDriver(), this);
    }

    @FindBy(xpath = "//select[@class='product_sort_container']")
    private WebElement filterDropdown;

    @FindBy(xpath = "//div[@data-test=\"inventory-item-name\"]")
    private List<WebElement> productNames;

    @FindBy(xpath = "//button[@class='btn btn_primary btn_small btn_inventory ']")
    private List<WebElement> productListAddToCartBtn;

    @FindBy(xpath = "//a[@class='shopping_cart_link']")
    private WebElement cartIcon;

    private By cartBadge = By.xpath("//span[@class='shopping_cart_badge']");

    @FindBy(id = "add-to-cart")
    private WebElement addToCartInDetailProduct;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerBtn;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateBtn;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutBtn;

    /**
     * Mengklik tombol hamburger untuk membuka menu navigasi.
     */
    public void openHamburgerMenu() {
        LogHelper.step("Membuka Navigasi");
        utils.clickWhenReady(burgerBtn);
        LogHelper.detail("Berhasil menampilkan navigasi");
    }

    /**
     * Melakukan reset state aplikasi melalui menu navigasi.
     */
    public void resetAppState() {
        LogHelper.step("Melakukan Reset App");
        utils.clickWhenReady(resetAppStateBtn);
        LogHelper.detail("Berhasil melakukan Reset App");
    }

    /**
     * Logout user dari aplikasi.
     */
    public void logout() {
        LogHelper.step("Melakukan Log out User");
        utils.clickWhenReady(logoutBtn);
        LogHelper.detail("Berhasil Log out dan menampilkan halaman Login");
    }

    /**
     * Memilih dan menambahkan produk ke keranjang berdasarkan nama produk secara dinamis.
     * <p>
     * Metode ini mengembalikan nilai boolean untuk mengindikasikan apakah produk berhasil ditemukan dan ditambahkan.
     * </p>
     *
     * @param name nama produk yang ingin dipilih dan ditambahkan ke keranjang
     * @return true jika produk berhasil ditambahkan, false jika tidak ditemukan
     */
    public boolean selectProduct(String name) {
        LogHelper.step("Memilih produk dengan nama: " + name);
        boolean found = false;

        try {
            List<WebElement> products = utils.refreshElement(() -> productNames);

            for (WebElement product : products) {
                String productText = product.getText().trim();
                if (productText.equalsIgnoreCase(name)) {
                    utils.clickWhenReady(product);
                    utils.clickWhenReady(addToCartInDetailProduct);
                    LogHelper.detail("Produk '" + name + "' berhasil ditambahkan ke keranjang.");
                    found = true;
                    break;
                }
            }

            if (!found) {
                LogHelper.detail("Produk '" + name + "' tidak ditemukan di halaman Dashboard.");
            }

        } catch (StaleElementReferenceException e) {
            LogHelper.detail("Terjadi stale element, mencoba ulang untuk produk: " + name);
            selectProduct(name); // recursive retry sekali
        }

        return found;
    }

    /**
     * Menambahkan seluruh produk yang terlihat di halaman ke dalam keranjang belanja secara otomatis.
     * <p>
     * Metode ini akan mengklik semua tombol "Add to cart" yang masih aktif hingga semua produk
     * berhasil ditambahkan.
     * </p>
     *
     * @return total jumlah produk yang berhasil ditambahkan ke keranjang
     */
    @SuppressWarnings("static-access")
    public int selectAllProductsToCart() {
        LogHelper.step("Menambahkan semua produk yang tersedia ke keranjang");

        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        int totalAdded = 0;

        while (true) {
            List<WebElement> addButtons = driver.findElements(
                    By.xpath("//button[contains(normalize-space(.), 'Add to cart')]"));
            if (addButtons.isEmpty()) {
                utils.scrollToTop();
                LogHelper.detail("Semua produk berhasil ditambahkan ke keranjang.");
                break;
            }

            for (WebElement addButton : addButtons) {
                try {
                    utils.clickWhenReady(addButton);
                    totalAdded++;
                    wait.until(ExpectedConditions.attributeContains(addButton, "class", "btn_secondary"));
                    LogHelper.detail("Produk ke-" + totalAdded + " berhasil ditambahkan ke keranjang.");
                } catch (StaleElementReferenceException ignored) {
                    // skip elemen yang hilang dari DOM
                } catch (TimeoutException te) {
                    LogHelper.detail("Timeout: tombol tidak berubah menjadi 'Remove' setelah diklik.");
                } catch (Exception e) {
                    LogHelper.detail("Gagal menambahkan produk: " + e.getMessage());
                }
            }

            WebElement lastProduct = addButtons.get(addButtons.size() - 1);
            utils.scrollIntoView(lastProduct);
            utils.sleep(500);
        }

        LogHelper.step("Membuka halaman Cart");
        try {
            utils.clickWhenReady(cartIcon);
            LogHelper.detail("Berhasil membuka halaman Cart.");
        } catch (Exception e) {
            LogHelper.detail("Gagal membuka halaman Cart: " + e.getMessage());
        }

        return totalAdded;
    }

    /**
     * Mengecek apakah keranjang memiliki produk.
     * 
     * @return jumlah item di keranjang, atau 0 jika kosong.
     */
    public int getCartItemCount() {
        WebDriver driver = WebDriverManager.getDriver();
        try {
            List<WebElement> badges = driver.findElements(cartBadge);
            if (!badges.isEmpty()) {
                String countText = badges.get(0).getText().trim();
                return Integer.parseInt(countText);
            }
        } catch (Exception e) {
            LogHelper.detail("Tidak dapat membaca jumlah keranjang: " + e.getMessage());
        }
        return 0;
    }
}
