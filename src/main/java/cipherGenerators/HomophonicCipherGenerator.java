package cipherGenerators;


import java.util.*;

public class HomophonicCipherGenerator {

    private static Random rand = new Random();

    public static List<Integer>[] generateKey(int[] homophoneCounts) {

        if (homophoneCounts.length != 26)
            throw new IllegalArgumentException("Array needs to have length of 26.");

        @SuppressWarnings("unchecked")
        List<Integer>[] key = new List[26];

        // Build a shuffled pool of all symbol numbers
        int total = 0;
        for (int c : homophoneCounts) total += c;

        List<Integer> symbolPool = new ArrayList<>(total);
        for (int i = 0; i < total; i++) symbolPool.add(i);
        Collections.shuffle(symbolPool, rand);  // randomise symbol assignment

        int poolIdx = 0;
        for (int i = 0; i < 26; i++) {
            key[i] = new ArrayList<>();
            for (int j = 0; j < homophoneCounts[i]; j++) {
                key[i].add(symbolPool.get(poolIdx++));
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

//        String plaintext =
//                "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGANDTHENRUNSINTOFORESTWHEREITMEETSANOTHERFOXTHATTELLSASTRANGESTORYABOUTHIDDENTREASUREANDSECRETMAPSBURIEDUNDEROLDOAKTREES";
//
//        int[] homophoneCounts = new int[26];
//
//        for (int i = 0; i < 26; i++) homophoneCounts[i] = 1;
//
//        homophoneCounts['E' - 'A'] = 2;
//        homophoneCounts['T' - 'A'] = 2;
//        homophoneCounts['A' - 'A'] = 2;
//        homophoneCounts['O' - 'A'] = 2;
//        homophoneCounts['I' - 'A'] = 2;
        String plaintext =
                "THESUNROSEOVERTHEOLDCITYWALLSANDTHEMARKETPLACECAMEALIVEWITHTHESOUNDSOFVENDORSCALLINGOUTTHEIRPRICESFRESHFRUITSANDVEGETABLESWEREPILEDHIGHONCARTSWOODENCRATESOFAPPLESORANGESANDPEARSSATALONGSIDEBASKETSOFEGGSNEWLYBAKEDBREADFILLEDTHEAIRWITHARICHSCENTBUTTERSTILLGLISTENINGFROMTHECHURSTOODNEARTHEFOUNTAINWHEREWOMENHADGATHEREDTOFETCHTHEIRMORNINGWATERTHECHILDRENRANPASTLAUGHINGANDSHOUTINGWHILETHEIRPARENTSBARTEREDNOISILYWITHMERCHANTSWHOSESTALLSSTRETCHEDALONGTHENARROWCOBBLESTONESTREETANOLDMANWITHAGREYBEARDSATQUIETLYONASTONEBENCHNEARTHEENTRANCETOTHEMARKETPLACEHEWATCHEDTHEBUSLYCROWDWITHWISEYESANDSMILEDSOFTLY";

        int[] homophoneCounts = new int[26];

// base: every letter gets 1
        for (int i = 0; i < 26; i++) homophoneCounts[i] = 1;

// extra homophones for high-frequency letters
//        homophoneCounts['E' - 'A'] = 6;
//        homophoneCounts['T' - 'A'] = 4;
//        homophoneCounts['A' - 'A'] = 4;
//        homophoneCounts['O' - 'A'] = 4;
//        homophoneCounts['I' - 'A'] = 3;
//        homophoneCounts['N' - 'A'] = 3;
//        homophoneCounts['S' - 'A'] = 3;
//        homophoneCounts['H' - 'A'] = 3;
//        homophoneCounts['R' - 'A'] = 3;
//        homophoneCounts['L' - 'A'] = 2;
//
//        homophoneCounts['D' - 'A'] = 2;
//        homophoneCounts['C' - 'A'] = 2;
//        homophoneCounts['U' - 'A'] = 2;
//        homophoneCounts['M' - 'A'] = 2;

        homophoneCounts['C' - 'A'] = 5;
        homophoneCounts['M' - 'A'] = 4;
        homophoneCounts['F' - 'A'] = 4;
        homophoneCounts['R' - 'A'] = 4;
        homophoneCounts['T' - 'A'] = 3;
        homophoneCounts['W' - 'A'] = 3;
        homophoneCounts['H' - 'A'] = 3;
        homophoneCounts['I' - 'A'] = 3;
        homophoneCounts['V' - 'A'] = 3;





// verify
        int total = Arrays.stream(homophoneCounts).sum();
        System.out.println("Total homophones: " + total);


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
