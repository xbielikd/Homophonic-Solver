package cipherGenerators;

import java.util.Arrays;

public class MonoSubCipherGenerator {

    public static int[] encrypt(String plaintext, int[] key) {
        plaintext = plaintext.toUpperCase();  // bezpečne uppercase
        int[] ct = new int[plaintext.length()];

        for (int i = 0; i < plaintext.length(); i++) {
            char c = plaintext.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                ct[i] = key[c - 'A'];
            } else {
                throw new IllegalArgumentException("Plaintext containts illegal char: " + c);
            }
        }

        return ct;
    }

    public static void main(String[] args) {
//        String plaintext = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGANDTHENRUNSAGAINTOCHECKALLTHELETTERFREQUENCIES";
        String plaintext2 = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGANDTHENRUNSINTOFORESTWHEREITMEETSANOTHERFOXTHATTELLSASTRANGESTORYABOUTHIDDENTREASUREANDSECRETMAPSBURIEDUNDEROLDOAKTREES";

        int[] key = new int[26];
        for (int i = 0; i < 26; i++) key[i] = i;

        int[] ciphertext = encrypt(plaintext2, key);

        System.out.println("Ciphertext (int[]):");
        System.out.println(Arrays.toString(ciphertext));

        StringBuilder sb = new StringBuilder();
        for (int val : ciphertext) {
            sb.append(val).append(" ");
        }
        System.out.println("Ciphertext (string):");
        System.out.println(sb.toString().trim());
    }
}
