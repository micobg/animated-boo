package models.word;

import persisters.TermType;
import persisters.TermsMysqlPersister;

import java.util.HashSet;
import java.util.Set;

public class WordCorrector extends Word {

    protected Set<String> terms = new HashSet<>();

    /**
     * Constructor of the word
     *
     * @param word the word
     */
    public WordCorrector(String word) {
        super(word);
    }

    /**
     * If the word has been found into db it is correct.
     *
     * @return true if the word is correct
     */
    public boolean isCorrect() {
        return id != null && type.equals(TermType.WORD.toString());
    }

    /**
     * Add given term int terms Set.
     *
     * @param term         the term
     * @param editDistance edit distance of the term
     * @param depth        depth of term generation
     */
    protected void handleNewTerm(String term, Integer editDistance, Integer depth) {
        terms.add(term);

        // generate next level terms
        generateTerms(term, depth);
    }

    public String findBestSuggestion() {
        generateTerms(word, wordMaxDeletionDepth);

        // return word with smallest editDistance
        TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();
        return mysqlPersister.loadSuggestion(terms);
    }
}
