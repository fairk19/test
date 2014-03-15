package go.mail.ru;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Александр on 10.03.14.
 */

public class MainPage {

    private WebDriver driver;
    private static final String TEST_URL = "http://go.mail.ru/";
    private static final int TIME_OUT = 5;
    private static final int TIME_OUT_PHOTO = 1;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterText(String query) {
        driver.findElement(By.id("q")).sendKeys(query);
    }

    public void submitText(String query) {
        driver.findElement(By.id("q")).sendKeys(query);
        driver.findElement(By.id("q")).submit();
    }

    public void  clearText(){
        driver.findElement(By.id("q")).clear();
    }

    public void clickAdvancedSearchButton() {
        driver.findElement(By.id("go-form__advanced-btn")).click();
    }
    public void parametersAdvancedSearchButton(String mimeType) {

        if (mimeType.equals("word")) {
            driver.findElement(By.className("ASW-files-word")).findElement(By.tagName("em")).click();

        } else if (mimeType.equals("pdf")) {
            driver.findElement(By.className("ASW-files-pdf")).findElement(By.tagName("em")).click();

        } else if (mimeType.equals("excel")) {
            driver.findElement(By.className("ASW-files-excel")).findElement(By.tagName("em")).click();

        } else if (mimeType.equals("ppt")) {
            driver.findElement(By.className("ASW-files-ppt")).findElement(By.tagName("em")).click();

        } else {
            System.err.println("Error input data: Некорректные входные данные");
        }

        driver.findElement(By.id("ASW-submit")).click();
    }

    public List<String> results(String query) {
        this.submitText(query);
        return Arrays.asList(new WebDriverWait(driver, TIME_OUT).until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver input) {
                return input.findElement(By.id("js-result_1"));
            }
        }).getText().split(" "));

    }
    public List<String> suggests(String query){
        this.enterText(query);
        return Arrays.asList(new WebDriverWait(this.driver, TIME_OUT).until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver input) {
                return input.findElement(By.className("go-suggests__item__text"));
            }
        }).getText());
    }

    public String mimeType(String mimeType) {

        this.parametersAdvancedSearchButton(mimeType);
        return new WebDriverWait(this.driver, TIME_OUT).until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver input) {
                return input.findElement(By.id("js-result_1"))
                        .findElement(By.className("result__title"))
                        .findElement(By.className("result__title-mime"));
            }
        }).getAttribute("title");
    }
   public static final String getTestUrl() {
       return TEST_URL;
   }

   public boolean photoIsDisplayed() {
       return new WebDriverWait(driver, TIME_OUT).
               until(ExpectedConditions.
                       textToBePresentInElement(driver.
                               findElement(By.id("wallpapers__toggle")), "Убрать фото"));
   }
   public boolean photoNotDisplayed() {
        return new WebDriverWait(driver, TIME_OUT).
                until(ExpectedConditions.
                        textToBePresentInElement(driver.
                                findElement(By.id("wallpapers__toggle")), "Показать фото"));
    }
   public void displayPhoto() {
        try{
            photoNotDisplayed();
        }catch (TimeoutException e){
            return;
        }
        driver.findElement(By.id("wallpapers__toggle")).click();
   }

   public void hidePhoto() {
       try{
           photoIsDisplayed();
       }catch (TimeoutException e){
           return;
       }
       driver.findElement(By.id("wallpapers__toggle")).click();
   }

   public void tabImage() {
       driver.findElement(By.id("tab-img")).click();
   }
   public boolean resultFormatImage() {
       return new WebDriverWait(this.driver, TIME_OUT_PHOTO).until(new Function<WebDriver, WebElement>() {
           @Override
           public WebElement apply(WebDriver input) {
               return input.findElement(By.id("pi_1"));
           }
       }).getTagName().equals("img");
   }
   public boolean placeholderSearchFormIsDisplay() {

       boolean placeholderIsDisplayed;
       try{
       placeholderIsDisplayed = new WebDriverWait(this.driver, TIME_OUT).until(new Function<WebDriver, WebElement>() {
           @Override
           public WebElement apply(WebDriver input) {
               return input.findElement(By.id("placeholder"));
           }
       }).isDisplayed();

       } catch (TimeoutException e){
           placeholderIsDisplayed = false;
       }
       return placeholderIsDisplayed;
   }
}
