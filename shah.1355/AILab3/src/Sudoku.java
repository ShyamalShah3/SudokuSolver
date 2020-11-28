/*Sudoku puzzle representation. Stores the initial State as well as the Current State*/
public class Sudoku {

    /* Initial State of the Puzzle */
    private int[][] initialState = new int[4][4];

    /* Current State of the Puzzle */
    private int[][] currentState = new int[4][4];

    /* Size of Sudoku Puzzle */
    private final int size = 4;

    /* Size of Mini-Matrix */
    private final int miniSize = 2;

    /*
     * Constructor that sets initial state and current state to the values
     * provided in initial Matrix
     */
    public Sudoku(int[][] initial) {
        int i = 0;
        int j = 0;
        /* Outer loop for copying initial */
        while (i < this.size) {
            j = 0;
            /* Inner loop to account for the different rows and columns */
            while (j < this.size) {
                this.initialState[i][j] = initial[i][j];
                this.currentState[i][j] = initial[i][j];
                j++;
            }
            i++;
        }
    }

    /**
     * printing the Solution State to the Console
     */
    public void printSolution() {
        int i = 0;
        int j = 0;
        while (i < this.size) {
            j = 0;
            while (j < this.size) {
                System.out.print(this.currentState[i][j]);
                System.out.print("|");
                j++;
            }
            System.out.println("\n--------");
            i++;
        }
    }

    /*
     * Used to copy a given Matrix. Mainly used so that object references do not
     * get confused.
     */
    public int[][] copy(int[][] state) {
        /* Matrix to be returned */
        int[][] result = new int[this.size][this.size];

        /* copying matrix */
        int i = 0;
        int j = 0;
        /* Outer loop for copying state */
        while (i < this.size) {
            j = 0;
            /* Inner loop to account for the different rows and columns */
            while (j < this.size) {
                result[i][j] = state[i][j];
                j++;
            }
            i++;
        }
        return result;
    }

    /**
     * Used to update the current State
     */
    public void updateCurrentState(int[][] state) {
        /* copying matrix */
        int i = 0;
        int j = 0;
        /* Outer loop for copying state */
        while (i < this.size) {
            j = 0;
            /* Inner loop to account for the different rows and columns */
            while (j < this.size) {
                this.currentState[i][j] = state[i][j];
                j++;
            }
            i++;
        }
    }

    /* Returns a Copy of the currentState */
    public int[][] currentState() {
        return this.copy(this.currentState);
    }

    /* Returns a Copy of the initialState */
    public int[][] initialState() {
        return this.copy(this.initialState);
    }

    /*
     * Heuristic to check the correctness of currentSolution. Returns 0 if the
     * solution is valid. Otherwise, returns the number of errors. First checks
     * to make sure that the currentState has the same values in the non-zero
     * positions as the inital state. Makes sure that current State has no
     * '0'values. makes sure that all the row values are different. Makes sure
     * all the column values are different. Makes sure all the individual 2x2
     * squares are also different.
     */
    public int evaluationFunction() {
        int heuristicValue = 0;
        int i = 0;
        int j = 0;

        /*
         * Verify that the non-zero initial values are the same in the current
         * state
         */
        while (i < this.size) {
            j = 0;
            /* Inner loop to account for the different rows and columns */
            while (j < this.size) {
                /* Checking equality of non-zero initial state values */
                if (this.initialState[i][j] != 0
                        && this.initialState[i][j] != this.currentState[i][j]) {
                    heuristicValue++;
                }
                j++;
            }
            i++;
        }

        /**
         * Make sure current state has only non-zero values
         */
        i = 0;
        j = 0;
        while (i < this.size) {
            j = 0;
            /* Inner loop to account for the different rows and columns */
            while (j < this.size) {
                /* Checking equality of non-zero initial state values */
                if (this.currentState[i][j] == 0) {
                    heuristicValue++;
                }
                j++;
            }
            i++;
        }

        /*
         * Make sure all the rows in the matrix have distinct values that are in
         * the range [1,4]
         */
        i = 0;
        j = 0;
        int matchingNumbers = 0;
        while (i < this.size) { //iterating column
            j = 0;
            while (j < this.size) { //iterating row
                /*
                 * have to start k at 0 because I couldn't find a way to iterate
                 * without checking the current value as well
                 */
                int k = 0;
                while (k < this.size) { //checking within given row
                    if (this.currentState[i][j] == this.currentState[i][k]) {
                        /*
                         * This value should be =1 if all the values in given
                         * row are distinct
                         */
                        matchingNumbers++;
                    }
                    k++;
                }

                if (matchingNumbers > 1) {
                    //accounting for the comparison between same values
                    heuristicValue += (matchingNumbers - 1);
                }
                matchingNumbers = 0; //resetting value
                j++;
            }
            i++;
        }

        /*
         * Make sure all the columns in the matrix have distinct values that are
         * in the range [1,4]
         */
        i = 0;
        j = 0;
        matchingNumbers = 0;
        while (j < this.size) { //iterating row
            i = 0;
            while (i < this.size) { //iterating column
                /*
                 * have to start k at 0 because I couldn't find a way to iterate
                 * without checking the current value as well
                 */
                int k = 0;
                while (k < this.size) { //checking within given row
                    if (this.currentState[i][j] == this.currentState[k][j]) {
                        /*
                         * This value should be =1 if all the values in given
                         * column are distinct
                         */
                        matchingNumbers++;
                    }
                    k++;
                }

                if (matchingNumbers > 1) {
                    //accounting for the comparison between same values
                    heuristicValue += (matchingNumbers - 1);
                }
                matchingNumbers = 0; //resetting value
                i++;
            }
            j++;
        }

        /* Checking to see if each individual 2x2 square has distinct values */

        /* Mini-Matrixes to store values of each 2x2 square */
        int[][] tmpOne = new int[2][2];
        int[][] tmpTwo = new int[2][2];
        int[][] tmpThree = new int[2][2];
        int[][] tmpFour = new int[2][2];

        //Split up CurrentState into 4 2x2 matrices
        tmpOne[0][0] = this.currentState[0][0];
        tmpOne[0][1] = this.currentState[0][1];
        tmpTwo[0][0] = this.currentState[0][2];
        tmpTwo[0][1] = this.currentState[0][3];
        tmpOne[1][0] = this.currentState[1][0];
        tmpOne[1][1] = this.currentState[1][1];
        tmpTwo[1][0] = this.currentState[1][2];
        tmpTwo[1][1] = this.currentState[1][3];
        tmpThree[0][0] = this.currentState[2][0];
        tmpThree[0][1] = this.currentState[2][1];
        tmpFour[0][0] = this.currentState[2][2];
        tmpFour[0][1] = this.currentState[2][3];
        tmpThree[1][0] = this.currentState[3][0];
        tmpThree[1][1] = this.currentState[3][1];
        tmpFour[1][0] = this.currentState[3][2];
        tmpFour[1][1] = this.currentState[3][3];

        /* Iterating to find out how many similarities */
        int matchOne = 0;
        int matchTwo = 0;
        int matchThree = 0;
        int matchFour = 0;

        i = 0;
        j = 0;
        /* Iterating Through Matrices */
        while (i < this.miniSize) {
            j = 0;
            while (j < this.miniSize) {
                int k = 0;
                int l = 0;

                /* Iterating within Matrices */
                while (k < this.miniSize) {
                    l = 0;
                    while (l < this.miniSize) {
                        /*
                         * Similar logic to whole matrix check. Just generalized
                         * to 2x2 matrices
                         */
                        if (tmpOne[i][j] == tmpOne[k][l] && i != k && j != l) {
                            matchOne++;
                        }

                        if (tmpTwo[i][j] == tmpTwo[k][l] && i != k && j != l) {
                            matchTwo++;
                        }

                        if (tmpThree[i][j] == tmpThree[k][l] && i != k
                                && j != l) {
                            matchThree++;
                        }

                        if (tmpFour[i][j] == tmpFour[k][l] && i != k
                                && j != l) {
                            matchFour++;
                        }
                        l++;
                    }
                    k++;
                }

                if (matchOne > 1) {
                    heuristicValue += matchOne;
                }

                if (matchTwo > 1) {
                    heuristicValue += matchTwo;
                }

                if (matchThree > 1) {
                    heuristicValue += matchThree;
                }

                if (matchFour > 1) {
                    heuristicValue += matchFour;
                }

                matchOne = 0;
                matchTwo = 0;
                matchThree = 0;
                matchFour = 0;
                j++;
            }
            i++;
        }

        return heuristicValue;
    }
}
