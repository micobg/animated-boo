package Indexer;

import models.text.TextManipulator;
import models.text.TextManipulatorIndexer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Index all words in given text.
 */
public class Indexer {

    public static void main(String[] args) {
        String fileToBeIndexed = args[0];
        Path pathToFile = FileSystems.getDefault().getPath("src", "files", fileToBeIndexed);

        try {
            readFile(pathToFile);
        } catch (IndexerException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Indexing done!");
    }

    /**
     * Read file and save all the words.
     *
     * @param pathToFile Path object to file to be read
     *
     * @throws IndexerException if something went wrong
     */
    private static void readFile(Path pathToFile) throws IndexerException {
        TextManipulator textManipulator = new TextManipulatorIndexer();

        try {
            Files.lines(pathToFile).forEach((String line) ->
                textManipulator.extractWords(line)
            );
        } catch (IOException ex) {
            throw new IndexerException("Cannot read the file.");
        }
    }
}