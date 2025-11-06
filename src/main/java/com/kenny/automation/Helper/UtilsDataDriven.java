package com.kenny.automation.Helper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class UtilsDataDriven {

    /**
     * Method utama untuk membaca data test.
     * Bisa handle Excel (.xlsx/.xls) dan CSV (.csv).
     *
     * @param filePath  path file
     * @param sheetName nama sheet (untuk Excel, CSV bisa null)
     * @return Object[][] dengan Map<String,String> per row
     * @throws CsvException jika terjadi error parsing CSV
     */
    public static Object[][] getTestData(String filePath, String sheetName) throws CsvException {
        if (filePath.toLowerCase().endsWith(".csv")) {
            return getCSVData(filePath);
        } else {
            return getExcelData(filePath, sheetName);
        }
    }

    /**
     * Membaca data dari CSV dengan header.
     * Aman terhadap BOM (Byte Order Mark) dan spasi tambahan.
     */
    private static Object[][] getCSVData(String csvPath) throws CsvException {
        Object[][] data = null;

        try (
            InputStreamReader isr = new InputStreamReader(new FileInputStream(csvPath), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            CSVReader reader = new CSVReader(br)
        ) {
            List<String[]> allData = reader.readAll();

            if (allData.isEmpty()) {
                return new Object[0][0];
            }

            // Header dengan trimming + handle BOM
            String[] headers = allData.get(0);
            for (int i = 0; i < headers.length; i++) {
                headers[i] = removeBOM(headers[i]).trim();
            }

            int rowCount = allData.size() - 1;
            data = new Object[rowCount][1];

            for (int i = 1; i <= rowCount; i++) {
                String[] row = allData.get(i);
                Map<String, String> rowData = new HashMap<>();

                for (int j = 0; j < headers.length; j++) {
                    String header = headers[j];
                    String value = (j < row.length && row[j] != null) ? row[j].trim() : "";
                    rowData.put(header, value);
                }

                data[i - 1][0] = rowData;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Membaca data dari Excel dengan header.
     * Otomatis trimming spasi.
     */
    private static Object[][] getExcelData(String excelPath, String sheetName) {
        Object[][] data = null;

        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet " + sheetName + " not found in Excel file!");
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            // Header
            String[] headers = new String[colCount];
            Row headerRow = sheet.getRow(0);
            for (int j = 0; j < colCount; j++) {
                headers[j] = headerRow.getCell(j).toString().trim();
            }

            // Data
            data = new Object[rowCount - 1][1];
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();

                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    String value = (cell == null) ? "" : cell.toString().trim();
                    rowData.put(headers[j], value);
                }

                data[i - 1][0] = rowData;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Menghapus karakter BOM (Byte Order Mark) jika ada.
     */
    private static String removeBOM(String text) {
        if (text != null && text.startsWith("\uFEFF")) {
            return text.substring(1);
        }
        return text;
    }
}
