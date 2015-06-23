package models.word;

import Indexer.StopWords;
import persisters.TermType;
import persisters.TermsMysqlPersister;

import java.util.Map;

public abstract class Word {

    protected final Integer minWordLength = 3;
    protected final Integer minTermLength = 2;
    protected final Integer deletionsMaxDepth = 2;

    /**
     * State
     */
    protected Long id = null;
    protected String type = null;
    protected String word = null;

    /**
     * Max depth of deletions for the word
     */
    protected Integer wordMaxDeletionDepth = null;

    /**
     * Call methods (different for every class) for given term.
     *
     * @param term         the term
     * @param editDistance edit distance of the term
     * @param depth        depth of term generation
     */
    protected abstract void handleNewTerm(String term, Integer editDistance, Integer depth);

    /**
     * Constructor of the word
     *
     * @param word the word
     */
    public Word(String word) {
        this.word = normalizer(word);
        wordMaxDeletionDepth = calculateMaxDeletionDepth();

        TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();
        Map<String, Object> wordData = mysqlPersister.fetchTerm(word);
        if (!wordData.isEmpty()) {
            id = (Long)wordData.get("id");
            type = (String)wordData.get("type");
        }
    }

    /**
     * Is the word shorter than minimum word length.
     *
     * @return true if it is too short
     */
    public boolean isShort() {
        return word.length() < minWordLength;
    }

    /**
     * Is the word stop word.
     *
     * @return true if it is stop word
     */
    public boolean isStopWord() {
        return StopWords.isStopWord(word);
    }

    /**
     * Return the word as String.
     *
     * @return
     */
    public String toString() {
        return word;
    }

    /**
     * Save the word and all terms that can be created by deletions.
     */
    public void save() {
        TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();
        Boolean generateTerms = false;

        if (id == null) {
            // save the word
            id = mysqlPersister.saveTerm(word, TermType.WORD);
            generateTerms = true;
        } else if (type.equals(TermType.TERM.toString())) {
            // change type of the term to word
            mysqlPersister.convertTermToWord(id);
            generateTerms = true;
        }
        // else if (type.equals(TermType.WORD.toString())) => nothing to do

        if (generateTerms) {
            // save all terms
            generateTerms(word, wordMaxDeletionDepth);
        }
    }

    /**
     * Normalize given word - remove free spaces, convert to lower case and etc.
     *
     * @param word given word as String
     *
     * @return normalized word as String
     */
    protected String normalizer(String word) {
        return word.trim().toLowerCase();
    }

    /**
     * Calculate max deletion depth for the word depends on word's length and configs.
     *
     * @return max deletion depth for the word
     */
    protected Integer calculateMaxDeletionDepth() {
        return word.length() < minTermLength + deletionsMaxDepth ? word.length() - minTermLength : deletionsMaxDepth;
    }

    /**
     * Generate all terms by removing one character from given word. Recursively do it for ever new term while hit the limit of deletion's depth. Save all terms
     * and their relations to the word.
     *
     * @param word  word that to be used for generating new terms
     */
    protected void generateTerms(String word, Integer depth) {
        if (depth < 1) {
            return;
        }

        Integer nextLevelDepth = depth - 1;
        Integer editDistance = deletionsMaxDepth - depth + 1;

        for (Integer i = 0; i < word.length(); i++) {
            StringBuilder newTerm = new StringBuilder(word);
            newTerm.deleteCharAt(i);

            handleNewTerm(newTerm.toString(), editDistance, nextLevelDepth);
        }
    }
}
