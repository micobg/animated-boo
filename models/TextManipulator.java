package models;

import Indexer.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextManipulator {

    public enum Workers {
        SAVE_WORD;
        // TODO: Add Corrector
    }
    private List<Workers> workers = new ArrayList<>();

    public void setWorker(Workers worker) {
        if (!workers.contains(worker)) {
            workers.add(worker);
        }
    }

    /**
     * Split given text to words and call a worker for each of them.
     *
     * @param text line of text
     */
    public void extractWords(String text) {
        Pattern pattern = Pattern.compile("\\p{L}+");
        Matcher matcher = pattern.matcher(text);

        // extract words
        while (matcher.find()) {
            Word word = new Word(matcher.group());

            if (!word.isShort() && !word.isStopWord()) {
                // TODO: make it also for Corrector
                if (workers.contains(Workers.SAVE_WORD)) {
                    word.save();
                }
            }
        }
    }
}
