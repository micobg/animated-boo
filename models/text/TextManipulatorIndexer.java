package models.text;

import models.word.Word;
import models.word.WordIndexer;

public class TextManipulatorIndexer extends TextManipulator {

    public void wordWorker(String word) {
        Word wordObject = new WordIndexer(word);

        if (!wordObject.isShort() && !wordObject.isStopWord()) {
            wordObject.save();
        }
    }
}
