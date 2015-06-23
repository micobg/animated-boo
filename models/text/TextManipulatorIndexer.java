package models.text;

import models.word.WordIndexer;

import java.util.regex.Matcher;

public class TextManipulatorIndexer extends TextManipulator {

    /**
     * Call save() method for every word if it is not stop word or too short.
     *
     * @param matcher Matcher that match the words
     */
    public void wordWorker(Matcher matcher) {
        WordIndexer word = new WordIndexer(matcher.group());

        if (!word.isShort() && !word.isStopWord()) {
            word.save();
        }
    }
}
