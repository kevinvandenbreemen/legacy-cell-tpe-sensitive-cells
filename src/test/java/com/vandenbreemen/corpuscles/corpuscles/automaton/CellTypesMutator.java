package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;

import java.util.Random;

/**
 * Mutates an existing set of cell types
 */
public class CellTypesMutator {

    /**
     * Rate of mutation
     */
    private double mutationRate;

    /**
     * Define a new
     * @param mutationRate
     */
    public CellTypesMutator(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void mutate(CorpusclesData data) {

        Random random = new Random(System.currentTimeMillis());

        boolean isBitOn = false;
        for(int alongHeight = 0; alongHeight<data.height(); alongHeight++) {
            for(int alongWidth = 0; alongWidth<data.width(); alongWidth ++) {

                if(random.nextDouble() >= mutationRate) {

                    for(int i=0; i<8; i++) {
                        if(random.nextBoolean()) {
                            isBitOn = data.bitIsOn(alongHeight, alongWidth, i);
                            data.setBit(alongHeight, alongWidth, i, !isBitOn);
                        }
                    }

                }

            }
        }
    }

}
