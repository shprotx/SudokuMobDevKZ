package ru.shprot.sudokumobdevkz.model.game.generator;

import static android.content.ContentValues.TAG;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.NUMBER_OF_CELLS;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.SUBLIST_SIZE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.VISIBLE_SQUARES_EASY;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.VISIBLE_SQUARES_EXPERT;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.VISIBLE_SQUARES_MEDIUM;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.model.game.Square;
import ru.shprot.sudokumobdevkz.model.game.generator.Solver.DancingLinks;
import ru.shprot.sudokumobdevkz.model.game.generator.Solver.DancingLinksAlgorithm;

public class Generator {
    private final ArrayList<Square> squares = new ArrayList<>(NUMBER_OF_CELLS);
    private final ArrayList<Integer>[] available = new ArrayList[NUMBER_OF_CELLS];
    private final int[][] grid = new int[SUBLIST_SIZE][SUBLIST_SIZE];
    public static boolean isUnicSolution = true;


    public ArrayList<Square> generate(int diff) {
        initBasicThings();
        generateGrid();
        parseGridArray();
        dig(diff);
        makeSquaresInvisible();
        return squares;
    }

    private void makeSquaresInvisible() {
        int iterator = 0;
        for (int[] ints : grid)
            for (int anInt : ints) {
                squares.get(iterator).setVisible(anInt != 0);
                iterator++;
            }
    }

    /**
     * This method hides items on the grid with the help of the solver (Dancing Links Algorithm).
     * @param diff indicates how many cells will be visible at the end of the executing.
     *             Number 3 means that difficulty is hard. Firstly, method removes maximum number of
     *             elements, and when randomly add back some cells if difficulty is less than 3.
     */
    private void dig(int diff) {
        int iterator = 0;
        int strategy = (int)(Math.random() * 8);
        int x, y;
        int emptyCellsCounter = 0;
        ArrayList<Integer> zeros = new ArrayList<>();
        while (iterator < NUMBER_OF_CELLS) {
            x = calcX(iterator, strategy);
            y = calcY(iterator, strategy);
            if (iterator % 4 == 0) {
                x = 8 - x;
                y = 8 - y;
            }
            int temp = grid[y][x];
            grid[y][x] = 0;
            DancingLinks.solutionsCount = 0;
            DancingLinksAlgorithm.tryToSolve(grid);
            if (!isUnicSolution)
                grid[y][x] = temp;
            else {
                emptyCellsCounter++;
                if (diff < 3) zeros.add(iterator);
            }
            iterator++;
        }
        if (diff < 3) {
            int position;
            Collections.shuffle(zeros);
            int haveToOpen = calcDifficulty(diff) - (NUMBER_OF_CELLS - emptyCellsCounter);
            while (haveToOpen > 0) {
                position = zeros.get(zeros.size() - 1);
                x = calcX(position,strategy);
                y = calcY(position,strategy);
                if (grid[y][x] == 0)
                    grid[y][x] = squares.get(position).getValue();
                else {
                    zeros.remove(zeros.size() - 1);
                    continue;
                }
                zeros.remove(zeros.size() - 1);
                haveToOpen--;
                emptyCellsCounter--;
            }
        }
    }

    private void parseGridArray() {
        int iterator = 0;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = squares.get(iterator).getValue();
                iterator++;
            }
    }

    private void generateGrid() {
        int count = 0;
        while(count < NUMBER_OF_CELLS) {
            if( !(available[count].size() == 0) ) {                 //if every number has been tried and failed then backtrack
                int randomIndex = getRan(available[count].size());
                int value = available[count].get(randomIndex);
                Square item = createItem(count, value);
                if (!conflicts(squares, item)) {                    // no issues
                    squares.add(count, item);
                    available[count].remove(randomIndex);
                    count++;
                } else                                              // conflict occurred
                    available[count].remove(randomIndex);
            } else {                                               //backtrack
                for (int sbuIndex = 1; sbuIndex <= 9; sbuIndex++)  //new possible values for certain cell
                    available[count].add(sbuIndex);
                squares.remove(count - 1);
                count--;
            }
        }
    }

    private void initBasicThings() {
        for(int index = 0; index < NUMBER_OF_CELLS; index++) {   // populating the matrix with all possible values
            available[index] = new ArrayList<>();
            for(int subIndex = 1; subIndex <= 9; subIndex++)
                available[index].add(subIndex);
        }
        squares.clear();
    }

    private static int calcDifficulty(int diff) {
        switch (diff) {
            case 1: return VISIBLE_SQUARES_EASY;
            case 2: return VISIBLE_SQUARES_MEDIUM;
            case 3: return VISIBLE_SQUARES_EXPERT;
            default: return diff;
        }
    }

    private static int calcX(int number, int strategy) {
        int result;
        if (number < SUBLIST_SIZE) result = number;
        else result = number % SUBLIST_SIZE;
        if (strategy == 0 || strategy == 2) return result;
        else if (strategy == 1 || strategy == 3) return 8 - result;
        else return result;
    }

    private static int calcY(int number, int strategy) {
        int result;
        if (number < SUBLIST_SIZE) result = 0;
        else result = number / SUBLIST_SIZE;
        if (strategy == 0 || strategy == 3) return result;
        else if (strategy == 1 || strategy == 2) return 8 - result;
        else return result;
    }

    private static int getRan(int upper) {
        return (int)(Math.random() * upper);
    }

    private static boolean conflicts(ArrayList<Square> currentSquare, Square test) {
        for (int i = 0; i < currentSquare.size(); i++) {
            Square s = currentSquare.get(i);
            if ( (s.getX() != 0 && s.getX() == test.getX())
                    | (s.getY() != 0 && s.getY() == test.getY())
                    | (s.getRegion() != 0 && s.getRegion() == test.getRegion()))
                if (s.getValue() == test.getValue()) return true;
        }
        return false;
    }

    private static Square createItem(int index, int value) {
        Square square = new Square();
        square.setPosition(index);
        index++;
        square.setX(getAcrossFromNumber(index));
        square.setY(getDownFromNumber(index));
        square.setRegion(getRegionFromNumber(index));
        square.setValue(value);
        square.setColor(R.color.black);
        square.setVisible(true);
        return square;
    }

    private static int getAcrossFromNumber(int index) {
        int n = index % SUBLIST_SIZE;
        if (n == 0) return SUBLIST_SIZE;
        else return n;
    }

    private static int getDownFromNumber(int index) {
        int n;
        if (getAcrossFromNumber(index) == SUBLIST_SIZE) n = index/SUBLIST_SIZE;
        else n = index/SUBLIST_SIZE + 1;
        return n;
    }

    private static int getRegionFromNumber(int index) {
        int region = 0;
        int x = getAcrossFromNumber(index);
        int y = getDownFromNumber(index);
        if ( (1 <= x && x <= 3) && (1 <= y && y <= 3) ) region = 1;
        else if ( (4 <= x && x <= 6) && (1 <= y && y <= 3) ) region = 2;
        else if ( (7 <= x && x <= 9) && (1 <= y && y <= 3) ) region = 3;
        else if ( (1 <= x && x <= 3) && (4 <= y && y <= 6) ) region = 4;
        else if ( (4 <= x && x <= 6) && (4 <= y && y <= 6) ) region = 5;
        else if ( (7 <= x && x <= 9) && (4 <= y && y <= 6) ) region = 6;
        else if ( (1 <= x && x <= 3) && (7 <= y && y <= 9) ) region = 7;
        else if ( (4 <= x && x <= 6) && (7 <= y && y <= 9) ) region = 8;
        else if ( (7 <= x && x <= 9) && (7 <= y && y <= 9) ) region = 9;
        return region;
    }
}
