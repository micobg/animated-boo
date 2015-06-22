package models.word;

import Indexer.persisters.TermType;
import Indexer.persisters.TermsMysqlPersister;

import java.util.Map;
import java.util.Set;

public class WordIndexer extends Word {

    /**
     * Constructor of the word
     *
     * @param word the word
     */
    public WordIndexer(String word) {
        super(word);
    }

    /**
     * Call saveTermIntoDb() method.
     *
     * @param term         the term
     * @param editDistance edit distance of the term
     * @param depth        depth of term generation
     */
    protected void handleNewTerm(String term, Integer editDistance, Integer depth) {
        saveTermIntoDb(term, editDistance, depth);
    }

    /**
     * Save the term into db and add a relation to the word. Generate next level terms (if applicable).
     *
     * @param term           the term
     * @param editDistance   edit distance of the term
     * @param nextLevelDepth depth of term generation
     */
    protected void saveTermIntoDb(String term, Integer editDistance, Integer nextLevelDepth) {
        TermsMysqlPersister mysqlPersister = new TermsMysqlPersister();

        Map<String, Object> termData = mysqlPersister.fetchTerm(normalizer(term));

        if (termData.isEmpty()) {
            // save the term and relation
            Long termId = mysqlPersister.saveTerm(term, TermType.TERM);
            mysqlPersister.saveRelation(termId, id, editDistance);

            // generate next level terms
            generateTerms(term, nextLevelDepth);
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
