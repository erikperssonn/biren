package biren;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;


class Datahandler {
    
    private BufferedWriter writer;

    Datahandler(){
        try{
            this.writer = new BufferedWriter(new FileWriter("app/src/main/resources/beers.json"));
                } catch(IOException e){
            e.printStackTrace();
        }
    }

    void writeToFile(List<Beer> beers){
        beers.sort((a, b) -> a.rating().compareTo(b.rating()));
        
        try{
        writer.write("{ " + " \n" + 
                    "\"beers\":[");

        for(Beer beer : beers){
            System.out.println(beer.cmdToString());

            writer.write(beer.toString());
            writer.newLine();
        }

        writer.write("]}");
        writer.flush();
        writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
