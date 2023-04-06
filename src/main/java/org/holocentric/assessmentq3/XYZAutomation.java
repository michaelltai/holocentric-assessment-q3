package org.holocentric.assessmentq3;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class XYZAutomation {
    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extent;
    private static ExtentTest extentTest;
    private static WebDriver driver;

    private static int totalVal = 0;

    static void creditValue(WebElement creditBtn, int amount) throws InterruptedException {
        creditBtn.click();
        Thread.sleep(500);
        WebElement amountInput = driver.findElement(By.cssSelector("input[placeholder='amount']"));
        amountInput.sendKeys(Integer.toString(amount));
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        submitBtn.click();
        totalVal = totalVal + amount;
    }

    static void debitValue(WebElement debitBtn, int amount) throws InterruptedException {
        debitBtn.click();
        Thread.sleep(500);
        WebElement amountInput = driver.findElement(By.cssSelector("input[placeholder='amount']"));
        amountInput.sendKeys(Integer.toString(amount));
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        submitBtn.click();
        totalVal = totalVal - amount;
    }

    @BeforeAll
    public static void beforeAll() {
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver","C://Users//micha//selenium-java-4.8.3//chromedriver/chromedriver.exe");
        options.addArguments("--remote-allow-origins=*");

        extent = new ExtentReports();
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") +"\\test-output\\testReport.html");
        extent.attachReporter(sparkReporter);

        sparkReporter.config().setOfflineMode(true);
        sparkReporter.config().setDocumentTitle("XYZ Automation Report");
        sparkReporter.config().setReportName("Test Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        sparkReporter.config().setEncoding("UTF-8");




        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void runTest() throws InterruptedException {
        extentTest = extent.createTest("XYZ Bank Test Run");

        try{
            driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login.");
            WebElement bankManagerBtn = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@class='btn btn-primary btn-lg'])[1]")));
            bankManagerBtn.click();
            WebElement drpDwn = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id("userSelect")));
            Select userList = new Select(drpDwn);
            userList.selectByIndex(1);
            WebElement loginBtn = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type=\"submit\"]")));
            loginBtn.click();

            WebElement accSel =  new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id("accountSelect")));
            Select accList = new Select(accSel);
            accList.selectByValue("number:1003");

            WebElement accSummary = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[ng-hide=\"noAccount\"]")));
            WebElement balance = accSummary.findElement(By.cssSelector("strong:nth-child(2)"));
            if(Integer.parseInt(balance.getText()) == totalVal){
                System.out.println("Account balance is: " + balance.getText());
            }else{
                System.out.println("Account balance is not 0");
            }

            List<WebElement> buttons = driver.findElements(By.cssSelector(".btn-group-lg>.btn, .btn-lg"));
            creditValue(buttons.get(1),50000);
            debitValue(buttons.get(2),3000);
            debitValue(buttons.get(2),2000);
            creditValue(buttons.get(1),5000);
            debitValue(buttons.get(2),10000);
            debitValue(buttons.get(2),15000);
            creditValue(buttons.get(1),1500);

            if(totalVal == Integer.parseInt(balance.getText())){
                System.out.println("The current balance tallies");
            }
            extentTest.log(Status.PASS, "Test Run Pass");
        }catch (Exception ex){
            extentTest.log(Status.FAIL,ex);
            throw ex;
        }
    }

    @AfterAll
    public static void afterAll(){
        if(driver != null){
            driver.quit();
        }

        extent.flush();
    }
}
