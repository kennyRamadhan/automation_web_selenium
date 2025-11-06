package com.kenny.automation.Helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * class utilitas (helper) untuk operasi yang terkait dengan JSON.
 * * Kelas ini menyediakan metode statis untuk membantu dalam memanipulasi
 * atau memformat data JSON.
 * @author Kenny Ramadhan
 * @version 1.0
 */
public class JsonHelper {

    /** Instance ObjectMapper tunggal untuk operasi pemetaan JSON. */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    /** Instance ObjectWriter tunggal yang dikonfigurasi untuk pretty-printing (format rapi). */
    private static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();

    /**
     * Mengubah string JSON mentah menjadi format yang lebih mudah dibaca (pretty-print).
     *
     * <p>Jika string input adalah {@code null}, metode ini akan mengembalikan {@code null}.
     * Jika string input bukan merupakan JSON yang valid (misalnya, terjadi {@code Exception}
     * saat parsing), metode ini akan mengembalikan string input asli tanpa perubahan.</p>
     *
     * @param json String JSON yang akan diformat.
     * @return String JSON yang sudah diformat (pretty-printed), 
     * {@code null} jika inputnya {@code null},
     * atau string input asli jika parsing gagal.
     */
    public static String prettyPrint(String json) {
        if (json == null) return null;
        try {
            // Baca string JSON sebagai objek generik
            Object obj = MAPPER.readValue(json, Object.class);
            // Tulis kembali objek tersebut sebagai string yang sudah diformat
            return WRITER.writeValueAsString(obj);
        } catch (Exception e) {
            // Jika gagal parsing (bukan JSON valid), kembalikan string asli
            return json;
        }
    }
}