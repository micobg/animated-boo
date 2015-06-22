package models.word;

import java.util.Set;

public class WordCorrector extends Word {

    protected Set<String> terms = null;

    /**
     * Constructor of the word
     *
     * @param word the word
     */
    public WordCorrector(String word) {
        super(word);
    }

//    public void generateTerms() {
//        super.generateTerms(word);
//    }

    protected void handleNewTerm(String term, Integer editDistance, Integer depth) {
        // save terms into this.terms
    }
}
