package com.kenny.automation.Listeners;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.kenny.automation.Helper.CustomCommand;

import io.qameta.allure.Allure;

public class LogHelper {

    /** Counter otomatis untuk penomoran step, per thread */
    private static ThreadLocal<Integer> stepCounter = ThreadLocal.withInitial(() -> 1);

    /** Node step yang sedang aktif, per thread */
    private static ThreadLocal<ExtentTest> currentStepNode = new ThreadLocal<>();

    /** Nama test case saat ini, per thread */
    private static ThreadLocal<String> currentTestName = new ThreadLocal<>();

    /** Set nama test case untuk thread saat ini */
    public static void setCurrentTestName(String testName) {
        currentTestName.set(testName);
    }

    /** Reset counter step ke 1 untuk thread ini */
    public static void resetCounter() {
        stepCounter.set(1);
    }

    /** Tambahkan step baru di log (Extents + Allure) */
    public static void step(String message) {
        String stepMessage = "STEP " + stepCounter.get() + ": " + message;
        stepCounter.set(stepCounter.get() + 1);

        // Simpan node baru per thread
        currentStepNode.set(ExtentNode.createNode(MarkupHelper.createLabel(stepMessage, ExtentColor.BLACK).getMarkup()));

        Allure.step(message);
    }

    /** Log detail + screenshot */
    public static void detail(String message) {
        ExtentTest node = currentStepNode.get();
        if (node != null) {
            node.log(Status.INFO, MarkupHelper.createLabel(message, ExtentColor.GREEN).getMarkup());

            String screenshotBase64 = CustomCommand.captureScreenshotBase64(message);
            try {
                if (screenshotBase64 != null) {
                    node.addScreenCaptureFromBase64String(screenshotBase64, message);

                    byte[] decodedScreenshot = Base64.getDecoder().decode(screenshotBase64);
                    Allure.addAttachment(message, new ByteArrayInputStream(decodedScreenshot));
                }
            } catch (Exception e) {
                node.warning("Gagal attach screenshot: " + e.getMessage());
            }
        } else {
            ExtentNode.getTest().log(Status.INFO, message);
            Allure.step(message);
        }
    }

    /** Log PASS */
    public static void pass(String message) {
        ExtentTest node = currentStepNode.get();
        if (node != null) {
            node.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN).getMarkup());
        } else {
            ExtentNode.getTest().log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN).getMarkup());
        }
    }

    /** Log FAIL */
    public static void fail(String message) {
        ExtentTest node = currentStepNode.get();
        if (node != null) {
            node.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED).getMarkup());
        } else {
            ExtentNode.getTest().log(Status.FAIL, message);
        }
    }
}
