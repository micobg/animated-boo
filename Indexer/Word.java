package Indexer;

import Indexer.persisters.TermType;
import Indexer.persisters.TermsMysqlPersister;

import java.util.Map;
import java.util.Set;

public class Word {

    private final Integer minWordLength = 3;
    private final Integer minTermLength = 2;
    private final Integer deletionsMaxDepth = 2;

    /**
     * State
     */
    private Long id = null;
    private String type = null;
    private String word = null;

    public Word(String word) {
        this.word = normalizer(word);

        TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();
        Map<String, Object> wordData = mysqlPersister.fetchTerm(word);
        if (!wordData.isEmpty()) {
            id = (Long)wordData.get("id");
            type = (String)wordData.get("type");
        }
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

        if (id == null) {
            // save the word
            id = mysqlPersister.saveTerm(word, TermType.WORD);

            // save all terms
            generateTerms();
        } else if (type.equals(TermType.TERM.toString())) {
            // change type of the term to word
            mysqlPersister.convertTermToWord(id);

            // save all terms
            generateTerms();
        }
        // else if (type.equals(TermType.WORD.toString())) => nothing to do
    }

    /**
     * Generate all possible terms by deletions depends on word length and minimum term length and save them.
     */
    private void generateTerms() {
        Integer depth = word.length() < minTermLength + deletionsMaxDepth ?
            word.length() - minTermLength :
            deletionsMaxDepth;

        generateDeletions(word, depth);
    }

    /**
     * Generate all terms by removing one character from given word. Recursively do it for ever new term while hit the
     * limit of deletion's depth. Save all terms and their relations to the word.
     *
     * @param word word that to be used for generating new terms
     * @param depth depth of deletions (if it is 0 then stop recursion)
     */
    private void generateDeletions(String word, Integer depth) {
        if(depth < 1) {
            return;
        }

        Integer newDepth = depth - 1;
        Integer editDistance = deletionsMaxDepth - depth + 1;

        for(Integer i = 0; i < word.length(); i++) {
            StringBuilder newTerm = new StringBuilder(word);
            newTerm.deleteCharAt(i);

            // TODO: refactor this method and make it reusable (for Corrector)

            TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();

            Map<String, Object> termData = mysqlPersister.fetchTerm(normalizer(newTerm.toString()));

            if (termData.isEmpty()) {
                // save the term and relation
                Long termId = mysqlPersister.saveTerm(newTerm.toString(), TermType.TERM);
                mysqlPersister.saveRelation(termId, id, editDistance);

                // generate next level terms
                generateDeletions(newTerm.toString(), newDepth);
            } else if (termData.get("type").equals(TermType.TERM.toString())) {
                // just add new relation for this word
                mysqlPersister.saveRelation((Long)termData.get("id"), id, editDistance);
            }
            else if (termData.get("type").equals(TermType.WORD.toString())) {
                // add new relation for this word
                mysqlPersister.saveRelation((Long)termData.get("id"), id, editDistance);

                // get all terms/words related of this term
                Set<Long> getInheritedRelations =
                    mysqlPersister.getInheritedRelations((Long)termData.get("id"), deletionsMaxDepth - editDistance);

                // add all inherited related terms as relation of the word
                for (Long relationId : getInheritedRelations) {
                    mysqlPersister.saveRelation(relationId, id, editDistance);
                }
            }
        }
    }
}
