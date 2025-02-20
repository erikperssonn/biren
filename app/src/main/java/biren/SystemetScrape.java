package biren;


import org.openqa.selenium.*;

import java.util.List;
import java.util.ArrayList;



public class SystemetScrape {
    
    private WebDriver driver;

    private List<String> beerNames = new ArrayList<String>();

    public SystemetScrape(WebDriver driver){
        this.driver = driver;
        
    }

    List<String> scrape(){
        
        driver.get("https://www.systembolaget.se/sortiment/ol/");
        System.out.println("DEBUGGING - Sysetembolaget page loaded");

        initPage();

        inputStoreAndChangeStore();

        Util.easySleep(500, 500);

        getAllBeerNames();

        return this.beerNames;
        
    }

    void initPage(){
        WebElement age = Util.tryToScrape("//*[@id=\"__next\"]/div[1]/div[2]/div/section/div/div/div[3]/a[2]", 10, "Could not find age button", this.driver);
        age.click();  

        WebElement cookie = Util.tryToScrape("//*[@id=\"modalId\"]/div[2]/div/button[1]", 10, "Could not find cookie button", this.driver);
        cookie.click();
    }

    
    void inputStoreAndChangeStore(){
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

    void getAllBeerNames(){
        WebElement nextPageButton;

        while(true){
            Util.easySleep(1000, 1000);
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

    void getAllBeerNamesOnSinglePage(){
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
             } catch(TimeoutException | StaleElementReferenceException e){
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


}

