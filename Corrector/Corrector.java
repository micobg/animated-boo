package Corrector;

import models.text.TextManipulatorCorrector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Corrector {

    public static void main (String args[]) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            TextManipulatorCorrector textManipulator = new TextManipulatorCorrector();

            String input;
            input = br.readLine();
                textManipulator.extractWords(input);

            System.out.println(
                "\n" +
                "Оригиналният текст с оградени в <> сгрешените думи и коригирания текст след него:\n" +
                "---------------------------------------------------------------------------------"
            );
            System.out.println(textManipulator.getInputText());
            System.out.println(textManipulator.getCorrectedText());
        } catch(IOException io) {
            System.err.println("Read input error.");
        }
    }
}
