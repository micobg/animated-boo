package Indexer;

import Indexer.persisters.TermType;
import Indexer.persisters.TermsMysqlPersister;
import Indexer.persisters.TermsRedisPersister;

import java.util.*;

public class Word {

    private final Integer minWordLength = 3;
    private final Integer minTermLength = 2;
    private final Integer deletionsMaxDepth = 2;

    private String word;

    /**
     * Terms: the key contains the term and value - edit distance
     */
    private Map<String, Integer> terms = new HashMap<>();

    public Word(String word) {
        this.word = normalizer(word);
    }

    /**
     * Normalize given word - remove free spaces, convert to lower case and etc.
     *
     * @param word given word as String
     *
     * @return normalized word as String
     */
    private String normalizer(String word) {
        return word.trim().toLowerCase();
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
     * Save the word and all terms that can be created by deletions.
     */
    public void save() {
        TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();
        TermsRedisPersister redisPersister = new TermsRedisPersister();

        Map<String, Object> wordData = mysqlPersister.fetchTerm(word);

        if (wordData.isEmpty()) {
            // save the word
            redisPersister.saveTerm(word, TermType.WORD);
            Long wordId = mysqlPersister.saveTerm(word, TermType.WORD);

            // save all terms
            generateTerms(wordId);
        } else if (wordData.get("type") == TermType.TERM.toString()) {
            // TODO
        }
        // else if (wordData.get("type") == TermType.WORD.toString()) => nothing to do


        // save all terms
//        persister.saveTerms(terms, wordId);
    }

    /**
     * Generate all possible terms by deletions depends on word length and minimum term length.
     */
    private void generateTerms(Long wordId) {
        Integer depth = word.length() < minTermLength + deletionsMaxDepth ?
            word.length() - minTermLength :
            deletionsMaxDepth;

        generateDeletions(word, depth);
    }

    /**
     * Generate all terms by removing one character from given word. Recursively do it for ever new term while hit the limit of deletion's depth.
     *
     * @param word word that to be used for generating new terms
     * @param depth depth of deletions (if it is 0 then stop recursion)
     */
    private void generateDeletions(String word, Integer depth) {
        if(depth < 1) {
            return;
        }

        /**
         * TODO: stop recursion if this word is already in db (as word, not as term!!!)
         * All words (without terms?) can be stored also in Redis for fast checking.
         */

        Integer newDepth = depth - 1;

        for(Integer i = 0; i < word.length(); i++) {
            StringBuilder newTerm = new StringBuilder(word);
            newTerm.deleteCharAt(i);

            if(!terms.containsKey(newTerm)) {
                // add term
                terms.put(newTerm.toString(), deletionsMaxDepth - depth + 1);

                // generate next level terms
                generateDeletions(newTerm.toString(), newDepth);
            }
        }
    }
}
