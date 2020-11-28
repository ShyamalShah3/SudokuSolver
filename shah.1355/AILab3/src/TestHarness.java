import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestHarness {

    private int[][] currentState = new int[4][4];
    private int[][] solutionState;
    private int InputLineCheck = 6;

    /**
     *
     * @param args
     *            contains the path to the test file
     */
    public static void main(String[] args) throws FileNotFoundException {
        int[][] inputState = fileToMatrix(args[0]);

        Sudoku lol = new Sudoku(inputState);
        System.out.println("----Hill Climbing Algorithm----");
        System.out.println("Initial State: ");
        lol.printSolution();
        HillClimb algorithm = new HillClimb();
        int[][] solution = algorithm.solver(lol);
        lol.updateCurrentState(solution);
        System.out.println("Solution State: ");
        lol.printSolution();
    }

    /**
     *
     * @param file
     *            contains path to test file
     * @return int[][] representing initial state of the Sudoku Puzzle
     * @throws FileNotFoundException
     */
    private static int[][] fileToMatrix(String file)
            throws FileNotFoundException {
        /* Matrix that will be returned */
        int[][] result = new int[4][4];

        BufferedReader inputFile = null;

        /* Creating BufferedReader Object from given input file */
        try {
            inputFile = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("Error reading input File");
        }

        String text = "";
        int inputLineCheck = 6;
        int lineCheck = 4;

        /* Populating result Matrix with inputFile values */
        try {
            /* Iterating variables */
            int i = 0;
            int j = 0;

            /* Reading file line by line */
            while ((text = inputFile.readLine()) != null
                    && i < inputLineCheck) {
                j = 0;
                /* if-statement to disregard first line of file */
                if (i > 0) {
                    /* loop to check all 4 characters on a single line */
                    while (j < lineCheck) {
                        char tmp = text.charAt(j);
                        if (tmp == '*') {
                            result[i - 1][j] = 0;
                        } else {
                            result[i - 1][j] = Character.getNumericValue(tmp);
                        }
                        j++;
                    }
                }
                i++;
            }

        } catch (IOException e) {
            System.out.println("Error with inputs in the file");
        }

        /* Closing inputFile */
        try {
            inputFile.close();
        } catch (IOException e) {
            System.out.println("Error Closing Input File");
        }

        return result;
    }

}
