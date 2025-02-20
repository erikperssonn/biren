package biren;

import org.openqa.selenium.*;

import java.util.List;
import java.util.ArrayList;



public class UntappedScrape {
    
    private WebDriver driver;
    private Datahandler datahandler;

    private List<Beer> beers;
    private List<Beer> recentBeers;

    UntappedScrape(WebDriver driver, Datahandler datahandler){
        this.driver = driver;   
        this.datahandler = datahandler;
        this.beers = new ArrayList<Beer>();
        this.recentBeers = new ArrayList<Beer>();
    }

    List<Beer> scrape(List<String> beerNames){
        
        int k = 0;
        for(String beerName : beerNames){
            driver.get("https://untappd.com/search?q=");
            System.out.println("DEBUGGING - Untapped page loaded");

            if(k == 0){
                try{
                    accecptCookies();
                } catch(TimeoutException e){
                    System.out.println("Could not find cookie button - Continuing");
                }
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
            if(recentBeers.size() >= 10){
                datahandler.writeToFile(recentBeers);
                recentBeers.clear();
            }
            k++;
            
        }
        
        datahandler.writeToFile(recentBeers);

        return beers;
            
        
    }

    void accecptCookies(){
        try{
            WebElement cookie = Util.tryToScrape("/html/body/div[5]/div[2]/div[2]/div[2]/div[2]/button[1]", 10, "Could not find cookie button", this.driver);
            cookie.click();    
        } catch(TimeoutException e){  //sometimes the cookie button is not found by the same path
            WebElement cookie = Util.tryToScrape("/html/body/div[6]/div[2]/div[2]/div[2]/div[2]/button[1]", 10, "Could not find cookie button", this.driver);
            cookie.click();    
        }
    }


    void getBeerInfo(String beerName){
        WebElement rating;
        String ratingText = "";
        
        try{
            rating = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div[1]/div/div[2]/span", 10, "Could not find rating", this.driver);
            ratingText = rating.getText();
        } catch(TimeoutException e){
            accecptCookies();
            rating = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div[1]/div/div[2]/span", 10, "Could not find rating", this.driver);
            ratingText = rating.getText();

        }

        WebElement ABV = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div[1]/div/div[2]/p[1]", 10, "Could not find ABV", this.driver);
        String ABVText = ABV.getText();

        WebElement type = Util.tryToScrape("//*[@id=\"slide\"]/div[2]/div[1]/div[1]/div/div[1]/div[1]/div[1]/p[2]", 10, "Could not find type", this.driver);
        String typeText = type.getText();
        System.out.println("DEBUGGING - Rating: " + ratingText  + " ABV: " + ABVText + " Type: " + typeText);
        
        //remove '(' and ')' from ratingText
        ratingText = ratingText.substring(1, ratingText.length()-1);
        if(ratingText.equals("N/A")){
            ratingText = "0.0";
        }

        //remove % from ABVText and "ABV
        ABVText = ABVText.split("%")[0];

        System.out.println("DEBUGGING - Rating: " + ratingText  + " ABV: " + ABVText + " Type: " + typeText);
        Beer beer = new Beer(beerName, Double.parseDouble(ratingText), typeText, Double.parseDouble(ABVText));
        beers.add(beer); 
        recentBeers.add(beer);
    }
}
