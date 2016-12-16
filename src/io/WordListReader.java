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

            String[] tokens = in.readLine().split(",");
            int idCol = -1;
            int wordCol = -1;
            for (int i = 0; i < tokens.length; i++){
                if (tokens[i].trim().equals("id"))
                    idCol = i;
                if (tokens[i].trim().equals("name"))
                    wordCol = i;
            }

            if (idCol == -1 || wordCol == -1){
                System.err.println("Please make sure \"id\" and \"name\" show up in the file " + filename);
                return;
            }

            String line = "";
            String id = "";
            String[] idSplit;
            Float meaning;
            String word = "";
            while (in.ready()) {
                counter++;
                line = in.readLine();
                tokens = line.split(",");

                id = tokens[idCol];

                idSplit = tokens[idCol].split("-");
                meaning = Float.parseFloat(idSplit[0]+"."+idSplit[1]);

                word = tokens[wordCol].trim();
                word = word.replaceAll("-", "");

                //remove spaces from word if there are any
                word = word.replaceAll("\\s+","");

                //adds meaning and language to database
                database.addEntry(id, meaning, word);
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
