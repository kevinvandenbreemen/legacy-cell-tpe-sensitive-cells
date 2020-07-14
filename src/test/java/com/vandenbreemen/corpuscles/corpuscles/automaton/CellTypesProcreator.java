package com.vandenbreemen.corpuscles.corpuscles.automaton;

import com.vandenbreemen.corpuscles.CorpusclesData;

import java.util.List;
import java.util.Random;

/**
 * Given a set of solutions deemed fit to survive, provides logic to have them procreate/mutate etc.
 */
public class CellTypesProcreator {


    public CorpusclesData getNextSolution(List<CorpusclesData> cellTypeData, CellTypesMutator cellTypesMutator) {

        //  For now mate solutions
        Random random = new Random(System.nanoTime());
        int ind1 = random.nextInt(cellTypeData.size());
        int ind2 = random.nextInt(cellTypeData.size());
        while (ind2 == ind1) {  //  Cheesy but very unlikely
            ind2 = random.nextInt(cellTypeData.size());
        }

        CorpusclesData ret = mate(cellTypeData.get(ind1), cellTypeData.get(ind2));
        if(cellTypesMutator != null) {
            cellTypesMutator.mutate(ret);
        }
        return ret;
    }

    private CorpusclesData mate(CorpusclesData mother, CorpusclesData father) {

        CorpusclesData child = new CorpusclesData(mother.height(), mother.width());
        Random random = new Random(System.nanoTime());
        for(int alongHeight = 0; alongHeight<mother.height(); alongHeight++) {
            for(int alongWidth = 0; alongWidth<mother.width(); alongWidth++) {
                if(random.nextBoolean()) {
                    child.writeData(alongHeight, alongWidth, mother.data(alongHeight, alongWidth));
                } else {
                    child.writeData(alongHeight, alongWidth, father.data(alongHeight, alongWidth));
                }
            }
        }

        return child;
    }

}
