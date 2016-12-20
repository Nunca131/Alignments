package io;

import container.Algebra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by nancy on 16.12.16.
 */
public class ScoreReader {

    private BufferedReader reader;
    Algebra alg;

    public ScoreReader(String filename, Algebra alg){
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.alg = alg;
        readScoreFile();
    }

    private void readScoreFile(){
        String[] lineSplit = new String[6];
        try {
            while(reader.ready()){
                try {
                    lineSplit = reader.readLine().split("\\s+");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                alg.fill(lineSplit[2] + lineSplit[3], lineSplit[4] + lineSplit[5],
                        Double.parseDouble(lineSplit[6]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
