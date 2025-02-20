package biren;

import java.util.HashMap;

import org.checkerframework.checker.units.qual.t;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;


public class SystemetScrape {
    
    private WebDriver driver;

    private List<String> beerNames = new ArrayList<String>();

    public SystemetScrape(WebDriver driver){
        this.driver = driver;
        
    }

    public List<String> scrape(){
        
        driver.get("https://www.systembolaget.se/sortiment/ol/");
        System.out.println("DEBUGGING - Sysetembolaget page loaded");

        initPage();

        inputStoreAndChangeStore();

        easySleep(500, 500);

        getAllBeerNames();

        return this.beerNames;
        
    }

    public void initPage(){
        WebElement age = tryToScrape("//*[@id=\"__next\"]/div[1]/div[2]/div/section/div/div/div[3]/a[2]", 10, "Could not find age button");
        age.click();  

        WebElement cookie = tryToScrape("//*[@id=\"modalId\"]/div[2]/div/button[1]", 10, "Could not find cookie button");
        cookie.click();
    }

    public void inputStoreAndChangeStore(){
        String userInput = System.console().readLine("Enter store number: ");

        WebElement visaTillganlighet = tryToScrape("//*[@id=\"__next\"]/main/div[2]/div[2]/div[2]/div[1]/div/button", 10, "Could not find visa tillgänglighet button");
        visaTillganlighet.click();

        WebElement hittaVarorIButik = tryToScrape("//*[@id=\"TGMSidebar:options\"]/div[2]/div/div[1]/div/button", 10, "Could not find hitta varor i butik button");
        hittaVarorIButik.click();

        WebElement inputField = tryToScrape("//*[@id=\"TGMSidebar:in_store\"]/div[2]/div/div[1]/input", 10, "Could not find input field");
        inputField.sendKeys(userInput);

        WebElement firstResult = tryToScrape("//*[@id=\"TGMSidebar:in_store\"]/div[2]/div/div[2]/div[2]/button", 10, "Could not find first result");
        firstResult.click();
    }

    public void getAllBeerNames(){
        WebElement nextPageButton;

        while(true){
            easySleep(1000, 1000);
            System.out.println("DEBUGGING - Getting new page");
            getAllBeerNamesOnSinglePage();
            try{
                nextPageButton = driver.findElement(By.xpath("//*[@id=\"__next\"]/main/div[2]/div[2]/div[2]/div[2]/div[5]/div/a"));
                nextPageButton.click();
            } catch(TimeoutException  | StaleElementReferenceException e){
                break;
            }
        }

        

    }

    public void getAllBeerNamesOnSinglePage(){
         List<WebElement> beers = driver.findElements(By.xpath("/html/body/div[1]/main/div[2]/div[2]/div[2]/div[2]/div[3]/a"));
         System.out.println("DEBUGGING - Beer size: " +  beers.size());

         int i = 0;
         for(WebElement beer : beers){
             String beerName;
             String beerSubName;
             try{
                WebElement beerBoldTitle = tryToScrapeFromWebElement(beer, ".//div/div[1]/div/div[2]/div[1]/div/p[1]", 0.5, "");
                beerName = beerBoldTitle.getText();     

                WebElement beerSubTitle = tryToScrapeFromWebElement(beer, ".//div/div[1]/div/div[2]/div[1]/div/p[2]", 0.5, "");
                beerSubName = beerSubTitle.getText();
             } catch(TimeoutException e){
                WebElement beerBoldTitle = tryToScrapeFromWebElement(beer, ".//div/div[2]/div/div[2]/div[1]/div/p[1]", 0.5, "Could not find beer bold title");
                beerName = beerBoldTitle.getText();     

                WebElement beerSubTitle = tryToScrapeFromWebElement(beer, ".//div/div[2]/div/div[2]/div[1]/div/p[2]", 0.5, "Could not find beer sub title");
                beerSubName = beerSubTitle.getText();
                
             }

             if(beerSubName.contains("Nr")){
                beerSubName = "";
             }

             String beerFullName = beerName + " " + beerSubName;
             beerNames.add(beerFullName);
             System.out.println("DEBUGGING - Beer name: " + beerFullName + " i: " + i);
             i++;
         }

    }

    
    public WebElement tryToScrape(String path, double seconds, String errorString){
        try{
            if(seconds <= 0){
                System.out.println(errorString);
                throw new TimeoutException();
            }
            WebElement element = driver.findElement(By.xpath(path));
            return element;
        } catch(NoSuchElementException e){
            System.out.println("Element not found, retrying... Time left: " + seconds + " seconds");
            easySleep(100, 100);
            return tryToScrape(path, seconds - 0.1, errorString);
        }
    }

    public WebElement tryToScrapeFromWebElement(WebElement element, String path, double seconds, String errorString){
        try{
            if(seconds <= 0){
                System.out.println(errorString);
                throw new TimeoutException();
            }
            WebElement newElement = element.findElement(By.xpath(path));
            return newElement;
        } catch(NoSuchElementException e){
            System.out.println("Element not found from element, retrying... Time left: " + seconds + " seconds");
            easySleep(100, 100);
            return tryToScrapeFromWebElement(element, path, seconds - 0.1, errorString);
        }
    }

    public void easySleep(int millisUpper, int millisLower){
        
        int millis = (int) (Math.random() * (millisUpper - millisLower) + millisLower);
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e){
            e.printStackTrace();
            
        }
    }
}

