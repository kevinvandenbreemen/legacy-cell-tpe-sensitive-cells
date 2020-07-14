package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.CellTypes;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveCell;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveSimulation;
import com.vandenbreemen.corpuscles.corpuscles.visualizer.CellTypeSensitiveRenderer;
import com.vandenbreemen.corpuscles.visualizer.CorpusclesVisualizer;

public class SimpleEvolverExample {

    public static void main(String[] args) {
        CellTypesMutator mutator = new CellTypesMutator(0.05);

        CellTypesProcreator procreator = new CellTypesProcreator();
        int height = 3;
        int width = 3;

        //  Define a tester
        int[] expectedActivations = { 0,2, 2,2};
        int[] inputs = {0,1};
        CellTypesTester tester = new CellTypesTester();

        CellTypeSensitiveEvolver evolver = new CellTypeSensitiveEvolver(mutator, new CellTypesBuilder(), height, width) {
            @Override
            protected double testSolution(CorpusclesData cellTypes) {

                //  Check for pulsers as outputs
                double nonEndpointCost = 1.0;
                for(int i=0; i<expectedActivations.length; i+=2) {
                    if(cellTypes.bitIsOn(expectedActivations[i], expectedActivations[i+1], CellTypes.COUPLER) &&
                            cellTypes.bitIsOn(expectedActivations[i], expectedActivations[i+1], CellTypes.COUPLER_ENDPOINT)
                    ) {
                        return 1.0;
                    } else if(!cellTypes.bitIsOn(expectedActivations[i], expectedActivations[i+1], CellTypes.COUPLER_ENDPOINT)) {
                        nonEndpointCost *= 0.8;
                    }
                }

                double defaultActivationCost = tester.testSolution(cellTypes, 3, new int[0], new int[0], expectedActivations);

                return defaultActivationCost + tester.testSolution(cellTypes, 3, inputs, expectedActivations) / nonEndpointCost;
            }
        };
        evolver.prepareSolutions(); //  Generate genepool
        evolver.testSolutions();

        for(int i=0; i<500; i++) {
            //  Remove the last 3
            evolver.cellTypesSets.remove(7);
            evolver.cellTypesSets.remove(7);
            evolver.cellTypesSets.remove(7);

            evolver.cellTypesSets.add(procreator.getNextSolution(evolver.cellTypesSets, mutator));
            evolver.cellTypesSets.add(procreator.getNextSolution(evolver.cellTypesSets, mutator));
            evolver.cellTypesSets.add(procreator.getNextSolution(evolver.cellTypesSets, mutator));

            evolver.testSolutions();
        }

        CorpusclesData optimalSolution = evolver.bestSolution();
        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(new CorpusclesData(height,width), optimalSolution);
        CellTypeSensitiveRenderer renderer = new CellTypeSensitiveRenderer(new CellTypeSensitiveCell(simulation));

        simulation.activate(inputs[0], inputs[1]);
        simulation.nextEpoch();

        new CorpusclesVisualizer(simulation, new CellTypeSensitiveAutomaton(simulation), renderer);

    }

}
