package com.iut.banque.selenium;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestsSelenium {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    private String appPath = "http://127.0.0.1:8080/_00_ASBank2023/";

    private String seleniumUrl = "http://localhost:4444/wd/hub";

    @Before
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1200");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--whitelister-ips=''");
        options.addArguments("--lang=fr");
        
        driver = new ChromeDriver(options);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
    }
    

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLogo() {
        try {
            driver.get(appPath);
        } catch (WebDriverException e) {
            System.out.println("The app couldn't be accessed, Selenium tests aborted");
            return;
        }
        driver.manage().window().setSize(new Dimension(550, 694));
        try {
            WebElement logo = driver.findElement(By.id("logo"));
            Assert.assertNotNull(logo);
        } catch (NoSuchElementException e) {
            Assert.fail("The logo couldn't be found");
        }
    }

    @Test
    public void testLogin() {
        try {
            driver.get(appPath);
        } catch (WebDriverException e) {
            System.out.println("The app couldn't be accessed, Selenium tests aborted");
            return;
        }
        driver.manage().window().setSize(new Dimension(550, 694));

        try {
            WebElement loginButton = driver.findElement(By.id("login-button"));
            Assert.assertNotNull(loginButton);
            loginButton.click();
        } catch (NoSuchElementException e) {
            Assert.fail("The login button couldn't be found");
        }

        try {
            WebElement userCdeInput = driver.findElement(By.id("controller_Connect_login_action_userCde"));
            WebElement userPwdInput = driver.findElement(By.id("controller_Connect_login_action_userPwd"));
            WebElement loginSubmitButton = driver.findElement(By.id("controller_Connect_login_action_submit"));

            userCdeInput.sendKeys("client1");
            userPwdInput.sendKeys("clientpass1");
            loginSubmitButton.click();
        } catch (NoSuchElementException e) {
            Assert.fail("The login inputs couldn't be found");
        }

        try {
            String userWelcome = driver.findElement(By.id("user-welcome")).getText();
            Assert.assertEquals("Jane client1", userWelcome);
        } catch (NoSuchElementException e) {
            Assert.fail("The user couldn't connect successfully");
        }
    }
}
