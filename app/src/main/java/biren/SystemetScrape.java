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
        WebElement age = Util.tryToScrape("//*[@id=\"__next\"]/div[1]/div[2]/div/section/div/div/div[3]/a[2]", 10, "Could not find age button", this.driver);
        age.click();  

        WebElement cookie = Util.tryToScrape("//*[@id=\"modalId\"]/div[2]/div/button[1]", 10, "Could not find cookie button", this.driver);
        cookie.click();
    }

    public void inputStoreAndChangeStore(){
        String userInput = System.console().readLine("Enter store number: ");

        WebElement visaTillganlighet = Util.tryToScrape("//*[@id=\"__next\"]/main/div[2]/div[2]/div[2]/div[1]/div/button", 10, "Could not find visa tillgänglighet button", this.driver);
        visaTillganlighet.click();

        WebElement hittaVarorIButik = Util.tryToScrape("//*[@id=\"TGMSidebar:options\"]/div[2]/div/div[1]/div/button", 10, "Could not find hitta varor i butik button", this.driver);
        hittaVarorIButik.click();

        WebElement inputField = Util.tryToScrape("//*[@id=\"TGMSidebar:in_store\"]/div[2]/div/div[1]/input", 10, "Could not find input field", this.driver);
        inputField.sendKeys(userInput);

        WebElement firstResult = Util.tryToScrape("//*[@id=\"TGMSidebar:in_store\"]/div[2]/div/div[2]/div[2]/button", 10, "Could not find first result", this.driver);
        firstResult.click();
    }

    public void getAllBeerNames(){
        WebElement nextPageButton;

        while(true){
            easySleep(1000, 1000);
            System.out.println("DEBUGGING - Getting new page");
            getAllBeerNamesOnSinglePage();
            try{
                nextPageButton = Util.tryToScrape("//*[@id=\"__next\"]/main/div[2]/div[2]/div[2]/div[2]/div[5]/div/a", 1, "Could not find next page button", this.driver);
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
                WebElement beerBoldTitle = Util.tryToScrapeFromWebElement(beer, ".//div/div[1]/div/div[2]/div[1]/div/p[1]", 0.5, "", this.driver);
                beerName = beerBoldTitle.getText();     

                WebElement beerSubTitle = Util.tryToScrapeFromWebElement(beer, ".//div/div[1]/div/div[2]/div[1]/div/p[2]", 0.5, "", this.driver);
                beerSubName = beerSubTitle.getText();
             } catch(TimeoutException e){
                WebElement beerBoldTitle = Util.tryToScrapeFromWebElement(beer, ".//div/div[2]/div/div[2]/div[1]/div/p[1]", 0.5, "Could not find beer bold title", this.driver);
                beerName = beerBoldTitle.getText();     

                WebElement beerSubTitle = Util.tryToScrapeFromWebElement(beer, ".//div/div[2]/div/div[2]/div[1]/div/p[2]", 0.5, "Could not find beer sub title", this.driver);
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

    
    

    

    public void easySleep(int millisUpper, int millisLower){
        
        int millis = (int) (Math.random() * (millisUpper - millisLower) + millisLower);
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e){
            e.printStackTrace();
            
        }
    }
}

