package biren.stats;

import biren.Beer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


class Stats {
    
    private List<Beer> beers;
   
    Stats (){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("app/src/main/resources/beers.json");
            BeersWrapper wrapper = objectMapper.readValue(file, BeersWrapper.class);
            this.beers = wrapper.getBeers();

            this.beers.sort((a, b) -> a.rating().compareTo(b.rating()));

            for(Beer beer : this.beers){
                System.out.println(beer.cmdToString());
            }
            System.out.println("beers size: " + this.beers.size()); 
        } catch(IOException e){
            e.printStackTrace();
        }
    }



    public List<Beer> getBeers() {
        return beers;
    }

    private static class BeersWrapper {
        private List<Beer> beers;

        public List<Beer> getBeers() {
            return beers;
        }

        public void setBeers(List<Beer> beers) {
            this.beers = beers;
        }
    }



}
