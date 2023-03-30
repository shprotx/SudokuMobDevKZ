package ru.shprot.sudokumobdevkz.model.game.generator.Solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.shprot.sudokumobdevkz.model.game.generator.Generator;


public class DancingLinks {

    private final ColumnNode header;
    private List<DancingNode> answer;
    public static int solutionsCount;

    private void search(int k) {
        if (solutionsCount == 2) return;
        if (header.R == header) {
            solutionsCount++;
            Generator.isUnicSolution = solutionsCount == 1;
        } else {
            ColumnNode c = selectColumnNodeHeuristic();
            c.cover();

            for (DancingNode r = c.D; r != c; r = r.D) {
                answer.add(r);

                for (DancingNode j = r.R; j != r; j = j.R) {
                    j.C.cover();
                }

                search(k + 1);

                r = answer.remove(answer.size() - 1);
                c = r.C;

                for (DancingNode j = r.L; j != r; j = j.L) {
                    j.C.uncover();
                }
            }
            c.uncover();
        }
    }

    private ColumnNode selectColumnNodeHeuristic() {
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;
        for (ColumnNode c = (ColumnNode) header.R; c != header; c = (ColumnNode) c.R) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    private ColumnNode makeDLXBoard(boolean[][] grid) {
        final int COLS = grid[0].length;

        ColumnNode headerNode = new ColumnNode("header");
        List<ColumnNode> columnNodes = new ArrayList<>();

        for (int i = 0; i < COLS; i++) {
            ColumnNode n = new ColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.hookRight(n);
        }
        headerNode = headerNode.R.C;

        for (boolean[] aGrid : grid) {
            DancingNode prev = null;
            for (int j = 0; j < COLS; j++) {
                if (aGrid[j]) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);
                    if (prev == null)
                        prev = newNode;
                    col.U.hookDown(newNode);
                    prev = prev.hookRight(newNode);
                    col.size++;
                }
            }
        }

        headerNode.size = COLS;

        return headerNode;
    }

    DancingLinks(boolean[][] cover) {
        header = makeDLXBoard(cover);
        solutionsCount = 0;
    }

    public void runSolver() {
        answer = new LinkedList<>();
        search(0);
    }

}