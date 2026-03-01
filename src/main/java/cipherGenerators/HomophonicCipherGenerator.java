package cipherGenerators;


import java.util.*;

public class HomophonicCipherGenerator {

    private static Random rand = new Random();

    public static List<Integer>[] generateKey(int[] homophoneCounts) {

        if (homophoneCounts.length != 26)
            throw new IllegalArgumentException("Array needs to have length of 26.");

        @SuppressWarnings("unchecked")
        List<Integer>[] key = new List[26];

        int symbolCounter = 0;

        for (int i = 0; i < 26; i++) {
            key[i] = new ArrayList<>();

            for (int j = 0; j < homophoneCounts[i]; j++) {
                key[i].add(symbolCounter++);
            }
        }

        return key;
    }


    public static int[] encrypt(String plaintext, List<Integer>[] key) {

        plaintext = plaintext.toUpperCase();
        int[] ct = new int[plaintext.length()];

        for (int i = 0; i < plaintext.length(); i++) {

            char c = plaintext.charAt(i);

            if (c < 'A' || c > 'Z')
                throw new IllegalArgumentException("Illegal char: " + c);

            int letterIndex = c - 'A';

            List<Integer> homophones = key[letterIndex];

            ct[i] = homophones.get(rand.nextInt(homophones.size()));
        }

        return ct;
    }

    public static void main(String[] args) {

        String plaintext =
                "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGANDTHENRUNSINTOFORESTWHEREITMEETSANOTHERFOXTHATTELLSASTRANGESTORYABOUTHIDDENTREASUREANDSECRETMAPSBURIEDUNDEROLDOAKTREES";

        int[] homophoneCounts = new int[26];

        for (int i = 0; i < 26; i++) homophoneCounts[i] = 1;

        homophoneCounts['E' - 'A'] = 2;
        homophoneCounts['T' - 'A'] = 2;
        homophoneCounts['A' - 'A'] = 2;
        homophoneCounts['O' - 'A'] = 2;
        homophoneCounts['I' - 'A'] = 2;

        List<Integer>[] key = generateKey(homophoneCounts);

        int[] ciphertext = encrypt(plaintext, key);

        System.out.println("Number of symbols: " +
                Arrays.stream(homophoneCounts).sum());

        System.out.println("Ciphertext:");
        for (int i =0; i< ciphertext.length; i++){
            System.out.print(ciphertext[i] + " ");
        }
    }
}
