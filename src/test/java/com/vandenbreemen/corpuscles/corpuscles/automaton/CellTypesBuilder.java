package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;

import java.util.Random;

/**
 * Builds up a grid of cell types for each cell of the automaton
 */
public class CellTypesBuilder {

    public CorpusclesData getCellTypes(int height, int width) {
        CorpusclesData data = new CorpusclesData(height, width);
        Random rand = new Random(System.nanoTime());

        byte[] rawBytes = new byte[height*width];
        rand.nextBytes(rawBytes);

        int cnt = 0;
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                data.writeData(i, j, rawBytes[cnt]);
                cnt++;
            }
        }

        return data;
    }

}
