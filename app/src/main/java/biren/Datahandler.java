package biren;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import java.util.HashSet;
import java.util.List;


class Datahandler {
    
    private BufferedWriter writer;
    private BufferedReader reader;

    Datahandler(){
        try{
            this.reader = new BufferedReader(new FileReader("app/src/main/resources/beers.json"));
            this.writer = new BufferedWriter(new FileWriter("app/src/main/resources/beers.json", true));
                } catch(IOException e){
            e.printStackTrace();
        }

    }

    void writeToFile(List<Beer> beers){
        beers.sort((a, b) -> a.rating().compareTo(b.rating()));
        
        try{
 
            for(Beer beer : beers){
                System.out.println(beer.cmdToString());

                writer.write(beer.toString());
                writer.newLine();
            }

           
            writer.flush();
            

        } catch(IOException e){
            e.printStackTrace();
        }
    }


    HashSet<String> readFromFile(){
        HashSet<String> beerNames = new HashSet<String>();
        try{
            String line = reader.readLine();
            while(line != null){
                if(line.contains("name")){
                    line = line.replace("\"name\": \"", "");
                    System.out.println(line);
                    line = line.substring(2, line.length() - 2);
                    System.out.println(line);
                    beerNames.add(line.replace("\",", ""));
                }
                line = reader.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return beerNames;
    }

}
