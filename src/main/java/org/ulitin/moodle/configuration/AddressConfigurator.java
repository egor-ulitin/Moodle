package org.ulitin.moodle.configuration;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;
import java.util.OptionalInt;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.LOG_LEVEL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.SESSION_OVERRIDE;


public class AddressConfigurator {
    private static final Logger LOG = LogManager.getRootLogger();
    private static final String ERROR_LOG_LEVEL = "error";
    private static final String KILL_NODE_COMMAND = "taskkill /F /IM node.exe";
    private static AppiumDriverLocalService appiumDriverLocalService;

    private AddressConfigurator() {

    }

    public static AppiumDriverLocalService getAppiumDriverLocalService(int port) {
        if (appiumDriverLocalService == null)
            startService(port);
        return appiumDriverLocalService;
    }
    private static void startService(int port) {
        makePortAvailableOccupied(port);
        appiumDriverLocalService = new AppiumServiceBuilder().withIPAddress(ConfigurationReader.get().appiumAddress())
                .usingPort(port).withArgument(SESSION_OVERRIDE).withArgument(LOG_LEVEL, ERROR_LOG_LEVEL).build();
        appiumDriverLocalService.start();
        LOG.info("Appium serveer started on port{}", port);
    }

    public static void stopService() {
        Optional.ofNullable(appiumDriverLocalService).ifPresent(service -> {
            service.stop();
            LOG.info("Appium service stopped");
        });
    }
    private static boolean isPortFree(int port) {
        boolean isFree = true;
        try(ServerSocket ignored = new ServerSocket(port)) {
            LOG.info("Specified port - {} is available and ready to use", port);
        }
        catch (Exception e) {
            isFree = false;
            LOG.warn("Specified port - {} is occupied by some process will be terminated", port);
        }
        return isFree;
    }
    private static void makePortAvailableOccupied(int port) {
        if (!isPortFree(port)) {
            try {
                Runtime.getRuntime().exec(KILL_NODE_COMMAND);
            }
            catch (IOException e) {
                LOG.info("Couldn't execute runtime command, message {}", e.getMessage());
            }
        }
    }
}
