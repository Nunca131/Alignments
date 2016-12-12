package container;

import java.util.ArrayList;
// import java.util.HashMap;

/**
 * Created by nancy on 30.11.16.
 */
public class WordDatabase {

    //TODO: database actually put as HashMap? Some access easier then

    private ArrayList<String> id;
    private ArrayList<Float> meaning;
    private ArrayList<String> words;

    /**
     * Constructs a new database.
     */
    public WordDatabase() {
        this.id = new ArrayList<>();
        this.meaning = new ArrayList<>();
        this.words = new ArrayList<>();

        // this.wordDatabase = new HashMap<String, HashMap <String, ArrayList<String> > > ();
    }

    /**
     * add meaning and language to its lists
     *
     * @param id
     * @param meaning
     * @param word
     */
    public void addEntry(String id, float meaning, String word) {
        (this.id).add(id);
        (this.meaning).add(meaning);
        (this.words).add(word);
        //check whether lang already in there?
        //is there already a HM with meaning key? --> add word to AL else create new HM
    }

    /**
     * returns meaning for a wordID
     *
     * @param wordID ID of the word (int) whose meaning is asked for
     * @return meaning of the word as String
     */
    public Float getMeaning(String wordID){
        int pos = id.indexOf(wordID);
        return meaning.get(pos);
    }

    /**
     * return the word for a given wordID
     * @param wordID ID of the word (int) whose meaning is asked for
     * @return actual word (String)
     */
    public String getWord(String wordID){
        int pos = id.indexOf(wordID);
        return words.get(pos);
    }

    /**
     * checks whether tow words have the same meaning
     *
     * @param id1 ID as integer of first word
     * @param id2 ID as integer of second word
     * @return bool
     */
    public boolean sameMeaning(String id1, String id2) {

        if(id1 == "")
            System.out.println("first word ID was empty");

        if(id2 == "")
            System.out.println("second word ID was empty");

        float meaning1 = getMeaning(id1);
        float meaning2 = getMeaning(id2);

        return (Math.round(meaning1*1000) == Math.round(meaning2*1000));
    }

}
