package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.Simulation;

import java.awt.*;

/**
 * Renders an individual cell
 */
public class CellRenderer {

    public void drawCell(Graphics g, int wSize, int hSize, Simulation simulation, int alongHeight, int alongWidth, int y, int x) {
        if(simulation.activated(alongHeight, alongWidth)) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.BLACK);
        }

        g.fillRect(x, y, wSize, hSize);
    }

}
