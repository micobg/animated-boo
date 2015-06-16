package Indexer;

import java.util.HashSet;
import java.util.Set;

public class Word {

    private final Integer minWordLength = 3;
    private final Integer minTermLength = 2;
    private final Integer deletionsMaxDepth = 2;

    private String word;
    private Set<String> terms = new HashSet<>();

    public Word(String word) {
        this.word = word;
    }

    public boolean isShort() {
        return word.length() < minWordLength;
    }

    public boolean isStopWord() {
        return StopWords.isStopWord(word);
    }

    /**
     * Save the word and all terms that can be created by deletions.
     */
    public void save() {
        generateTerms();

        // TODO: save terms (put SQL in different class)
    }

    /**
     * Generate all possible terms by deletions depends on word length and minimum term length.
     */
    private void generateTerms() {
        Integer depth = word.length() < minTermLength + deletionsMaxDepth ?
            word.length() - minTermLength :
            deletionsMaxDepth;

        StringBuilder wordObject = new StringBuilder(word);
        generateDeletions(wordObject, depth);
    }

    /**
     * Generate all terms by removing one character from given word. Recursively do it for ever new term while hit the limit of deletion's depth.
     *
     * @param word word that to be used for generating new terms
     * @param depth depth of deletions (if it is 0 then stop recursion)
     */
    private void generateDeletions(StringBuilder word, Integer depth) {
        if(depth < 1) {
            return;
        }

        /**
         * TODO: stop recursion if this word is already in db (as word, not as term!!!)
         * All words (without terms?) can be stored also in Redis for fast checking.
         */

        Integer newDepth = depth - 1;

        for(Integer i = 0; i < word.length(); i++) {
            StringBuilder newTerm = word.deleteCharAt(i);

            if(!terms.contains(newTerm)) {
                // add term
                terms.add(newTerm.toString());

                // generate next level terms
                generateDeletions(newTerm, newDepth);
            }
        }
    }
}
