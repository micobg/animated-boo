package Corrector;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Corrector {

    public static void main (String args[]) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String input;

            while((input = br.readLine()) != null){
                System.out.println(input);
            }

        } catch(IOException io) {
            System.err.println("Read input error.");
        }
    }
}
