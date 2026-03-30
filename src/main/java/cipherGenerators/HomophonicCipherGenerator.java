package cipherGenerators;


import solvers.HomophonicAnnealingSolver;

import java.util.*;
import java.util.stream.Collectors;

public class HomophonicCipherGenerator {

    private static Random rand = new Random();
//    private static final double CONST = 0.1;
//    private static final int FIXED_NUMBER = 5;


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
        String plaintext =
                "THESUNROSEOVERTHEOLDCITYWALLSANDTHEMARKETPLACECAMEALIVEWITHTHESOUNDSOFVENDORSCALLINGOUTTHEIRPRICESFRESHFRUITSANDVEGETABLESWEREPILEDHIGHONCARTSWOODENCRATESOFAPPLESORANGESANDPEARSSATALONGSIDEBASKETSOFEGGSNEWLYBAKEDBREADFILLEDTHEAIRWITHARICHSCENTBUTTERSTILLGLISTENINGFROMTHECHURSTOODNEARTHEFOUNTAINWHEREWOMENHADGATHEREDTOFETCHTHEIRMORNINGWATERTHECHILDRENRANPASTLAUGHINGANDSHOUTINGWHILETHEIRPARENTSBARTEREDNOISILYWITHMERCHANTSWHOSESTALLSSTRETCHEDALONGTHENARROWCOBBLESTONESTREETANOLDMANWITHAGREYBEARDSATQUIETLYONASTONEBENCHNEARTHEENTRANCETOTHEMARKETPLACEHEWATCHEDTHEBUSLYCROWDWITHWISEYESANDSMILEDSOFTLY";

//        String plaintext1000 = "THESUNHADBEENRISINGOVERTHEOLDCITYWALLSFORCENTURIESANDTHEPEOPLEWHOLIVEDTHEREHADGROWNUSEDTOTHESIGHTOFITSGOLDENRAYSFALLINGONTHEANCIENTBUILDINGSANDSTONEPAVEDSQUARESTHEMARKETPLACEWASALWAYSTHEFIRSTPLACETOCOMEALIVEEACHMORNINGWITHVENDORSSETTINGUPTHEIRSTALLSANDSHOUTINGOUTTHEIRPRICESTOPASSERSBYOLDWOMENWITHWORNHANDSSOLDBASKETSOFEGGSBREADANDFRESHVEGETABLESWHILEFISHMENSMELLINGOFTHESEAVOIFEROUSLYADVERTISEDTHEIRDAILYCATCHYOUNGBOYSDARTEDTHROUGHTHECROWDSDELIVERINGMESSAGESANDSTEALINGSMALLFRUITSWHENNOBODYWATCHEDTHEBLACKSMITHINTHEFARCORNERHADBEENWORKINGSINCEBEFOREDAWNHISHAMMERRINGOUTALOUDRHYTHMICCLANGINGTHATSOUNDEDACROSSTHESQUAREWOMENBARTEREDNOISILYWITHMERCHANTSFORNEWFABRICSANDSPICESIMPORTEDFROMDISTANTLANDSACROSSTHESEAANDOLDERMENSMOKINGPIPESSATONBENCHESDISCUSSINGNEWSOFUPCOMINGHARVESTSANDPOLITICALDISPUTESFROMNEIGHBORINGVILLAGESTHECHILDRENRANPASTLAUGHINGANDSHOUTINGATEVERYTHINGANDNOTHINGWITHOUTACAREINTHEWORLDTHEAIRWASTHICKWITHSOUNDSANDSMELLSOFLIFEANDCOMMERCE";
//        String plaintext1760 = "THESUNHADBEENRISINGOVERTHEOLDCITYWALLSFORCENTURIESANDTHEPEOPLEWHOLIVEDTHEREHADGROWNUSEDTOTHESIGHTOFITSGOLDENRAYSFALLINGONTHEANCIENTBUILDINGSANDSTONEPAVEDSQUARESTHEMARKETPLACEWASALWAYSTHEFIRSTPLACETOCOMEALIVEEACHMORNINGWITHVENDORSSETTINGUPTHEIRSTALLSANDSHOUTINGOUTTHEIRPRICESTOPASSERSBYOLDWOMENWITHWORNHANDSSOLDBASKETSOFEGGSBREADANDFRESHVEGETABLESWHILEFISHMENSMELLINGOFTHESEAVOIFEROUSLYADVERTISEDTHEIRDAILYCATCHYOUNGBOYSDARTEDTHROUGHTHECROWDSDELIVERINGMESSAGESANDSTEALINGSMALLFRUITSWHENNOBODYWATCHEDTHEBLACKSMITHINTHEFARCORNERHADBEENWORKINGSINCEBEFOREDAWNHISHAMMERRINGOUTALOUDRHYTHMICCLANGINGTHATSOUNDEDACROSSTHESQUAREWOMENBARTEREDNOISILYWITHMERCHANTSFORnewfabricsandspicesimportedfromdistantlandsacrosstheseaandoldermensmokingpipessatonbenchesdiscussingnewsofupcomingharvestsandpoliticaldisputesfromneighboringvillagesthechildrenranpastlaughingandshoutingateverythingandnothingwithoutacareintheworldtheairwasthickwithsoundsandsmellsoflifeandcommercethefountainatthecenterofthesquarebabbledsoftlyandpigeonsgatheredarounditspedestallookingforanythingdecenttoeatthecathedralbelltowerstoodtalloverthetilerooftopsandeveryhouritsdeepbronzebellsoundedoutacrosstherooftopsofthewholedowntownneighborhoodcausingpigeonsbattersleepyresidentstowakeupandcursetheirfatethelibraryanexoldbuildingthatsmelledlikemoldypaperwasopenedonlyonwednesdaysandwhenithappenedseveralyoungerscribessatinsidecopyingDOCUMENTSBYLIGHTTHATCAMEINFRIGHTFULLYSLOWTHROUGHSTONEARCHWAYSTHEYCOPIEDTREATIESANDBORINGSERMONSWHILETHEIRDREAMSWEREFULLOFADVENTUREANDTRAVELINGUNKNOWNROADSTHROUGHWILDLANDSSTILLUNMAPPEDBYANYCARTOGRAPHEROVERALLOFTHISSCENETHEMAYORINHISHIGHWINDOWWATCHEDTHECOMINGSANDGOINGSOFHISPEOPLEHEHADDONESOFORDECADESANDWASCONVINCEDTHATKNOWINGTHEIRCUSTOMSANDRHYTHMSWASWHATHADKEPTHIMINFAVORSOLONG".toUpperCase();

//        List<Integer>[] key = generateHomophonicKey(plaintext, 5);
//        int[] cipherText = encrypt(plaintext, key);
//
//
//        System.out.println("Ciphertext:");
//        for (int i =0; i< cipherText.length; i++){
//            System.out.print(cipherText[i] + " ");
//        }
    }

    public static List<Integer>[] generateHomophonicKey(String plaintext, Integer value, StringBuilder log) {
        int[] homophoneCounts = new int[26];

        // base: every letter gets 1
        for (int i = 0; i < 26; i++) homophoneCounts[i] = 1;

        homophoneCountBasedOnFixedNumber(homophoneCounts,value);

//        System.out.println("Plaintext legth: " + plaintext.length());
//        int total = Arrays.stream(homophoneCounts).sum();
//        System.out.println("Total homophones: " + total);
//
//        System.out.println("Number of symbols: " +
//                Arrays.stream(homophoneCounts).sum());
//
//
//        List<Integer>[] key= generateKey(homophoneCounts);
//        for (int i = 0; i< key.length; i++){
//            System.out.println( (char)('A' + i)+ "(" + i + ") " + key[i]);
//        }
        log.append("Plaintext length: ").append(plaintext.length()).append("\n");
        log.append("Total homophones: ").append(Arrays.stream(homophoneCounts).sum()).append("\n\n");
        log.append("--- HOMOPHONE KEY DISTRIBUTION ---\n");

        List<Integer>[] key = generateKey(homophoneCounts);
        for (int i = 0; i < key.length; i++) {
            log.append((char)('A' + i)).append("(").append(i).append(") ").append(key[i]).append("\n");
        }

        return key;
    }

    public static List<Integer>[] generateHomophonicKey(String plaintext, Double value, StringBuilder log) {
        int[] homophoneCounts = new int[26];

        // base: every letter gets 1
        for (int i = 0; i < 26; i++) homophoneCounts[i] = 1;

        homophoneCountBasedOnFreq(homophoneCounts, plaintext, value);

//        System.out.println("Plaintext legth: " + plaintext.length());
//        int total = Arrays.stream(homophoneCounts).sum();
//        System.out.println("Total homophones: " + total);
//
//        System.out.println("Number of symbols: " +
//                Arrays.stream(homophoneCounts).sum());
//
//
//        List<Integer>[] key= generateKey(homophoneCounts);
//        for (int i = 0; i< key.length; i++){
//            System.out.println( (char)('A' + i)+ "(" + i + ") " + key[i]);
//        }
        homophoneCountBasedOnFreq(homophoneCounts, plaintext, value);

        log.append("Plaintext length: ").append(plaintext.length()).append("\n");
        log.append("Total homophones: ").append(Arrays.stream(homophoneCounts).sum()).append("\n\n");
        log.append("--- HOMOPHONE KEY DISTRIBUTION ---\n");

        List<Integer>[] key = generateKey(homophoneCounts);
        //TODO this is mine
        for (int i = 0; i < key.length; i++) {
            log.append((char)('A' + i)).append("(").append(i).append(") ").append(key[i]).append("\n");
        }

        //this is for laco
//        for (int i = 0; i < key.length; i++) {
//            String formattedSymbols = key[i].stream()
//                    .map(n -> String.format("%02d", n))
//                    .collect(Collectors.joining(", ", "[", "]"));
//            log.append((char)('A' + i)).append("(").append(i).append(") ").append(formattedSymbols).append("\n");
//        }
        return key;
    }


    private static void homophoneCountBasedOnFreq(int[] homophoneCounts, String plaintext, Double value) {
        for (int i = 0; i<26; i++){
            homophoneCounts[i]= (int) Math.ceil(HomophonicAnnealingSolver.ENGLISH_FREQ[i] * 100 * value);
        }
    }

    private static void homophoneCountBasedOnFixedNumber(int[] homophoneCounts, Integer value) {
        for (int i = 0; i<26; i++){
            homophoneCounts[i]= value;
        }
    }


    // TODO auto generate number of homophones : |CT|*freq[i]*someConstant (0-1 range)
    // 1 func - fix (same number of homophones per letter, A = X = D = 5 e.g.)
    // 2 func - automatic by frequency
    // cyclicka struktura generovania kluca, pridat tiez do prace
}
