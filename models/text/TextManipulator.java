package models.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TextManipulator {

    public abstract void wordWorker(Matcher word);

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
            wordWorker(matcher);
        }
    }
}
