package com.kenny.automation.Listeners;

import com.aventstack.extentreports.ExtentTest;

public class ExtentNode {

    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> nodeTest = new ThreadLocal<>();

    /**
     * Membuat parent test baru untuk thread saat ini.
     */
    public static ExtentTest createTest(String testName) {
        ExtentTest test = ExtentReportsManager.getExtentReports().createTest(testName);
        parentTest.set(test);
        return test;
    }

    /**
     * Mengambil parent test yang aktif.
     */
    public static ExtentTest getTest() {
        return parentTest.get();
    }

    /**
     * Membuat node baru di bawah parent test.
     */
    public static ExtentTest createNode(String stepName) {
        ExtentTest parent = parentTest.get();
        if (parent == null) {
            parent = ExtentReportsManager.getExtentReports().createTest("Unnamed Test (Auto Created)");
            parentTest.set(parent);
        }
        ExtentTest node = parent.createNode(stepName);
        nodeTest.set(node);
        return node;
    }

    /**
     * Mengambil node test yang sedang aktif.
     */
    public static ExtentTest getNode() {
        ExtentTest node = nodeTest.get();
        if (node == null) {
            node = parentTest.get();
            if (node == null) {
                node = ExtentReportsManager.getExtentReports().createTest("Unnamed Test (Auto Created)");
                parentTest.set(node);
            }
        }
        return node;
    }

    /**
     * Menambahkan log info.
     */
    public static void addInfo(String message) {
        getNode().info(message);
    }

    /**
     * Menambahkan screenshot.
     */
    public static void addScreenshot(String path) {
        try {
            getNode().addScreenCaptureFromPath(path);
        } catch (Exception e) {
            getNode().warning("Failed to attach screenshot: " + e.getMessage());
        }
    }

    /**
     * Bersihkan thread local di akhir suite.
     */
    public static void remove() {
        parentTest.remove();
        nodeTest.remove();
    }
}
