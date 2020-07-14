package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CellularAutomaton;
import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveCell;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveSimulation;

/**
 * Cellular automaton that utilizes only {@link CellTypeSensitiveCell}s
 */
public class CellTypeSensitiveAutomaton extends CellularAutomaton {

    private CellTypeSensitiveCell theCell;

    public CellTypeSensitiveAutomaton(Simulation simulation) {
        super(simulation);
        if(!(simulation instanceof CellTypeSensitiveSimulation)) {
            throw new RuntimeException("Cannot build this automaton without a " + CellTypeSensitiveSimulation.class.getSimpleName());
        }

        theCell = new CellTypeSensitiveCell(simulation);
    }

    @Override
    protected Corpuscle getCorpuscle(int alongWidth, int alongHeight, Simulation simulation) {
        return theCell;
    }
}
