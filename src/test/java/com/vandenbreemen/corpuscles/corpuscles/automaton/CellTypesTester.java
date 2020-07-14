package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveSimulation;

public class CellTypesTester {

    /**
     * Test the given cell type arrangement with the given set of expected activations
     * @param cellTypes         Cell types (a la genome)
     * @param numIterations     Number of iterations after initial stimulations
     * @param inputs            Inputs to use for stimulating initial inputs
     * @param expectedActivations   Activations expected
     * @return                  Cost of solution as a value between 0 and 1.
     */
    public double testSolution(CorpusclesData cellTypes, int numIterations, int[] inputs, int[] expectedActivations) {

        return testSolution(cellTypes, numIterations, inputs, expectedActivations, null);

    }

    /**
     * Test the given cell type arrangement with the given set of expected activations
     * @param cellTypes         Cell types (a la genome)
     * @param numIterations     Number of iterations after initial stimulations
     * @param inputs            Inputs to use for stimulating initial inputs
     * @param expectedActivations   Activations expected
     * @param   expectedDeactivations   Cells that are expected to be deactivated
     * @return                  Cost of solution as a value between 0 and 1.
     */
    public double testSolution(CorpusclesData cellTypes, int numIterations, int[] inputs, int[] expectedActivations, int[] expectedDeactivations) {

        if(inputs == null || inputs.length % 2 != 0) {
            throw new RuntimeException("Inputs must be an array of ordered pairs of coordinates");
        }

        CorpusclesData data = new CorpusclesData(cellTypes.height(), cellTypes.width());

        //  Set the initial activations
        int alongHeight;
        int alongWidth;
        for(int i=0; i<inputs.length; i+=2) {
            alongHeight = inputs[i];
            alongWidth = inputs[i+1];
            data.activate(alongHeight, alongWidth);
        }

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(data, cellTypes);

        CellTypeSensitiveAutomaton automaton = new CellTypeSensitiveAutomaton(simulation);

        double cost = 0.0;
        for(int i=0; i<numIterations; i++) {
            automaton.performNextEpoch();
            cost += computeCost(data, expectedActivations, expectedDeactivations);
        }

        return cost / (double)numIterations;

    }

    public double computeCost(CorpusclesData data, int[] coordinatesOfCellsToBeActivated) {
        return computeCost(data, coordinatesOfCellsToBeActivated, null);
    }

    public double computeCost(CorpusclesData data, int[] coordinatesOfCellsToBeActivated, int[] coordinatesOfCellsToBeInactive) {
        if(coordinatesOfCellsToBeActivated == null || coordinatesOfCellsToBeActivated.length % 2 != 0) {
            throw new RuntimeException("coordinatesOfCellsToBeActivated:  Must be array of ordered coordinate pairs");
        }
        int numCellsToCheck = coordinatesOfCellsToBeActivated.length;

        int countWrong = 0;
        int alongHeight;
        int alongWidth;
        for(int i=0; i<coordinatesOfCellsToBeActivated.length; i+=2) {
            alongHeight = coordinatesOfCellsToBeActivated[i];
            alongWidth = coordinatesOfCellsToBeActivated[i+1];

            if(!data.activated(alongHeight, alongWidth)) {
                countWrong ++;
            }
        }

        if(coordinatesOfCellsToBeInactive != null) {
            if(coordinatesOfCellsToBeInactive.length % 2 != 0) {
                throw new RuntimeException("coordinatesOfCellsToBeInactive:  Must be an array of ordered coordinate pairs");
            }

            numCellsToCheck += coordinatesOfCellsToBeInactive.length;

            for(int i=0; i<coordinatesOfCellsToBeInactive.length; i+=2) {
                alongHeight = coordinatesOfCellsToBeInactive[i];
                alongWidth = coordinatesOfCellsToBeInactive[i+1];

                if(data.activated(alongHeight, alongWidth)) {
                    countWrong ++;
                }
            }
        }


        numCellsToCheck /= 2;

        return (double)countWrong / (double)numCellsToCheck;
    }
}
