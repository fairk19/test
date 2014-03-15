package go.mail.ru.tests;

import go.mail.ru.MainPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.TimeoutException;

/**
 * Created by Александр on 14.03.14.
 */
public class SimpleTest {
    private WebDriver driver;
    private ArrayList<String> mimeTypes = new ArrayList<String>();
    private ArrayList<String> queriesForSuggestsTest = new ArrayList<String>();
    private ArrayList<String> queriesForResultTest = new ArrayList<String>();
    private MainPage mainPage;


    @BeforeTest
    @Parameters({"browser", "hub", "url"})
    public void setUp(String browser, String hub, String url) {
        try{
            if (browser.toLowerCase().equals("chrome"))
                this.driver = new RemoteWebDriver(new URL(hub), DesiredCapabilities.chrome());
            else if (browser.toLowerCase().equals("firefox"))
                this.driver = new RemoteWebDriver(new URL(hub), DesiredCapabilities.firefox());
            else
                throw new NotImplementedException();

        }catch (MalformedURLException e){
            System.err.println("go.mail.ru.tests: AdvancedSearchTest: MalformedURLException");
        }

        mimeTypes.add("word");
        mimeTypes.add("ppt");
        mimeTypes.add("pdf");
        mimeTypes.add("excel");

        queriesForSuggestsTest.add("вк");
        queriesForSuggestsTest.add("dr");

        queriesForResultTest.add("вк");
        queriesForResultTest.add("dr");
        queriesForResultTest.add("vk");
        queriesForResultTest.add("мл");

        mainPage = new MainPage(driver);
    }

    @BeforeMethod
    public void setUpMethod(){
        driver.get(MainPage.getTestUrl());
        driver.manage().window().maximize();
    }

    @Test
    public void testAdvancedSearch() {

        mainPage.submitText("test");
        mainPage.clickAdvancedSearchButton();

        for (int i = 0; i < mimeTypes.size(); i++) {

            String titleResult = mainPage.mimeType(mimeTypes.get(i));
            if ( mimeTypes.get(i).equals("word")) {
                Assert.assertTrue(titleResult.equals("[doc]"));

            } else if (mimeTypes.get(i).equals("pdf")) {
                Assert.assertTrue(titleResult.equals("[pdf]"));

            } else if (mimeTypes.get(i).equals("excel")) {
                Assert.assertTrue(titleResult.equals("[xls]"));

            } else if (mimeTypes.get(i).equals("ppt")) {
                Assert.assertTrue(titleResult.equals("[ppt]"));
            }
        }
    }

    @Test
    public void testSuggests() {

        for (int i = 0; i < queriesForSuggestsTest.size(); i++){
            List<String> listSuggests = mainPage.suggests(queriesForSuggestsTest.get(i));
            Assert.assertTrue(listSuggests.contains("вконтакте"));
            mainPage.clearText();
        }
    }

    @Test
    public void testResult() {

        for (int i = 0; i < queriesForResultTest.size(); i++) {
            List<String> listResults = mainPage.results(queriesForResultTest.get(i));
            Assert.assertTrue(listResults.contains("Вконтакте\n" +
                    "vk.com\n" +
                    "ВКонтакте"));
            mainPage.clearText();
        }
    }

    @Test
    public void testImageOnMainPage() {

        try{
            mainPage.photoNotDisplayed();
        }catch (TimeoutException e){
            mainPage.hidePhoto();
            Assert.assertTrue(mainPage.photoNotDisplayed());
        }

        try {
            mainPage.photoIsDisplayed();

        }catch (TimeoutException e){
            mainPage.displayPhoto();
            Assert.assertTrue(mainPage.photoIsDisplayed());
        }
    }

    @Test
    public void testTopMenu(){
        mainPage.tabImage();
        Assert.assertTrue(mainPage.resultFormatImage());
    }

    @Test
    public void testPlaceholderSearchForm() {
        mainPage.submitText("");
        Assert.assertTrue(mainPage.placeholderSearchFormIsDisplay());
    }

    @AfterTest
    public void tearDown(){
        driver.close();
    }
}
