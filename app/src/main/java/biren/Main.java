package biren;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String chromeDriverPath = "C:\\Users\\oxxer\\Desktop\\programmeingsaker\\chromedriver-win64 (1)\\chromedriver-win64\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        //options.addArguments("--headless");
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);
        
        WebDriver driver = new ChromeDriver(options);
        
        //---------------------------------------------------------
        //-------------for testing purposes-----------------------

        //List<String> beerNamesList = List.of("Sibbarps Husbryggeri 79", "Hög Standard Skånsk Västkust IPA", "Lill-Olas APA");

        //---------------------------------------------------------
        Datahandler datahandler = new Datahandler();

        HashSet<String> beerNamesSet = datahandler.readFromFile();
        
        System.out.println("DEBUGGING - Beer names set size: " + beerNamesSet.size());

        SystemetScrape scrape = new SystemetScrape(driver);
        List<String> beerNamesList = scrape.scrape();

        for(String beerName : beerNamesList){
            if(beerNamesSet.contains(beerName)){
                beerNamesList.remove(beerName);
                System.out.println("DEBUGGING - Removed: " + beerName);
            }
        }


        UntappedScrape untappedScrape = new UntappedScrape(driver, datahandler);
        List<Beer> beers = untappedScrape.scrape(beerNamesList);

        
        

        driver.quit();

    }
}