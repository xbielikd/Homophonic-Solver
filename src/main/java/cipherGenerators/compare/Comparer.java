package cipherGenerators.compare;

public class Comparer {
    public static double comparePlaintexts(String original, String decrypted) {

        if (original.length() != decrypted.length()) {
            throw new IllegalArgumentException("Texty nemajú rovnakú dĺžku.");
        }

        int matches = 0;
        int length = original.length();

        StringBuilder diffLine = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char o = original.charAt(i);
            char d = decrypted.charAt(i);

            if (o == d) {
                matches++;
                diffLine.append(" ");   // zhoda
            } else {
                diffLine.append("^");   // rozdiel
            }
        }

        double percent = 100.0 * matches / length;

        System.out.println("ORIGINAL :");
        System.out.println(original);

        System.out.println("DECRYPTED:");
        System.out.println(decrypted);

        System.out.println("DIFF     :");
        System.out.println(diffLine.toString());

        System.out.printf("Zhoda: %.2f %% (%d / %d)%n", percent, matches, length);

        return percent;
    }

    static void main() {
        String golden="THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGANDTHENRUNSINTOFORESTWHEREITMEETSANOTHERFOXTHATTELLSASTRANGESTORYABOUTHIDDENTREASUREANDSECRETMAPSBURIEDUNDEROLDOAKTREES";
        String pt="THEQUICKBROWNGEFJUMPSEXERTHELAZYDEVANDTHENRUNSENTOGORESTWHEREITMEETSANETHERGOFTHATTELLSASTRANVESTORYABOUTHIDDENTREASUREANDSECRETMAPSBURIEDUNDERELDEAKTREES";
        comparePlaintexts(golden, pt);
    }
}
