/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micheal_luong_bit_parity_check;

import java.util.Scanner;

/**
 *
 * @author micheal
 */
public class Micheal_Luong_Bit_Parity_Check {

    /**
     * @param args the command line arguments
     */
    
    public static int[][] Name;
    public static String[] checking;
    public static int[][] results;
    public static int[][] ParityWeights;
    public static int[] ErrorIndexes;

    public static void main(String[] args) {
        // TODO code application logic here

        Scanner scan = new Scanner(System.in);
        String answer;
        System.out.println("What is your first name?");
        answer = scan.nextLine();
        Response(answer);
        results = HammingCodeConversion(Name);

        System.out.println("Letters      Hamming Code");

        for (int i = 0; i < results.length; i++) {
            System.out.print("   " + answer.substring(i, i + 1) + "         ");
            for (int j = 0; j < 11; j++) {
                if (j == 0 || j == 1 || j == 3 || j == 7) {
                    System.out.print(Color.ANSI_GREEN + results[i][j] + Color.ANSI_RESET);
                } else {
                    System.out.print(results[i][j]);
                }
            }
            System.out.println("");
        }

        checking = new String[Name.length];

        /*Recording user's copy*/
        System.out.println("Type in the code that you see above.");
        for (int i = 0; i < results.length; i++) {
            String user;
            user = scan.nextLine();

            checking[i] = user;
            if (i != results.length - 1) {
                System.out.println("Type in the next line...");
            }
        }

        System.out.println("\n\n\n\n");
        if (Checking(checking) == true) {
            System.out.println("\n" + "That's correct");
        } else {
            System.out.println("You file has been corrupted!");
            System.out.println("Here is the fixed version");

            for (int i = 0; i < results.length; i++) {
                System.out.print("   " + answer.substring(i, i + 1) + "         ");
                for (int j = 0; j < 11; j++) {
                    if (j == 0 || j == 1 || j == 3 || j == 7) {
                        System.out.print(Color.ANSI_GREEN + results[i][j] + Color.ANSI_RESET);
                    } else if (ErrorIndexes[i] != 0 && j + 1 == ErrorIndexes[i]) {
                        System.out.print(Color.ANSI_YELLOW + results[i][j] + Color.ANSI_RESET);
                    } else {
                        System.out.print(results[i][j]);
                    }
                }
                System.out.println("");
            }
        }
    }

    static void Response(String response) {

        Name = new int[response.length()][7];

        for (int i = 0; i < response.length(); i++) {
            char letter = response.charAt(i);
            String asciiValue = Integer.toBinaryString(letter);
            for (int j = 0; j < asciiValue.length(); j++) {
                int separate = Character.digit(asciiValue.charAt(j), 10);
                Name[i][j] = separate;
            }
        }

        System.out.println("\n\n\n\n");
    }

    static int[][] HammingCodeConversion(int[][] original) {
        int[][] clone = new int[original.length][11];//might have to change to 12 

        for (int i = 0; i < original.length; i++) {

            /*fills in the parity bit checks*/
            clone[i][0] = Parity1_2(original[i][0], original[i][1], original[i][3], original[i][4], original[i][6]);
            clone[i][1] = Parity1_2(original[i][0], original[i][2], original[i][3], original[i][5], original[i][6]);
            clone[i][3] = Parity3_4(original[i][1], original[i][2], original[i][3]);
            clone[i][7] = Parity3_4(original[i][4], original[i][5], original[i][6]);

            /*fills in the 7 bits*/
            clone[i][2] = original[i][0];
            clone[i][4] = original[i][1];
            clone[i][5] = original[i][2];
            clone[i][6] = original[i][3];
            clone[i][8] = original[i][4];
            clone[i][9] = original[i][5];
            clone[i][10] = original[i][6];
        }

        return clone;
    }

    static int Parity1_2(int one, int two, int three, int four, int five) {
        int temp = (one + two + three + four + five) % 2;

        if (temp == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    static int Parity3_4(int one, int two, int three) {
        int temp = (one + two + three) % 2;

        if (temp == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    static boolean Checking(String[] compare) {
        ParityWeights = new int[compare.length][4];//initialized
        ErrorIndexes = new int[compare.length];
        //ArrayList<Error> Errors = new ArrayList<Error>();
        boolean valid = true;
        int[][] digits = new int[compare.length][11];//stores 7 bits
        for (int i = 0; i < compare.length; i++) {
            String temp = String.valueOf(compare[i]);
            for (int j = 0; j < temp.length(); j++) {
                int digit = Character.digit(temp.charAt(j), 10);
                digits[i][j] = digit;
                System.out.print(digits[i][j]);
            }
            System.out.println("");
        }

        /*The comparison*/
        for (int i = 0; i < digits.length; i++) {
            if ((digits[i][2] + digits[i][4] + digits[i][6] + digits[i][8] + digits[i][10]) % 2 != results[i][0]) {
                valid = false;
                //Errors.add(new Error(i));
                ParityWeights[i][0] = 1;
            }

            if ((digits[i][2] + digits[i][5] + digits[i][6] + digits[i][9] + digits[i][10]) % 2 != results[i][1]) {
                valid = false;
                //Errors.add(new Error(i));
                ParityWeights[i][1] = 2;
            }
            if ((digits[i][4] + digits[i][5] + digits[i][6]) % 2 != results[i][3]) {
                valid = false;
                //Errors.add(new Error(i));
                ParityWeights[i][2] = 4;
            }
            if ((digits[i][8] + digits[i][9] + digits[i][10]) % 2 != results[i][7]) {
                valid = false;
                //Errors.add(new Error(i));
                ParityWeights[i][3] = 8;
            }
        }

        /*stores bad bits*/
        int ErrorCounterIndex = 0;
        if (!valid) {

            System.out.println("There's a bad bit...");
            for (int i = 0; i < digits.length; i++) {
                int errorBit = ParityWeights[i][0] + ParityWeights[i][1] + ParityWeights[i][2] + ParityWeights[i][3];
                ErrorIndexes[ErrorCounterIndex] = errorBit;
                ErrorCounterIndex++;
            }

            int counter = 1;
            boolean finish = false;
            for (int i = 0; i < digits.length; i++) {
                counter = 1;
                finish = false;
                for (int j = 0; j < 11; j++) {
                    if (ErrorIndexes[i] != 0 && counter == ErrorIndexes[i] && !finish) {
                        System.out.print(Color.ANSI_RED + digits[i][j] + Color.ANSI_RESET);
                        finish = true;
                    } else {
                        System.out.print(digits[i][j]);
                        counter++;
                    }
                }
                System.out.println("");
            }
        }
        return valid;
    }
}
