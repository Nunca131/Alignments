package container;

import java.util.ArrayList;
// import java.util.HashMap;

/**
 * Created by nancy on 30.11.16.
 */
public class WordDatabase {

    //TODO: database actually put as HashMap? Some access easier then

    // "HashMap< languages, HashMap < meanings, ArrayList<words> > >"
    // private HashMap<String, HashMap <String, ArrayList<String> > > wordDatabase;

    private ArrayList<String> meaning;
    private ArrayList<String> language;
    private ArrayList<String> words;

    /**
     * Constructs a new database.
     */
    public WordDatabase() {
        this.meaning = new ArrayList<String>();
        this.language = new ArrayList<String>();
        this.words = new ArrayList<String>();

        // this.wordDatabase = new HashMap<String, HashMap <String, ArrayList<String> > > ();
    }

    /**
     * add meaning and language to its lists
     *
     * @param meaning
     * @param word
     * @param language
     */
    public void addEntry(String meaning, String word, String language) {
        (this.meaning).add(meaning);
        (this.words).add(word);
        (this.language).add(language);
        //check whether lang already in there?
        //is there already a HM with meaning key? --> add word to AL else create new HM
    }

    /**
     * returns meaning for a wordID
     *
     * @param wordID ID of the word (int) whose meaning is asked for
     * @return meaning of the word as String
     */
    public String getMeaning(int wordID){
        return meaning.get(wordID);
    }

    /**
     * return the word for a given wordID
     * @param wordID ID of the word (int) whose meaning is asked for
     * @return actual word (String)
     */
    public String getWord(int wordID){
        return words.get(wordID);
    }

    //TODO get a list of word IDs for a given meaning and language (think about whether only for one lang or several?)

    /*public ArrayList<int> getWords(String meaning, String lang){
        ArrayList<String> retWords = new ArrayList<String>();
        int firstAppearanceOfLang = this.language.indexOf(lang);
        for(int i = firstAppearanceOfLang; i < words.size(); i++){

        }
        return retWords;
     }*/



    /**
     * returns language for a wordID
     *
     * @param wordID ID of the word (int) whose language is looked for
     * @return language
     */
    public String getLanguage(int wordID){
        return language.get(wordID);
    }

    /**
     * checks whether tow words have the same meaning
     *
     * @param id1 ID as integer of first word
     * @param id2 ID as integer of second word
     * @return bool
     */
    public boolean sameMeaning(int id1, int id2) {

        if(id1 == -1)
            System.out.println("first word ID not found");

        if(id2 == -1)
            System.out.println("second word ID not found");

        return (meaning.get(id1)).equals(meaning.get(id2));
    }

}
