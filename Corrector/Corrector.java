package Corrector;

import models.text.TextManipulator;
import models.text.TextManipulatorCorrector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Corrector {

    public static void main (String args[]) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String input;
            while((input = br.readLine()) != null) {
                TextManipulator textManipulator = new TextManipulatorCorrector();
                textManipulator.extractWords(input);
            }
        } catch(IOException io) {
            System.err.println("Read input error.");
        }
    }
}
