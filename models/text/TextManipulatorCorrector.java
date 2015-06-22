package models.text;

import models.word.Word;
import models.word.WordCorrector;

public class TextManipulatorCorrector extends TextManipulator {

    public void wordWorker(String word) {
        Word wordObject = new WordCorrector(word);

        /**
         * TODO:
         *  - if word is into db (as word) => it's correct
         *  - else:
         *  -- generate all terms
         *  -- load al words that are related with this terms
         */

        if (!wordObject.isShort() && !wordObject.isStopWord()) {
            // word.***();
        }
    }
}
