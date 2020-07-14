package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;

import java.util.ArrayList;
import java.util.List;

abstract public class CellTypeSensitiveEvolver {

    private int width;
    private int height;

    List<CorpusclesData> cellTypesSets;

    private CellTypesMutator mutator;
    private CellTypesBuilder builder;

    public CellTypeSensitiveEvolver(CellTypesMutator mutator, CellTypesBuilder builder, int height, int width) {
        this.mutator = mutator;
        this.builder = builder;
        this.height = height;
        this.width = width;
        this.cellTypesSets = new ArrayList<>();

    }

    /**
     * Prepares a batch of solutions
     */
    public void prepareSolutions() {
        for(int i=0; i<10; i++) {
            cellTypesSets.add(builder.getCellTypes(height, width));
        }
    }

    protected abstract double testSolution(CorpusclesData cellTypes);

    /**
     * Run through a single iteration of the {@link #testSolution(CorpusclesData) test function} for each solution in the generated
     * solutions
     */
    public void testSolutions() {
        cellTypesSets.sort((d1, d2)->{
            double cost1 = testSolution(d1);
            double cost2 = testSolution(d2);

            if(cost1 < cost2) {
                return -1;
            } else if(cost2 < cost1) {
                return 1;
            }

            return 0;
        });

        System.out.println("cost0: " + testSolution(cellTypesSets.get(0)));
        System.out.println("costLast: " + testSolution(cellTypesSets.get(cellTypesSets.size()-1)));
    }

    /**
     * Returns the best solution found after the latest run of {@link #testSolutions()}.
     * @return
     */
    public CorpusclesData bestSolution() {
        return cellTypesSets.get(0);
    }
}
