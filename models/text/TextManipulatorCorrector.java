package models.text;

import models.word.WordCorrector;

import java.util.regex.Matcher;

public class TextManipulatorCorrector extends TextManipulator {

    protected StringBuffer inputText = null;
    protected StringBuffer correctedText = null;

    protected Integer inputTextOffset = 0;
    protected Integer correctedTextOffset = 0;

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
                Integer wordDiff = suggestion.length() - word.toString().length();

                inputText = inputText.replace(matcher.start() + inputTextOffset, matcher.end() + inputTextOffset, "<" + word.toString() + ">");
                correctedText = correctedText.replace(matcher.start() + correctedTextOffset, matcher.end() + correctedTextOffset, suggestion);

                inputTextOffset += 2;
                if (wordDiff != 0) {
                    correctedTextOffset += wordDiff;
                }
            }
        }
    }
}
