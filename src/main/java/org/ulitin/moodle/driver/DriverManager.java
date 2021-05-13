package org.ulitin.moodle.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ulitin.moodle.configuration.AddressConfigurator;
import org.ulitin.moodle.configuration.CapabilitiesConfigurator;
import org.ulitin.moodle.configuration.ConfigurationReader;
import org.ulitin.moodle.configuration.EnviromentType;

import java.io.IOException;
import java.lang.module.Configuration;
import java.util.Optional;

import static java.lang.String.format;

public class DriverManager {
    private static final Logger LOG = LogManager.getRootLogger();
    private static final EnviromentType ENVIROMENT_TYPE = EnviromentType.valueOf(ConfigurationReader.get().env().toUpperCase());
    private static AppiumDriver<MobileElement> driver;

    private DriverManager() {

    }

    public static AppiumDriver<MobileElement> getDriver() {
        if (driver == null)
            driver = createDriver();
        return driver;
    }

    private static AppiumDriver<MobileElement> createDriver() {
        switch (ENVIROMENT_TYPE) {
            case LOCAL -> driver = new AndroidDriver<MobileElement>(AddressConfigurator.getAppiumDriverLocalService(ConfigurationReader.get().appiumPort()),
                    CapabilitiesConfigurator.getLocalCapabilities());
            default -> 
                    throw new IllegalArgumentException(format("Unexpected enviroment value: %S", ENVIROMENT_TYPE));
        }
        LOG.info("Driver is created");
        LOG.info("Enviroment type {}", ENVIROMENT_TYPE);
        return driver;
    }
    public static void closeDriver(){
        Optional.ofNullable(getDriver()).ifPresent(driverInstance -> {
            driverInstance.quit();
            driver = null;
            LOG.info("Driver is closed");
        });
    }
    public static void closeAppium() {
        AddressConfigurator.stopService();
    }


    public static void closeEmulator() throws IOException {
        try {
            Runtime.getRuntime().exec(format("adb %s emu kill", ConfigurationReader.get().udid()));
            LOG.info("AVD is closed");
        }
        catch (IOException e) {
            LOG.info("AVD is not closed, message: {}", e.getMessage());
        }
    }
}