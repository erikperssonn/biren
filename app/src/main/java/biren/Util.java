package biren;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

interface Util {
    

    static WebElement tryToScrape(String path, double seconds, String errorString, WebDriver driver){
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
            return tryToScrape(path, seconds - 0.1, errorString, driver);
        }
    }

    static WebElement tryToScrapeFromWebElement(WebElement element, String path, double seconds, String errorString, WebDriver driver){
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
            return tryToScrapeFromWebElement(element, path, seconds - 0.1, errorString, driver);
        }
    }

    static void easySleep(int millisUpper, int millisLower){
        
        int millis = (int) (Math.random() * (millisUpper - millisLower) + millisLower);
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e){
            e.printStackTrace();
            
        }
    }

}
