package nonograms.view;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import nonograms.model.Field;
import nonograms.model.CellState;
import nonograms.model.event.StateChangeListener;
import nonograms.model.event.StateChangeEvent;

/**
 * TODO: Document this file.
 */
public class FieldView extends JComponent
{
    private static final int CELL_WIDTH = 20, CELL_HEIGHT = 20;
    private static final int EDGE_OFFSET = 3;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color CROSS_COLOR = Color.RED;
    private static final Color FILL_COLOR = Color.BLACK;

    private Field field;

    public FieldView(Field field)
    {
        this.field = field;

        field.addGeneralStateChangeListener(new StateChangeListener()
        {
            public void stateChanged(StateChangeEvent event)
            {
                repaint();
            }
        });
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(field.getColumnCount() * CELL_WIDTH + 1,
                             field.getRowCount() * CELL_HEIGHT + 1);
    }

    /**
     */
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        int rowCount = field.getRowCount();
        int columnCount = field.getColumnCount();
        int xMax = field.getColumnCount() * CELL_WIDTH + 1;
        int yMax = field.getRowCount() * CELL_HEIGHT + 1;

        // Background.
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, xMax, yMax);

        // Lines.
        g.setColor(LINE_COLOR);
        g2.setStroke(new BasicStroke(1));
        for (int row = 0; row <= rowCount; row++)
        {
            int y = row * CELL_HEIGHT;
            g.drawLine(0, y, xMax, y);
        }
        for (int col = 0; col <= columnCount; col++)
        {
            int x = col * CELL_WIDTH;
            g.drawLine(x, 0, x, yMax);
        }

        g2.setStroke(new BasicStroke(2));

        // Cell contents.
        for (int row = 0; row < rowCount; row++)
        {
            int y0 = row * CELL_HEIGHT + EDGE_OFFSET;
            int y1 = (row + 1) * CELL_HEIGHT - EDGE_OFFSET;

            for (int col = 0; col < columnCount; col++)
            {
                int x0 = col * CELL_WIDTH + EDGE_OFFSET;
                int x1 = (col + 1) * CELL_WIDTH - EDGE_OFFSET;

                CellState state = field.getCell(row, col).getState();
                if (state == null) continue;
                switch (state)
                {
                    case CROSSED:
                        g.setColor(CROSS_COLOR);
                        g.drawLine(x0, y0, x1, y1);
                        g.drawLine(x1, y0, x0, y1);
                        break;
                    case FILLED:
                        g.setColor(FILL_COLOR);
                        g.fillRect(x0, y0, x1-x0, y1-y0);
                        break;
                }
            }
        }
    }
}
