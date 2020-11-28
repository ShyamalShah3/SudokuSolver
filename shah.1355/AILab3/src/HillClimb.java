import java.util.Random;

public class HillClimb {
    private int[][] initialState;
    private int[][] currentState = new int[4][4];
    private int[][] tmpState = new int[4][4];
    private int[][] solution;
    private int[] randomNumbers = new int[4];
    private Sudoku puzzle;
    private int errors;
    private int arraySize = 4;
    private int count = 0;

    /**
     * constructor: does nothing
     */
    public HillClimb() {

    }

    public void generateRandomNumbers() {
        Random generator = new Random();
        int tmp = 0;

        /* Populating array to start */
        while (tmp < this.arraySize) {
            this.randomNumbers[tmp] = 0;
            tmp++;
        }

        /* Randomly putting numbers 1-4 in the array */
        int i = 0;
        while (i < this.arraySize) {
            /*
             * generating a random number in the bounds [1,4] and entering into
             * array once it is unique
             */
            do {
                tmp = generator.nextInt(this.arraySize) + 1;
            } while (this.arrayCheck(tmp, this.randomNumbers));

            this.randomNumbers[i] = tmp;
            i++;
        }
    }

    /**
     * Returns true if number exists in Array. False Otherwise.
     *
     * @param number
     * @param array
     * @return true if number exists in Array. False Otherwise
     */
    public boolean arrayCheck(int number, int[] array) {
        boolean check = false;
        int i = 0;
        while (!check && i < this.arraySize) {
            if (array[i] == number) {
                check = true;
            }
            i++;
        }

        return check;
    }

    /**
     * Sets initial state from Sudoku initial State
     *
     * @param state
     */
    public void setInitialState(int[][] state) {
        this.initialState = this.puzzle.initialState();
    }

    /**
     * Populating the matrix randomly
     *
     * @param currentState
     */
    public void makeState(int[][] currentState) {
        int i = 0;
        int j = 0;
        int k = 0;

        /* Filling in all 0 values with randomly generated numbers */
        while (i < this.arraySize) {
            j = 0;
            while (j < this.arraySize) {
                k = 0;
                while (k < this.arraySize) {
                    /*
                     * Making sure value at position is 0 and that row doesn't
                     * contain same value
                     */
                    if (currentState[i][j] == 0
                            && !this.arrayCheck(this.randomNumbers[k],
                                    currentState[i])) {
                        currentState[i][j] = this.randomNumbers[k];
                    }
                    k++;
                }
                j++;
            }
            i++;
        }
    }

    public int[][] solver(Sudoku puzzle) {
        /* initializing puzzle */
        this.puzzle = new Sudoku(puzzle.initialState());

        /* Setting initial state */
        this.setInitialState(this.puzzle.initialState());

        /* Copying Initial State Value into Current State */
        this.copy(this.initialState);

        /* Used to count the number of iterations of the algorithm */
        this.count = 0;

        this.generateRandomNumbers();

        do {
            this.makeState(this.currentState);

            this.solution = this.hillClimber(this.currentState);

            this.puzzle.updateCurrentState(this.solution);
        } while (this.puzzle.evaluationFunction() != 0);

        return this.solution;

    }

    public void copy(int[][] state) {

        /* copying matrix */
        int i = 0;
        int j = 0;
        /* Outer loop for copying state */
        while (i < this.arraySize) {
            j = 0;
            /* Inner loop to account for the different rows and columns */
            while (j < this.arraySize) {
                this.currentState[i][j] = state[i][j];
                j++;
            }
            i++;
        }
    }

    public int[][] hillClimber(int[][] nextState) {
        Random generator = new Random();

        int currentErrorCount = 0;
        int nextErrorCount = 0;

        //Get error value for current State
        this.puzzle.updateCurrentState(this.currentState);
        currentErrorCount = this.puzzle.evaluationFunction();

        System.out.println("Iteration: " + this.count);
        System.out.println("Evaluation Function: " + currentErrorCount);
        this.puzzle.printSolution();

        /* Exiting if current state is a solution */
        if (currentErrorCount == 0) {
            return this.currentState;
        }

        do {
            this.tmpState = this.puzzle.copy(this.currentState);
            /*
             * Pick random row from initial state and find two spots in the
             * initial state in the specific row where the initial state values
             * are '0' and swapping those values
             */
            int randomRow = generator.nextInt(this.arraySize);
            int[] randomColumns = new int[2];
            int tmpCount = 0;
            do {
                if (tmpCount == 500) {
                    randomRow = generator.nextInt(this.arraySize);
                    tmpCount = 0;
                }
                for (int i = 0; i < 2; i++) {
                    randomColumns[i] = generator.nextInt(this.arraySize);
                }
                tmpCount++;
            } while (this.initialState[randomRow][randomColumns[0]] != 0
                    && this.initialState[randomRow][randomColumns[1]] != 0);

            int tmp = this.tmpState[randomRow][randomColumns[0]];
            this.tmpState[randomRow][randomColumns[0]] = this.tmpState[randomRow][randomColumns[1]];
            this.tmpState[randomRow][randomColumns[1]] = tmp;

            this.puzzle.updateCurrentState(this.tmpState);
            nextErrorCount = this.puzzle.evaluationFunction();

            this.count++;
            if (nextErrorCount != 0 && (this.count >= 500)) {
                System.out.println("Restarting Search");
                this.count = 0;
                this.currentState = this.puzzle.copy(this.initialState);
                return this.puzzle.copy(this.initialState);
            } else {
                System.out.println("Iteration: " + this.count);
                System.out.println("Evaluation Function: " + nextErrorCount);
            }

        } while (nextErrorCount >= currentErrorCount);

        this.currentState = this.puzzle.copy(this.tmpState);

        return this.puzzle.copy(this.currentState);
    }
}
