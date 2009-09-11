package nonograms.model;

import java.util.List;
import java.util.AbstractList;
import java.util.ArrayList;

import nonograms.model.event.StateChangeListener;
import nonograms.model.event.StateChangeEvent;

/**
 * TODO: Document this file.
 */
public class Field
{
    private int columnCount, rowCount;
    private Cell[][] cells;

    private List<List<StateChangeListener>> rowListeners;
    private List<List<StateChangeListener>> columnListeners;
    private List<StateChangeListener> generalListeners = new ArrayList<StateChangeListener>();

    public Field(int rowCount, int columnCount)
    {
        this.columnCount = columnCount;
        this.rowCount = rowCount;

        cells = new Cell[rowCount][];
        for (int row = 0; row < rowCount; row++)
        {
            cells[row] = new Cell[columnCount];
            for (int col = 0; col < columnCount; col++)
            {
                cells[row][col] = new Cell(this, row, col);
            }
        }

        rowListeners = new ArrayList<List<StateChangeListener>>();
        for (int i = 0; i < rowCount; i++)
        {
            rowListeners.add(new ArrayList<StateChangeListener>());
        }

        columnListeners = new ArrayList<List<StateChangeListener>>();
        for (int i = 0; i < columnCount; i++)
        {
            columnListeners.add(new ArrayList<StateChangeListener>());
        }
    }

    public int getColumnCount()
    {
        return columnCount;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public Cell getCell(int row, int col)
    {
        return cells[row][col];
    }

    public List<Cell> viewRow(final int row)
    {
        return new AbstractList<Cell>()
        {
            public Cell get(int col)
            {
                return getCell(row, col);
            }

            public int size()
            {
                return columnCount;
            }
        };
    }

    public List<Cell> viewColumn(final int col)
    {
        return new AbstractList<Cell>()
        {
            public Cell get(int row)
            {
                return getCell(row, col);
            }

            public int size()
            {
                return rowCount;
            }
        };
    }

    void fireStateChanged(int row, int col)
    {
        StateChangeEvent event = new StateChangeEvent(row, col);

        for (StateChangeListener listener : generalListeners)
        {
            listener.stateChanged(event);
        }

        for (StateChangeListener listener : rowListeners.get(row))
        {
            listener.stateChanged(event);
        }

        for (StateChangeListener listener : columnListeners.get(col))
        {
            listener.stateChanged(event);
        }
    }

    public void addGeneralStateChangeListener(StateChangeListener listener)
    {
        generalListeners.add(listener);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < rowCount; row++)
        {
            for (int col = 0; col < columnCount; col++)
            {
                CellState state = getCell(row, col).getState();
                if (state == null)
                {
                    builder.append(" ");
                }
                else
                {
                    switch (state)
                    {
                        case CROSSED: builder.append("."); break;
                        case FILLED: builder.append("#"); break;
                    }
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
