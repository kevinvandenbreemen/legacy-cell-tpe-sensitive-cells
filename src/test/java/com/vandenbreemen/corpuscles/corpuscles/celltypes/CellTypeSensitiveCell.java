package com.vandenbreemen.corpuscles.corpuscles.celltypes;

import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.corpuscles.CellTypes;

import static com.vandenbreemen.corpuscles.Simulation.NeighbourHoodRange.*;

/**
 * Cell that is aware of its own cell type
 */
public class CellTypeSensitiveCell extends Corpuscle {

    private CellTypeSensitiveSimulation cellTypeSimulation() {
        return (CellTypeSensitiveSimulation)simulation;
    }

    public CellTypeSensitiveCell(Simulation simulation) {
        super(simulation);
        if (!(simulation instanceof CellTypeSensitiveSimulation)){
            throw new RuntimeException("Cannot create this kind of cell without a " + CellTypeSensitiveSimulation.class.getSimpleName());
        }
    }

    public boolean isCouplingCell(int alongHeight, int alongWidth) {
        return cellTypeSimulation().cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.COUPLER);
    }

    public boolean isInhibitorCell(int alongHeight, int alongWidth) {
        if(cellTypeSimulation().cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.INHIBITOR)){
            return !isCouplingCell(alongHeight, alongWidth);
        }
        return false;
    }

    public boolean isPulsingCell(int alongHeight, int alongWidth) {
        return isCouplingCell(alongHeight, alongWidth) && isCouplingEndpoint(alongHeight, alongWidth);
    }

    public boolean isCouplingEndpoint(int alongHeight, int alongWidth) {
        return cellTypeSimulation().cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.COUPLER_ENDPOINT);
    }

    /**
     * Returns a quad of cell coordinates for the endpoints of a coupler cell
     * @param alongHeight
     * @param alongWidth
     * @return  Coords as follows:  [cell1_h, cell1_w, cell2_h, cell2_w]
     */
    private int[] getCouplingEndpointLocations(int alongHeight, int alongWidth) {

        Simulation cellTypes = cellTypeSimulation().cellTypes;
        int[] neighbourhood = simulation.getMooreNeighbourhoodRange(alongHeight, alongWidth);
        int[] cellCoordOne;
        int[] cellCoordTwo;

        if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.HORIZONTAL.position)) {
            cellCoordOne = new int[]{alongHeight, neighbourhood[WIDTH_MIN]};
            cellCoordTwo = new int[]{alongHeight, neighbourhood[WIDTH_MAX]};
        } else if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.VERTICAL.position)) {
            cellCoordOne = new int[]{neighbourhood[HEIGHT_MIN], alongWidth};
            cellCoordTwo = new int[]{neighbourhood[HEIGHT_MAX], alongWidth};
        } else if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.FWD_SLASH.position)) {
            cellCoordOne = new int[]{neighbourhood[HEIGHT_MIN], neighbourhood[WIDTH_MIN]};
            cellCoordTwo = new int[]{neighbourhood[HEIGHT_MAX], neighbourhood[WIDTH_MAX]};
        } else /*Backslash*/ {
            cellCoordOne = new int[]{neighbourhood[HEIGHT_MAX], neighbourhood[WIDTH_MIN]};
            cellCoordTwo = new int[]{neighbourhood[HEIGHT_MIN], neighbourhood[WIDTH_MAX]};
        }

        return new int[] {
            cellCoordOne[0],
            cellCoordOne[1],
            cellCoordTwo[0],
            cellCoordTwo[1]
        };
    }

    @Override
    public void takeTurn(int alongHeight, int alongWidth) {

        int[] neighbourhood = simulation.getMooreNeighbourhoodRange(alongHeight, alongWidth);

        //  First check if we're being inhibited
        for(int i=neighbourhood[HEIGHT_MIN]; i<=neighbourhood[HEIGHT_MAX]; i++) {
            for (int j = neighbourhood[WIDTH_MIN]; j <= neighbourhood[WIDTH_MAX]; j++) {
                if (i == alongHeight && j == alongWidth) {
                    continue;
                }
                if (isInhibitorCell(i, j) && simulation.activated(i, j)) {
                    simulation.deactivate(alongHeight, alongWidth);
                    return;
                }
            }
        }


        Simulation cellTypes = cellTypeSimulation().cellTypes;

        if (isInhibitorCell(alongHeight, alongWidth)) {
            doInhibitorCellLogic(alongHeight, alongWidth, neighbourhood, cellTypes);
        } else if (isPulsingCell(alongHeight, alongWidth)) {  //  Pulsing cell
            doPulsingCellLogic(alongHeight, alongWidth, cellTypes);
        }
        else if(isCouplingCell(alongHeight, alongWidth)) {
            doCouplingLogic(alongHeight, alongWidth, neighbourhood, cellTypes);

        } else if(isCouplingEndpoint(alongHeight, alongWidth)) {
            doCouplingEndpointLogic(alongHeight, alongWidth, neighbourhood);
        }

    }

    private void doInhibitorCellLogic(int alongHeight, int alongWidth, int[] neighbourhood, Simulation cellTypes) {
        int numRequiredForActivation = 9;   //  Too many so not satisfied
        if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.InhibitorTypes.TwoCells.position)) {
            numRequiredForActivation = 2;
        } else if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.InhibitorTypes.FourCells.position)) {
            numRequiredForActivation = 4;
        }

        int numActivated = 0;
        for(int i=neighbourhood[HEIGHT_MIN]; i<=neighbourhood[HEIGHT_MAX]; i++) {
            for (int j = neighbourhood[WIDTH_MIN]; j <= neighbourhood[WIDTH_MAX]; j++) {
                if (i == alongHeight && j == alongWidth) {
                    continue;
                }
                if(simulation.activated(i, j)) {
                    numActivated ++;
                }
            }
        }

        if(numActivated >= numRequiredForActivation) {
            simulation.activate(alongHeight, alongWidth);
        } else {
            simulation.deactivate(alongHeight, alongWidth);
        }
    }

    private void doCouplingEndpointLogic(int alongHeight, int alongWidth, int[] neighbourhood) {
        for(int i=neighbourhood[HEIGHT_MIN]; i<=neighbourhood[HEIGHT_MAX]; i++) {
            for(int j= neighbourhood[WIDTH_MIN]; j<=neighbourhood[WIDTH_MAX]; j++) {
                if(i==alongHeight && j==alongWidth) { continue; }
                if(isCouplingCell(i, j) && simulation.activated(i,j)) {
                    int[] couplingEndpointLocations = getCouplingEndpointLocations(i, j);
                    if( (alongHeight == couplingEndpointLocations[0] && alongWidth == couplingEndpointLocations[1]) ||
                            (alongHeight == couplingEndpointLocations[2] && alongWidth == couplingEndpointLocations[3]) ){
                        simulation.activate(alongHeight, alongWidth);
                    }
                }
            }
        }
    }

    private void doCouplingLogic(int alongHeight, int alongWidth, int[] neighbourhood, Simulation cellTypes) {
        //  Step 1:  Identify the cells we're coupling
        int[] cellCoordOne;
        int[] cellCoordTwo;

        if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.HORIZONTAL.position)) {
            cellCoordOne = new int[]{alongHeight, neighbourhood[WIDTH_MIN]};
            cellCoordTwo = new int[]{alongHeight, neighbourhood[WIDTH_MAX]};
        } else if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.VERTICAL.position)) {
            cellCoordOne = new int[]{neighbourhood[HEIGHT_MIN], alongWidth};
            cellCoordTwo = new int[]{neighbourhood[HEIGHT_MAX], alongWidth};
        } else if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.FWD_SLASH.position)) {
            cellCoordOne = new int[]{neighbourhood[HEIGHT_MIN], neighbourhood[WIDTH_MIN]};
            cellCoordTwo = new int[]{neighbourhood[HEIGHT_MAX], neighbourhood[WIDTH_MAX]};
        } else /*Backslash*/ {
            cellCoordOne = new int[]{neighbourhood[HEIGHT_MAX], neighbourhood[WIDTH_MIN]};
            cellCoordTwo = new int[]{neighbourhood[HEIGHT_MIN], neighbourhood[WIDTH_MAX]};
        }

        //  Step 2:  Check the states of these cells
        boolean cell1On = simulation.activated(cellCoordOne[0], cellCoordOne[1]) || simulation.activated(cellCoordOne[1], cellCoordOne[0]);
        boolean cell2On = simulation.activated(cellCoordTwo[0], cellCoordTwo[1]) || simulation.activated(cellCoordTwo[1], cellCoordTwo[0]);

        //  Step 3:  Determine what to do with activations

        boolean carrySignalForward = cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.CouplerActivations.FIRST_3) &&
                cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.CouplerActivations.FIRST_4);
        if (carrySignalForward && (cell1On || cell2On)){
            simulation.activate(alongHeight, alongWidth);
        }
        else if(cell1On && cell2On) {

            //  Do I just want to turn on my first three slots?
            if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.CouplerActivations.FIRST_3) ||
                    cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.CouplerActivations.FIRST_4)) {

                boolean isFirst4 = cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.CouplerTypes.CouplerActivations.FIRST_4);

                //  Step 3a:  Are all my bits turned on?
                if(isFirst4 && simulation.bitIsOn(alongHeight, alongWidth, 1) &&
                        simulation.bitIsOn(alongHeight, alongWidth, 2) &&
                        simulation.bitIsOn(alongHeight, alongWidth, 3) &&
                        simulation.bitIsOn(alongHeight, alongWidth, 4)
                ) {
                    simulation.activate(alongHeight, alongWidth);
                }
                else if(!isFirst4 && simulation.bitIsOn(alongHeight, alongWidth, 1) &&
                        simulation.bitIsOn(alongHeight, alongWidth, 2) &&
                        simulation.bitIsOn(alongHeight, alongWidth, 3)) {
                    simulation.activate(alongHeight, alongWidth);
                }
                else {
                    if(!simulation.bitIsOn(alongHeight, alongWidth, 1)) {
                        simulation.setBit(alongHeight, alongWidth, 1, true);
                    }
                    else if(!simulation.bitIsOn(alongHeight, alongWidth, 2)) {
                        simulation.setBit(alongHeight, alongWidth, 2, true);
                    }
                    else if(!simulation.bitIsOn(alongHeight, alongWidth, 3)) {
                        simulation.setBit(alongHeight, alongWidth, 3, true);
                    } else if (isFirst4 && ! simulation.bitIsOn(alongHeight, alongWidth, 4)) {
                        simulation.setBit(alongHeight, alongWidth, 4, true);
                    }
                }
            }

        }
    }

    private void doPulsingCellLogic(int alongHeight, int alongWidth, Simulation cellTypes) {
        if(!simulation.activated(alongHeight, alongWidth)) {

            if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.PulseTypes.TwoEpochs.position)) {
                if(!simulation.bitIsOn(alongHeight, alongWidth, 1)) {
                    simulation.setBit(alongHeight, alongWidth, 1, true);
                } else if(!simulation.bitIsOn(alongHeight, alongWidth, 2)) {
                    simulation.setBit(alongHeight, alongWidth, 2, true);
                } else {
                    simulation.activate(alongHeight, alongWidth);
                    simulation.setBit(alongHeight, alongWidth, 1, false);
                    simulation.setBit(alongHeight, alongWidth, 2, false);
                }

            } else if (cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.PulseTypes.ThreeEpochs.position)) {
                if(!simulation.bitIsOn(alongHeight, alongWidth, 1)) {
                    simulation.setBit(alongHeight, alongWidth, 1, true);
                } else if(!simulation.bitIsOn(alongHeight, alongWidth, 2)) {
                    simulation.setBit(alongHeight, alongWidth, 2, true);
                } else if(!simulation.bitIsOn(alongHeight, alongWidth, 3)) {
                    simulation.setBit(alongHeight, alongWidth, 3, true);
                } else {
                    simulation.activate(alongHeight, alongWidth);
                    simulation.setBit(alongHeight, alongWidth, 1, false);
                    simulation.setBit(alongHeight, alongWidth, 2, false);
                    simulation.setBit(alongHeight, alongWidth, 3, false);
                }
            } else {
                simulation.activate(alongHeight, alongWidth);
            }
        } else {

            simulation.deactivate(alongHeight, alongWidth);
            if(cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.PulseTypes.TwoEpochs.position) ||
                    cellTypes.bitIsOn(alongHeight, alongWidth, CellTypes.PulseTypes.ThreeEpochs.position)) {
                simulation.setBit(alongHeight, alongWidth, 1, true);
            }
        }
    }
}
