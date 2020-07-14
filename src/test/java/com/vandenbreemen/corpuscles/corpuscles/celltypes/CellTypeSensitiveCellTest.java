package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.corpuscles.CellTypes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CellTypeSensitiveCellTest {

    private CorpusclesData cells;
    private CorpusclesData cellTypes;

    @Before
    public void setup() {
        cells = new CorpusclesData(10,10);
        cellTypes = new CorpusclesData(10,10);
    }


    @Test
    public void testCouplingSideBySideActivatedAfterFirstThreeBitsTurnedOn() {

        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.HORIZONTAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_3, true);
        CellTypeSensitiveSimulation cellTypesSim = new CellTypeSensitiveSimulation(cells, cellTypes);

        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(cellTypesSim);
        cellTypesSim.activate(1,1);
        cellTypesSim.activate(1,3);

        //  Activate the first three first

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        //  Now I should be on
        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertTrue(cellTypesSim.activated(1,2));


    }

    @Test
    public void testCouplerSendsSignalForwardWhenConfiguredToDoSo() {
        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.HORIZONTAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_3, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_4, true);
        CellTypeSensitiveSimulation cellTypesSim = new CellTypeSensitiveSimulation(cells, cellTypes);

        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(cellTypesSim);
        cellTypesSim.activate(1,1);
        cellTypesSim.nextEpoch();

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertTrue(cellTypesSim.activated(1,2));
    }

    @Test
    public void testCouplingSideBySideActivatedAfterFirstFourBitsTurnedOn() {

        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.HORIZONTAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_4, true);
        CellTypeSensitiveSimulation cellTypesSim = new CellTypeSensitiveSimulation(cells, cellTypes);

        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(cellTypesSim);
        cellTypesSim.activate(1,1);
        cellTypesSim.activate(1,3);

        //  Activate the first three first

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertFalse(cellTypesSim.activated(1,2));

        //  Now I should be on
        cell.takeTurn(1,2);
        cellTypesSim.nextEpoch();

        assertTrue(cellTypesSim.activated(1,2));


    }

    @Test
    public void testDetectsWhenAdjacentCellIsCouplerAndIsTurnedOn() {

        //  Set up coupler cell
        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.VERTICAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_3, true);

        //  Set up coupler endpoint
        cellTypes.setBit(2, 2, CellTypes.COUPLER_ENDPOINT, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Turn on the coupler
        simulation.activate(1,2);
        simulation.nextEpoch();


        //  Now execute turn for endpoint.  Endpoint should be turned on
        assertFalse(cellTypes.activated(2,2));

        cell.takeTurn(2,2);
        simulation.nextEpoch();

        assertTrue(simulation.activated(2,2));

    }



    @Test
    public void testDetectsWhenAdjacentCellIsCouplerEndpointCellDoesNotActivateIfCouplerNotActivated() {

        //  Set up coupler cell
        cellTypes.setBit(1,2, CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.VERTICAL.position, true);
        cellTypes.setBit(1,2, CellTypes.CouplerTypes.CouplerActivations.FIRST_3, true);

        //  Set up coupler endpoint
        cellTypes.setBit(2, 2, CellTypes.COUPLER_ENDPOINT, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Leave coupler off
        simulation.nextEpoch();


        //  Now execute turn for endpoint.  Endpoint should be turned on
        assertFalse(cellTypes.activated(2,2));

        cell.takeTurn(2,2);
        simulation.nextEpoch();

        assertFalse(simulation.activated(2,2));

    }

    @Test
    public void testInhibitoryActivatesIfSufficientCellsNearbyActivated() {
        //  Set up inhibitory cell
        cellTypes.setBit(1,2, CellTypes.INHIBITOR, true);
        cellTypes.setBit(1,2, CellTypes.InhibitorTypes.TwoCells.position, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Turn on the inhibitor and an adjacent cell
        simulation.activate(1,1);
        simulation.activate(2,2);
        simulation.nextEpoch();

        //  Turn on the inhibitor since two cells beside it are on
        cell.takeTurn(1,2);
        simulation.nextEpoch();

        //  Inhibitor should be on
        assertTrue(simulation.activated(1,2));
    }

    @Test
    public void testInhibitoryActivatesIfSufficientCellsNearbyActivated_FourCells() {
        //  Set up inhibitory cell
        cellTypes.setBit(1,2, CellTypes.INHIBITOR, true);
        cellTypes.setBit(1,2, CellTypes.InhibitorTypes.FourCells.position, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Turn on the inhibitor and an adjacent cell
        simulation.activate(1,1);
        simulation.activate(2,2);
        simulation.activate(1,3);
        simulation.activate(2,3);

        simulation.nextEpoch();

        //  Turn on the inhibitor since two cells beside it are on
        cell.takeTurn(1,2);
        simulation.nextEpoch();

        //  Inhibitor should be on
        assertTrue(simulation.activated(1,2));
    }

    @Test
    public void testInhibitoryDeactivatesIfInsufficientCellsNearbyActivated() {
        //  Set up inhibitory cell
        cellTypes.setBit(1,2, CellTypes.INHIBITOR, true);
        cellTypes.setBit(1,2, CellTypes.InhibitorTypes.TwoCells.position, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Turn on the inhibitor and an adjacent cell
        simulation.activate(1,2);
        simulation.activate(1,1);
        simulation.nextEpoch();

        //  Turn on the inhibitor since two cells beside it are on
        cell.takeTurn(1,2);
        simulation.nextEpoch();

        //  Inhibitor should be on
        assertFalse(simulation.activated(1,2));
    }

    @Test
    public void testNeighboursOfInhibitorDeactivateIfInhibitorIsActivated() {
        //  Set up inhibitory cell
        cellTypes.setBit(1,2, CellTypes.INHIBITOR, true);
        cellTypes.setBit(1,2, CellTypes.InhibitorTypes.TwoCells.position, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        simulation.activate(1,2);
        simulation.activate(1,1);
        simulation.nextEpoch();

        //  Now have the cell at 1,1 take its turn and it should deactivate
        cell.takeTurn(1,1);
        simulation.nextEpoch();

        assertFalse(simulation.activated(1,1));
    }

    @Test
    public void testPulsingCellActivatesOnFirstTurn() {
        //  Set up pulsing cell
        cellTypes.setBit(1,2,CellTypes.COUPLER_ENDPOINT, true);
        cellTypes.setBit(1,2,CellTypes.COUPLER, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        simulation.nextEpoch();

        cell.takeTurn(1,2);
        simulation.nextEpoch();

        assertTrue(simulation.activated(1,2));
    }

    @Test
    public void testPulsingCellTurnsOffAfterActivated() {
        //  Set up pulsing cell
        cellTypes.setBit(1,2,CellTypes.COUPLER_ENDPOINT, true);
        cellTypes.setBit(1,2,CellTypes.COUPLER, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        simulation.nextEpoch();

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        cell.takeTurn(1,2);
        simulation.nextEpoch();

        assertFalse(simulation.activated(1,2));
    }

    @Test
    public void testPulsingCellDeactivatesForTwoEpochsWhenConfiguredToDoSo() {
        //  Set up pulsing cell
        cellTypes.setBit(1,2,CellTypes.COUPLER_ENDPOINT, true);
        cellTypes.setBit(1,2,CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.PulseTypes.TwoEpochs.position, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Blinker off for first two iterations
        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertTrue(simulation.activated(1,2));

        //  Then turns off for another two iterations
        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertTrue(simulation.activated(1,2));
    }

    @Test
    public void testPulsingCellDeactivatesForThreeEpochsWhenConfiguredToDoSo() {
        //  Set up pulsing cell
        cellTypes.setBit(1,2,CellTypes.COUPLER_ENDPOINT, true);
        cellTypes.setBit(1,2,CellTypes.COUPLER, true);
        cellTypes.setBit(1,2, CellTypes.PulseTypes.ThreeEpochs.position, true);

        CellTypeSensitiveSimulation simulation = new CellTypeSensitiveSimulation(cells, cellTypes);
        CellTypeSensitiveCell cell = new CellTypeSensitiveCell(simulation);

        //  Blinker off for first two iterations
        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertTrue(simulation.activated(1,2));

        //  Then turns off for another two iterations
        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertFalse(simulation.activated(1,2));

        cell.takeTurn(1,2);
        simulation.nextEpoch();
        assertTrue(simulation.activated(1,2));
    }

}