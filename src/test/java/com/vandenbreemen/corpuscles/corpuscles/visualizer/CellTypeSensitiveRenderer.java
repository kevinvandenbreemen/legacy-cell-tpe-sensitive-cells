package com.vandenbreemen.corpuscles.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.corpuscles.celltypes.CellTypeSensitiveCell;
import com.vandenbreemen.corpuscles.visualizer.CellRenderer;

import java.awt.*;

public class CellTypeSensitiveRenderer extends CellRenderer {

    private CellTypeSensitiveCell cell;

    public CellTypeSensitiveRenderer(CellTypeSensitiveCell cell) {
        this.cell = cell;
    }

    @Override
    public void drawCell(Graphics g, int wSize, int hSize, Simulation simulation, int alongHeight, int alongWidth, int y, int x) {

        if(!(g instanceof Graphics2D)) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) g;

        int midPtx = x+(wSize/2);
        int midPty = y+(hSize/2);

        String cellTypeStr = "unkn";

        Color color = Color.white;
        if(cell.isPulsingCell(alongHeight, alongWidth)) {
            color = Color.orange;
            cellTypeStr = "plsr";
        } else if(cell.isInhibitorCell(alongHeight, alongWidth)) {
            color = Color.RED;
            cellTypeStr = "inhb";
        } else if(cell.isCouplingCell(alongHeight, alongWidth)) {
            color = Color.blue;
            cellTypeStr = "cpl";
        } else if(cell.isCouplingEndpoint(alongHeight, alongWidth)) {
            color = Color.green;
            cellTypeStr = "edpt";
        }

        g.setColor(color);
        if(simulation.activated(alongHeight, alongWidth)) {
            g.fillRect(x, y, wSize, hSize);
        } else if (g instanceof Graphics2D){
            graphics2D.setStroke(new BasicStroke(2.0f));
            graphics2D.drawRect(x, y, wSize, hSize);

        }

        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("TimesRoman", Font.PLAIN, 8));
        graphics2D.drawString(cellTypeStr, midPtx, midPty);

    }
}
