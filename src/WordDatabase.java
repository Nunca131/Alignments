import java.util.ArrayList;
/**
 * Created by nancy on 30.11.16.
 */
public class WordDatabase {

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
    }

    /**
     * returns meaning for a wordID
     *
     * @param wordID
     * @return meaning
     */
    public String getMeaning(int wordID){
        return meaning.get(wordID);
    }

    /**
     * return the word for a given wordID
     * @param wordID
     * @return
     */
    public String getWord(int wordID){
        return words.get(wordID);
    }

    //TODO get a list of words for a given meaning (think about whether only for one lang or several?)
    /*
    public ArrayList<String> get words(String meaning, String lang){
        ArrayList<String> retWords = new ArrayList<String>();
        int firstAppearanceOfLang = this.language.indexOf(lang);
        for(int i = firstAppearanceOfLang; i < words.size(); i++){

        }
     }
     */


    /**
     * returns language for a wordID
     *
     * @param wordID
     * @return language
     */
    public String getLanguage(int wordID){
        return language.get(wordID);
    }

    /**
     * checks whether tow words have the same meaning
     *
     * @param id1
     * @param id2
     * @return bool
     */
    public boolean sameMeaning(int id1, int id2) {

        if(id1 == -1)
            System.out.println("first word ID not found");

        if(id2 == -1)
            System.out.println("second word ID not found");

        if( (meaning.get(id1)).equals(meaning.get(id2)) )
            return true;
        else
            return false;
    }

}
