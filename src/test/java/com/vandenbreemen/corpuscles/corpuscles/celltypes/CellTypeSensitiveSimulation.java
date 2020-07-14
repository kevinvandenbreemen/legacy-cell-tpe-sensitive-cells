package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;

public class CellTypeSensitiveSimulation extends Simulation {

    /**
     * Cell types data
     */
    public final Simulation cellTypes;

    public CellTypeSensitiveSimulation(CorpusclesData data, CorpusclesData cellTypesData) {
        super(data);
        this.cellTypes = new Simulation(cellTypesData);
    }

    @Override
    public void nextEpoch() {
        super.nextEpoch();
        cellTypes.nextEpoch();
    }
}
