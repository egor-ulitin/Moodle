package org.ulitin.moodle.pages;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.ulitin.moodle.driver.DriverManager;

public class BasePage {
    public BasePage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()),this);
    }
}
