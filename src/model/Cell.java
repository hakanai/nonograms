package nonograms.model;

/**
 * TODO: Document this file.
 */
public class Cell
{
    private CellState state;
    private Field field;
    private int row;
    private int col;

    Cell(Field field, int row, int col)
    {
        this.field = field;
        this.row = row;
        this.col = col;
    }

    public CellState getState()
    {
        return state;
    }

    public void setState(CellState newState)
    {
        CellState oldState = state;
        state = newState;
        if (oldState != newState)
        {
            field.fireStateChanged(row, col);
        }
    }
}
