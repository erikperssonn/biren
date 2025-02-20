package biren;

import java.util.List;
import java.util.HashMap;

import org.checkerframework.checker.units.qual.t;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;


public class UntappedScrape {
    
    private WebDriver driver;

    private List<Beer> beers = new ArrayList<>();

    public UntappedScrape(WebDriver driver){
        this.driver = driver;   
    }

    public List<Beer> scrape(List<String> beerNames){
        
        int k = 0;
        for(String beerName : beerNames){
            driver.get("https://untappd.com/search?q=");
            System.out.println("DEBUGGING - Untapped page loaded");

            if(k == 0){
                accecptCookies();
            }

            WebElement search = Util.tryToScrape("//*[@id=\"search-term\"]", 10, "Could not find search bar", this.driver);
            System.out.println("DEBUGGING - Found search bar");
            
            search.sendKeys(beerName);
            
            search.sendKeys(Keys.RETURN);

            //WebElement container = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div/div[1]/div[3]/div[2]", 10, "Could not find container", this.driver);
//
            //// no children, no results
            //List<WebElement> children = container.findElements(By.xpath("./*"));
            //System.out.println("DEBUGGING - Found children: " + children.size());
            //if (children.isEmpty()) {
            //    System.out.println("DEBUGGING - No results found for: " + beerName);
            //    continue;
            //}
            try{
                WebElement firstBeer = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div/div[1]/div[3]/div[2]/div/div[1]/p[1]/a", 1, "Could not find first beer", this.driver);
                firstBeer.click();
            } catch(TimeoutException e){
                System.out.println("Could not find first beer");
                continue;
            }

            Util.easySleep(512, 379);

            getBeerInfo(beerName);
            k++;
            
        }

        return beers;
            
        
    }

    public void accecptCookies(){
        try{
            WebElement cookie = Util.tryToScrape("/html/body/div[5]/div[2]/div[2]/div[2]/div[2]/button[1]", 10, "Could not find cookie button", this.driver);
            cookie.click();    
        } catch(TimeoutException e){  //sometimes the cookie button is not found by the same path
            WebElement cookie = Util.tryToScrape("/html/body/div[6]/div[2]/div[2]/div[2]/div[2]/button[1]", 10, "Could not find cookie button", this.driver);
            cookie.click();    
        }
    }


    public void getBeerInfo(String beerName){
        WebElement rating = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div[1]/div/div[2]/span", 10, "Could not find rating", this.driver);
        String ratingText = rating.getText();

        WebElement ABV = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div[1]/div/div[2]/p[1]", 10, "Could not find ABV", this.driver);
        String ABVText = ABV.getText();

        WebElement type = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div[1]/div/div[1]/div[1]/div[1]/p[2]", 10, "Could not find type", this.driver);
        String typeText = type.getText();
        System.out.println("DEBUGGING - Rating: " + ratingText  + " ABV: " + ABVText + " Type: " + typeText);
        
        //remove '(' and ')' from ratingText
        ratingText = ratingText.substring(1, ratingText.length()-1);

        //remove % from ABVText and "ABV
        ABVText = ABVText.split("%")[0];

        System.out.println("DEBUGGING - Rating: " + ratingText  + " ABV: " + ABVText + " Type: " + typeText);
        beers.add(new Beer(beerName, Double.parseDouble(ratingText), typeText, Double.parseDouble(ABVText))); 
    }
}
