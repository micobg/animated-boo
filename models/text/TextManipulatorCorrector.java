package models.text;

import models.word.WordCorrector;

import java.util.regex.Matcher;

public class TextManipulatorCorrector extends TextManipulator {

    protected StringBuffer inputText = null;
    protected StringBuffer correctedText = null;

    protected Integer offset = 0;

    /**
     * Split given text to words and call a worker for each of them.
     *
     * @param text line of text
     */
    public void extractWords(String text) {
        inputText = new StringBuffer(text);
        correctedText = new StringBuffer(text);

        super.extractWords(text);
    }

    public String getInputText() {
        return inputText.toString();
    }

    public String getCorrectedText() {
        return correctedText.toString();
    }

    /**
     * Search for best suggestion for current word and replace it into the text.
     *
     * @param matcher the word
     */
    public void wordWorker(Matcher matcher) {
        WordCorrector word = new WordCorrector(matcher.group());

        if (word.isStopWord()) {
            return;
        }

        if (!word.isCorrect()) {
            String suggestion = word.findBestSuggestion();

            // show and correct the mistake
            if (suggestion != null) {
                inputText = inputText.replace(matcher.start() + offset, matcher.end() + offset, "<" + word.toString() + ">");
                correctedText = correctedText.replace(matcher.start(), matcher.end(), suggestion);

                Integer wordDiff = word.toString().length() + 2 - suggestion.length();
                if (wordDiff != 0) {
                    offset += wordDiff;
                }
            }
        }
    }
}
