package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.CellTypes;
import com.vandenbreemen.corpuscles.corpuscles.automaton.CellTypesTester;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CellTypesTesterTest {

    @Test
    public void testComputesCostFunctionForCorrectAnswer() {
        CellTypesTester tester = new CellTypesTester();
        CorpusclesData data = new CorpusclesData(3,3);
        data.activate(0,2);
        data.activate(2,2);

        //  True/false/true at the far right side
        int[] expectedActivations = {
                0,2,
                2,2
        };

        double cost = tester.computeCost(data, expectedActivations);
        assertEquals(0.0, cost);
    }

    @Test
    public void testComputesCostFunctionForCorrectAnswerSpecifyingCellsThatMustBeOff() {
        CellTypesTester tester = new CellTypesTester();
        CorpusclesData data = new CorpusclesData(3,3);
        data.activate(0,2);
        data.activate(2,2);
        data.activate(1,1);
        data.activate(1,0);

        //  True/false/true at the far right side
        int[] expectedActivations = {
                0,2,
                2,2
        };

        int[] expectedInActive = {
                1,1,
                1,0
        };

        double cost = tester.computeCost(data, expectedActivations, expectedInActive);
        assertEquals(0.5, cost);
    }

    @Test
    public void testComputesCostFunctionForIncorrectAnswer() {
        CellTypesTester tester = new CellTypesTester();
        CorpusclesData data = new CorpusclesData(3,3);

        //  True/false/true at the far right side
        int[] expectedActivations = {
                0,2,
                2,2
        };

        double cost = tester.computeCost(data, expectedActivations);
        assertEquals(1.0, cost);
    }

    @Test
    public void testComputesCostWhileIterating() {
        //  Setup cell types
        CorpusclesData cellTypeData = new CorpusclesData(2,2);
        cellTypeData.setBit(0,0, CellTypes.COUPLER_ENDPOINT, true);
        cellTypeData.setBit(0,0, CellTypes.COUPLER, true);
        cellTypeData.setBit(0,1, CellTypes.INHIBITOR, true);
        cellTypeData.setBit(0,1, CellTypes.InhibitorTypes.TwoCells.position, true);
        cellTypeData.setBit(0,1, CellTypes.COUPLER_ENDPOINT, true);
        cellTypeData.setBit(0,1, CellTypes.COUPLER, true);

        //  Run a single iteration
        CellTypesTester tester = new CellTypesTester();
        int[] expectedActivations = { 0,1, 1,1};
        int[] inputs = {0,0};
        double cost = tester.testSolution(cellTypeData, 2, inputs, expectedActivations);

        System.out.println(cost);
    }

}
