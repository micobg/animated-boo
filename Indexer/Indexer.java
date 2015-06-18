package Indexer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static void readFile(Path pathToFile) throws IndexerException {
        try {
            Files.lines(pathToFile).forEach((String line) -> {
                Pattern pattern = Pattern.compile("\\p{L}+");
                Matcher matcher = pattern.matcher(line);

                // extract words
                while (matcher.find()) {
                    Word word = new Word(matcher.group());

                    if (!word.isShort() && !word.isStopWord()) {
                        word.save();
                    }
                }
            });
        } catch (IOException ex) {
            throw new IndexerException("Cannot read the file.");
        }
    }
}