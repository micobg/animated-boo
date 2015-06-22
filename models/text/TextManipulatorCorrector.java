package models.text;

import models.word.WordCorrector;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class TextManipulatorCorrector extends TextManipulator {

    protected StringBuffer inputText = new StringBuffer();
    protected StringBuffer correctedText = new StringBuffer();

    /**
     *
     *
     * @param matcher the word
     */
    public void wordWorker(Matcher matcher) {
        WordCorrector word = new WordCorrector(matcher.group());


        /**
         * TODO:
         *  - if word is into db (as word) => it's correct
         *  - else:
         *  -- generate all terms
         *  -- load al words that are related with this terms
         */

        if (word.isCorrect()) {
            matcher.appendReplacement(inputText, word.toString());
            matcher.appendReplacement(correctedText, word.toString());
        } else {
            word.findBestSuggestion();
        }
    }
}
