package io;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * Created by nancy on 01.12.16.
 */
public class AlignmentWriter {

    //TODO kinda not working with GZIPOutputStream - via cmd line?

    /*PrintWriter writer = new PrintWriter("aln.txt", "UTF-8");

    public void write(String s){
        writer.println(s);
    }

    public void close(){
        writer.close();
    }

    public AlignmentWriter() throws FileNotFoundException, UnsupportedEncodingException {
    }*/

    GZIPOutputStream zip;
    BufferedWriter writer;

    /**
     * Writes lines of the alignments to a file
     * @param filename name of the output file
     * @throws IOException
     */
    public AlignmentWriter(String filename) throws IOException {
        zip = new GZIPOutputStream(new FileOutputStream(new File(filename)));
        writer = new BufferedWriter(new OutputStreamWriter(zip, "UTF-8"));
    }

    /**
     * write alignment line into the alignment file
     * @param s alignment line
     */
    public void write(String s){
        try {
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * closes the output stream 
     */
    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
