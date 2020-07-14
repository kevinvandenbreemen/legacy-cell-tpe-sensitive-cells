package com.vandenbreemen.corpuscles.corpuscles;

import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.Simulation;

import static com.vandenbreemen.corpuscles.Simulation.NeighbourHoodRange.WIDTH_MAX;
import static com.vandenbreemen.corpuscles.Simulation.NeighbourHoodRange.WIDTH_MIN;

public class HorizontalAssociationCorpuscle extends Corpuscle {

    public HorizontalAssociationCorpuscle(Simulation simulation) {
        super(simulation);
    }

    @Override
    public void takeTurn(int alongHeight, int alongWidth) {
        byte data = simulation.data(alongHeight, alongWidth);

        int[] neighbourhood = simulation.getMooreNeighbourhoodRange(alongHeight, alongWidth);

        //  If I was previously set to act as a conduit and one (but not both) of the cells on either side of me are on then
        //  Activate the cell that isn't on
        if(simulation.bitIsOn(alongHeight, alongWidth, 1)) {
            if(simulation.activated(alongHeight, neighbourhood[WIDTH_MIN]) ^
                    simulation.activated(alongHeight, neighbourhood[WIDTH_MAX])) {
                simulation.activate(alongHeight, neighbourhood[WIDTH_MIN]);
                simulation.activate(alongHeight, neighbourhood[WIDTH_MAX]);
                simulation.activate(alongHeight, alongWidth);
            } else {    //  If both are on or neither are on then deactivate me
                simulation.deactivate(alongHeight, alongWidth);
            }
            return;
        }

        //  If the corpuscles on either side of me are on then I turn my connection bit on
        if(simulation.activated(alongHeight, neighbourhood[WIDTH_MIN]) &&
                simulation.activated(alongHeight, neighbourhood[WIDTH_MAX])){
            simulation.setBit(alongHeight, alongWidth, 1, true);
        }



    }
}
