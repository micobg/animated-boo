package models.word;

import Indexer.persisters.TermType;
import Indexer.persisters.TermsMysqlPersister;

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
        return id != null && type == TermType.WORD.toString();
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

    public void findBestSuggestion() {
        generateTerms(word, wordMaxDeletionDepth);

        // load all words that are related with this terms
        TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();
        Set<String> relatedWords = mysqlPersister.loadRelatedWords(terms);

        // return word with smallest editDistance
    }
}
