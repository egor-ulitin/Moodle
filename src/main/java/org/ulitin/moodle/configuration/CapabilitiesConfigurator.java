package org.ulitin.moodle.configuration;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.*;
import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;

public class CapabilitiesConfigurator {
    private CapabilitiesConfigurator() {

    }
    public static DesiredCapabilities getLocalCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(UDID, ConfigurationReader.get().udid());
        desiredCapabilities.setCapability(AVD, ConfigurationReader.get().localDeviceName());
        desiredCapabilities.setCapability(APP_PACKAGE, ConfigurationReader.get().appPackage());
        desiredCapabilities.setCapability(APP_ACTIVITY, ConfigurationReader.get().appActivity());
        desiredCapabilities.setCapability(APP, new File(ConfigurationReader.get().appPath()).getAbsolutePath());
        return desiredCapabilities;
    }
}
