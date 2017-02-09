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

    private String filename;
    private BufferedReader reader;
    Algebra alg;

    /**
     * Reads file with ngram scores and stores them in an Algebra
     * @param filename
     * @param alg
     */
    public ScoreReader(String filename, Algebra alg){
        this.filename = filename;
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.alg = alg;
        readScoreFile();
    }

    /**
     * reads the file previously set and parses the lines which are then used
     * to fill the Algebra
     */
    private void readScoreFile(){
        String line = "";
        String[] lineSplit = new String[7];

        try {
            while(reader.ready()){
                try {
                    line = reader.readLine();
                    lineSplit = line.split("\\s+");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //unigram case
                if (lineSplit.length == 5)
                    alg.fill( lineSplit[2], lineSplit[3], Double.parseDouble(lineSplit[4]));

                //bigram case
                else if (lineSplit.length == 7)
                alg.fill(lineSplit[2] + lineSplit[3], lineSplit[4] + lineSplit[5],
                        Double.parseDouble(lineSplit[6]));

                else
                    System.err.println("Found invalid line in score file " + this.filename + ": \"" + line + "\"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
