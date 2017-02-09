package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

/**
 * Created by nancy on 07.02.17.
 */
public class RepacementsReader {

    public void readReplacements(TreeMap<String, String> replacements, String filename){
        String[] lineSplit = new String[2];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            while(reader.ready()){
                lineSplit = reader.readLine().split("\\s+");
                replacements.put(lineSplit[0], lineSplit[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
