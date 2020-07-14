package com.vandenbreemen.corpuscles.visualizer;


import com.vandenbreemen.corpuscles.CellularAutomaton;
import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.corpuscle.ConwayCell;
import com.vandenbreemen.corpuscles.corpuscles.HorizontalAssociationCorpuscle;

import java.util.Random;

public class CorpusclesVisualizerTest {

    public static void main(String[] args) {
        CorpusclesData data = new CorpusclesData(100,100);
        Simulation sim = new Simulation(data);

        sim.activate(5,5);
        sim.activate(4,5);
        sim.activate(3,5);
        sim.activate(1,5);
        sim.activate(1,3);
        sim.activate(1,0);
        sim.activate(2,4);
        sim.nextEpoch();

        final ConwayCell conwayCell = new ConwayCell(sim);
        final HorizontalAssociationCorpuscle horiz = new HorizontalAssociationCorpuscle(sim);
        CellularAutomaton automaton = new CellularAutomaton(sim) {

            @Override
            protected Corpuscle getCorpuscle(int alongWidth, int alongHeight, Simulation simulation) {

                if(new Random(System.nanoTime()).nextBoolean()) {
                    return horiz;
                }

                return conwayCell;
            }
        };
        CorpusclesVisualizer visualizer = new CorpusclesVisualizer(sim, automaton, null);
    }

}