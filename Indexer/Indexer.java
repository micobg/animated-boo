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
            // TODO: handle exception
            System.out.println("Error!");
        }

        System.out.println("Hi!");
    }

    private static void readFile(Path pathToFile) throws IndexerException {
        try {
            Files.lines(pathToFile).forEach((String line) -> {
                System.out.println(line);

                Pattern  pattern = Pattern.compile("\\p{L}+");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    String a = matcher.group();
                    System.out.println(a);
                }


                // extract words

                // for each word
                // remove stop words
                // generate and save terms
            });
        } catch (IOException ex) {
            throw new IndexerException("Cannot read the file.");
        }
    }


}
