package org.ulitin.moodle.utils;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.ulitin.moodle.driver.DriverManager;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static java.lang.String.format;

public class TestListener implements ITestListener {
    private static final Logger LOG = LogManager.getRootLogger();

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("{} started", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("{} passed", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.info("{} failed", result.getName());
        takeScreenshot();
    }

    private void takeScreenshot() {
        try {
            File screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(format(".//target/screenshots/%s.png", LocalDate.now())));
        }
        catch (IOException e) {
            LOG.error("Failed to take screenshot: {}", e.getMessage());
        }
    }
}
