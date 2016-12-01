package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by nancy on 30.11.16.
 */
public class WordListReader {

    /**
     * Reads a word list file in a given database.
     *
     * @param filename filename of the wordlist file
     * @param database database for storing entries
     */
    public static void readFile(String filename, container.WordDatabase database) {
        long start = System.currentTimeMillis();
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader in = new BufferedReader(reader);

            int counter = 0;

            while (in.ready()) {
                counter++;
                String[] tokens = in.readLine().split("\t");
                //String wordID = tokens[0].trim();
                String language = tokens[2].trim();
                String meaning = tokens[1].trim();
                String word = tokens[4].trim();

                //remove spaces from word if there are any
                word = word.replaceAll("\\s+","");

                //adds meaning and language to database
                database.addEntry(meaning, word, language);
            }

            in.close();

            long end = System.currentTimeMillis();
            long ms = end - start;
            System.out.println("** Read " + counter + " entries of file " + filename + " in " + ms + " ms.");
        } catch (FileNotFoundException e) {
            System.err.println("Could not find the file " + filename);
        } catch (IOException e) {
            System.err.println("There was an error processing the file " + filename);
        }

    }


}
