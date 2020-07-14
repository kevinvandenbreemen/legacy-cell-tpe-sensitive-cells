package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.automaton.CellTypeSensitiveAutomaton;
import com.vandenbreemen.corpuscles.corpuscles.automaton.CellTypesBuilder;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveCell;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveSimulation;
import com.vandenbreemen.corpuscles.corpuscles.visualizer.CellTypeSensitiveRenderer;

public class CellTypeSensitiveTest {

    public static void main(String[] args) {

        final int height = 20;
        final int width = 20;

        CellTypesBuilder builder = new CellTypesBuilder();
        CorpusclesData cellTypes = builder.getCellTypes(height,width);
        CorpusclesData cells = new CorpusclesData(height,width);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveAutomaton automaton = new CellTypeSensitiveAutomaton(simulation);

        simulation.activate(2,2);
        simulation.activate(4,4);
        simulation.activate(4,2);
        simulation.activate(0,4);
        simulation.activate(5,2);

        simulation.nextEpoch();

        CorpusclesVisualizer visualizer = new CorpusclesVisualizer(simulation, automaton, new CellTypeSensitiveRenderer(new CellTypeSensitiveCell(simulation)));

    }

}
