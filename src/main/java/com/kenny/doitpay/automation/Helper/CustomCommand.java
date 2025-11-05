package com.kenny.doitpay.automation.Helper;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.kenny.doitpay.automation.Config.WebDriverManager;
import java.util.function.Supplier;


/**
 * CustomCommand berisi kumpulan utilitas umum untuk interaksi Web UI
 * seperti klik aman, scroll, input teks, verifikasi elemen, dan capture screenshot.
 * 
 * Semua method menggunakan instance driver tunggal dari WebDriverManager
 * untuk menghindari pembuatan WebDriver baru secara tidak sengaja.
 */
public class CustomCommand {

    /** Timeout default untuk explicit wait */
    private static final int DEFAULT_TIMEOUT = 60;
    
    
    private static final int RETRY_COUNT = 3; // jumlah retry jika stale
    private static final int SLEEP_MS = 300;  // delay setelah scroll


    /**
     * Mengembalikan instance WebDriver yang aktif dengan validasi.
     *
     * @return WebDriver aktif
     * @throws IllegalStateException jika WebDriver belum diinisialisasi
     */
    private static WebDriver getDriverSafe() {
        if (WebDriverManager.getDriver() == null) {
            throw new IllegalStateException(
                "Driver belum diinisialisasi! Pastikan DriverManager.setDriver() dipanggil di BaseTest.setUp()");
        }
        return WebDriverManager.getDriver();
    }

    /**
     * Mengisi teks ke dalam elemen setelah elemen siap diklik dan visible.
     * Jika nilai text adalah null, maka tidak mengisi apa pun dan hanya mencatat peringatan.
     *
     * @param element WebElement target
     * @param text    teks yang akan diinput (boleh null)
     */
    public void sendKeysWhenReady(WebElement element, String text) {
        WebDriver driver = getDriverSafe();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));

        element.clear();

        if (text == null) {
            System.out.println(" Nilai 'null' diterima untuk elemen: " + element + " — input dilewati.");
            return; 
        }

        element.sendKeys(text);
        System.out.println(" Input text: '" + text + "' pada elemen: " + element);
    }


    /**
     * Melakukan klik aman pada elemen yang siap diklik.
     * Jika gagal, menggunakan fallback JavaScript click.
     *
     * @param element WebElement target
     */
    public void clickWhenReady(WebElement element) {
        WebDriverWait wait = new WebDriverWait(WebDriverManager.getDriver(), Duration.ofSeconds(10));
        int attempts = 0;

        while (attempts < 2) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (StaleElementReferenceException e) {
              System.out.println("Elemen stale saat klik, mencoba ulang...");
                attempts++;
            }
        }

        throw new RuntimeException("Gagal klik elemen setelah beberapa percobaan.");
    }


    /**
     * Memverifikasi bahwa elemen tampil di halaman dalam batas waktu tertentu.
     *
     * @param element WebElement yang akan diverifikasi
     * @throws RuntimeException jika elemen tidak muncul dalam durasi timeout
     */
    public void verifyElementExist(WebElement element) {
        WebDriver driver = getDriverSafe();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            System.out.println("Element ditemukan: " + element);
        } catch (TimeoutException e) {
            throw new RuntimeException("Element tidak ditemukan: " + element, e);
        }
    }
    
    
    /**
     * 
     * @param element
     * @return
     */
    public boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }


    /**
     * Memverifikasi bahwa elemen tidak tampil atau sudah hilang dari halaman.
     *
     * @param by locator elemen target
     * @throws RuntimeException jika elemen masih muncul setelah timeout
     */
    public void verifyElementNotExist(By by) {
        WebDriver driver = getDriverSafe();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            if (invisible) {
                System.out.println("Element tidak ditemukan (seperti yang diharapkan): " + by);
            }
        } catch (TimeoutException e) {
            throw new RuntimeException("Element masih muncul padahal seharusnya tidak: " + by);
        }
    }

    /**
     * Mengambil teks dari elemen yang sudah tampil.
     *
     * @param element WebElement target
     * @return teks dari elemen
     */
    public String getTextWhenReady(WebElement element) {
        WebDriver driver = getDriverSafe();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.visibilityOf(element));
        String text = element.getText();
        System.out.println("Teks dari elemen: " + text);
        return text;
    }
    
    /**
     * Mengambil teks dari elemen menggunakan JavaScript (textContent).
     * <p>
     * Metode ini SANGAT BERGUNA untuk mengambil teks dari elemen
     * yang tidak terlihat di layar (di luar viewport/perlu scroll),
     * di mana .getText() standar akan mengembalikan string kosong.
     * </p>
     * * @param element WebElement target
     * @return teks dari elemen (sudah di-trim), atau string kosong jika gagal
     */
    public static String getTextWithJS(WebElement element) {
        WebDriver driver = getDriverSafe(); // Menggunakan helper internal Anda
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // 'textContent' akan mengambil teks bahkan jika elemen tersembunyi
            String text = (String) js.executeScript("return arguments[0].textContent;", element);
            
            if (text != null) {
                return text.trim();
            } else {
                return ""; // Menghindari NullPointerException
            }
            
        } catch (Exception e) {
            System.out.println("[WARNING] Gagal mengambil teks via JS: " + e.getMessage() + ". Mencoba fallback .getText().");
            
            // Fallback ke getText() biasa jika JS gagal (meskipun mungkin akan kosong)
            try {
                 String fallbackText = element.getText();
                 return (fallbackText != null) ? fallbackText.trim() : "";
            } catch (Exception e2) {
                System.out.println("[ERROR] Fallback .getText() juga gagal: " + e2.getMessage());
                return ""; // Kembalikan string kosong jika semua gagal
            }
        }
    }

    /**
     * Scroll halaman hingga elemen dengan teks tertentu terlihat.
     *
     * @param text teks target yang dicari
     * @throws RuntimeException jika elemen tidak ditemukan setelah beberapa kali scroll
     */
    public void scrollIntoText(String text) {
        WebDriver driver = getDriverSafe();
        boolean found = false;
        int maxScroll = 5;

        String xpath = "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                + text.toLowerCase() + "')]";

        for (int i = 0; i < maxScroll; i++) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(xpath));

                if (!elements.isEmpty()) {
                    WebElement el = elements.get(0);
                    ((JavascriptExecutor) driver)
                            .executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", el);
                    System.out.println("Found and scrolled to element with text: " + text);
                    found = true;
                    break;
                } else {
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 400);");
                    Thread.sleep(400);
                }
            } catch (Exception e) {
                System.out.println("Scroll attempt " + (i + 1) + " failed: " + e.getMessage());
            }
        }

        if (!found) {
            throw new RuntimeException(
                    "Element with text '" + text + "' not found after " + maxScroll + " scroll attempts.");
        }
    }

    /**
     * Scroll ke elemen agar terlihat di tengah layar.
     *
     * @param element WebElement target
     */
    public  void scrollIntoView(WebElement element) {
        WebDriver driver = getDriverSafe();
        int attempts = 0;
        while (attempts < RETRY_COUNT) {
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
                Thread.sleep(SLEEP_MS);
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
                System.out.println("Stale element, retry " + attempts);
                // coba ambil ulang element dari DOM jika WebElement memiliki By tersimpan
                // ini opsional, kalau kamu punya reference By
            } catch (Exception e) {
                System.out.println("Failed scroll into view: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Scroll ke bagian paling atas halaman (koordinat 0,0).
     */
    public void scrollToTop() {
        ((JavascriptExecutor) getDriverSafe()).executeScript("window.scrollTo(0, 0);");
        System.out.println("Scrolled to top");
    }

    /**
     * Scroll halaman secara vertikal berdasarkan jumlah pixel tertentu.
     *
     * @param pixels jumlah pixel (positif = ke bawah, negatif = ke atas)
     */
    public void scrollByPixel(int pixels) {
        WebDriver driver = getDriverSafe();
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + pixels + ");");
        System.out.println("Scrolled by pixel: " + pixels);
    }

    /**
     * Scroll ke posisi absolut berdasarkan koordinat X dan Y.
     *
     * @param x posisi horizontal (X)
     * @param y posisi vertikal (Y)
     */
    public void scrollByCoordinate(int x, int y) {
        WebDriver driver = getDriverSafe();
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(arguments[0], arguments[1]);", x, y);
        System.out.println("Scrolled to coordinate X: " + x + ", Y: " + y);
    }

    /**
     * Klik di posisi koordinat tertentu di halaman menggunakan JavaScript.
     * Cocok digunakan ketika elemen tidak dapat diklik langsung (misalnya tertutup overlay).
     *
     * @param x posisi horizontal (X)
     * @param y posisi vertikal (Y)
     */
    public void clickByCoordinate(int x, int y) {
        WebDriver driver = getDriverSafe();
        String script = "var evt = new MouseEvent('click', {clientX: arguments[0], clientY: arguments[1], view: window, bubbles: true, cancelable: true});"
                + "document.elementFromPoint(arguments[0], arguments[1]).dispatchEvent(evt);";
        ((JavascriptExecutor) driver).executeScript(script, x, y);
        System.out.println("Clicked at coordinate X: " + x + ", Y: " + y);
    }

    /**
     * Menunggu hingga elemen terlihat (visible) dan mengembalikannya.
     *
     * @param by locator elemen target
     * @return WebElement setelah visible
     */
    public WebElement waitUntilVisible(By by) {
        WebDriver driver = getDriverSafe();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        System.out.println("Elemen visible: " + by);
        return element;
    }

    /**
     * Delay sederhana tanpa throws InterruptedException.
     *
     * @param millis durasi tunggu dalam milidetik
     */
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Mengambil screenshot dalam format Base64 untuk embed ke laporan HTML.
     *
     * @param stepName nama langkah yang sedang dijalankan
     * @return base64 string dari screenshot
     */
    @SuppressWarnings("deprecation")
	public static String captureScreenshotBase64(String stepName) {
        WebDriver driver = getDriverSafe();
        if (driver == null)
            return null;
        
        // Debug log thread dan nama step
        System.out.println("[DEBUG] Thread " + Thread.currentThread().getId() + " capture screenshot for step: " + stepName);

        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.out.println("[WARNING] Gagal capture screenshot base64: " + e.getMessage());
            return null;
        }
    }
    
    
    public String getTextIfPresent(WebElement element) {
        try {
            if (isElementPresent(element)) {
                return getTextWithJS(element).trim();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    
    public <T> T refreshElement(Supplier<T> elementSupplier) {
        try {
            return elementSupplier.get();
        } catch (StaleElementReferenceException e) {
            System.out.println("Elemen stale, mencoba refresh element...");
            return elementSupplier.get(); // ambil ulang lewat mekanisme PageFactory
        }
    }

}
