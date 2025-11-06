package com.kenny.automation.Listeners;

/**
 * ApiLogManager adalah kelas utilitas yang mengelola objek {@link ApiLogHelper} secara thread-safe
 * menggunakan {@link ThreadLocal}. 
 * <p>
 * class ini memungkinkan setiap thread memiliki instance logger sendiri tanpa mengganggu thread lain.
 * Cocok digunakan dalam pengujian API yang dijalankan secara parallel untuk memastikan log setiap
 * request tercatat secara terpisah.
 * </p>
 * @author Kenny Ramadhan
 * @version 1.0
 */
public class ApiLogManager {
	private static ThreadLocal<ApiLogHelper> apiLogger = new ThreadLocal<>();

    public static void setLogger(ApiLogHelper logger) {
        apiLogger.set(logger);
    }

    public static ApiLogHelper getLogger() {
        return apiLogger.get();
    }

    public static void removeLogger() {
        apiLogger.remove();
    }
}
