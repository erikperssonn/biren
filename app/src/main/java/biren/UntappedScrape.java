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

    public UntappedScrape(WebDriver driver){
        this.driver = driver;   
    }

    public void scrape(List<String> beerNames){
        
        for(String beerName : beerNames){
            
        }
            
        
    }
}
