package com.kenny.automation.Helper;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;


/**
 * <h1>ConfigLoader</h1> Utility class untuk membaca file konfigurasi
 * `config.properties` dan menyediakan method helper untuk mendapatkan value
 * berdasarkan key.
 *
 * <p>
 * <b>Fungsi utama:</b>
 * </p>
 * <ul>
 * <li>Memuat konfigurasi dari file
 * <code>src/main/java/MBI/DST/Resources/config.properties</code> saat class
 * pertama kali dipanggil.</li>
 * <li>Menyediakan method untuk mengambil value konfigurasi secara langsung
 * (<code>get()</code>).</li>
 * <li>Menyediakan method dengan fallback ke default value jika key tidak
 * ditemukan (<code>getOrDefault()</code>).</li>
 * <li>Mengecek apakah suatu key memiliki value yang valid
 * (<code>has()</code>).</li>
 * </ul>
 *
 * <p>
 * <b>Contoh Penggunaan:</b>
 * </p>
 * 
 * <pre>
 * Gunakan sebagian value capabilities dari config.properties
 * String deviceName = ConfigLoader.getOrDefault("deviceName", "iPhone 14 Pro");
 * if (ConfigLoader.has("udid")) {
 * 	String udid = ConfigLoader.get("udid");
 * 	System.out.println("Running on specific device: " + udid);
 * }
 * 
 * Gunakan value capabilites yang ada di config.properties
 * DesiredCapabilities caps = new DesiredCapabilities();
 * ConfigLoader.getAll().forEach(caps::setCapability);
 * </pre>
 *
 * <p>
 * <b>Catatan:</b> Jika file <code>config.properties</code> tidak ditemukan,
 * class ini akan menggunakan default value (jika disediakan oleh caller) dan
 * menampilkan peringatan di console.
 * </p>
 *
 * @author Kenny Ramadhan
 * @version 1.0
 */

public class ConfigLoader {

	/** Menyimpan semua konfigurasi dari file config.properties */
	private static Properties props = new Properties();

	// Static block untuk memuat file config.properties saat class dipanggil pertama
	// kali
	static {
		try {
			FileInputStream fis = new FileInputStream("src/main/java/com/kenny/automation/Resources/config.properties");
			props.load(fis);
			fis.close();
			System.out.println("Config loaded successfully.");
		} catch (IOException e) {
			System.err.println("Could not load config.properties. Using defaults where possible.");
		}
	}

	/**
	 * Mengambil value dari key yang ada di config.properties.
	 *
	 * @param key Nama key yang ingin diambil.
	 * @return Value dari key, atau <code>null</code> jika tidak ditemukan.
	 */
	public static String get(String key) {
		return props.getProperty(key);
	}


	/**
	 * Mengecek apakah key memiliki value yang valid.
	 *
	 * @param key Nama key yang ingin dicek.
	 * @return <code>true</code> jika key ada dan tidak kosong, <code>false</code>
	 *         jika tidak ada.
	 */
	public static boolean has(String key) {
		String value = props.getProperty(key);
		return value != null && !value.trim().isEmpty();
	}
	
	
	public static String getActiveEnvironmentUrl() {
	    // Ambil environment dari System Property (dikirim Jenkins)
	    String env = System.getProperty("ENVIRONMENT", "STAGING").toUpperCase();

	    // Ambil URL dari config.properties
	    String url = props.getProperty(env);

	    if (url == null || url.isEmpty()) {
	        System.err.println(" Environment " + env + " tidak ditemukan di config.properties. Default ke STAGING.");
	        url = props.getProperty("STAGING");
	    }

	    System.out.println(" Running tests on environment: " + env + " → " + url);
	    return url;
	}

	

}

