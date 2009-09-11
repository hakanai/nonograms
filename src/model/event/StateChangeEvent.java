package nonograms.model.event;

/**
 * TODO: Document this file.
 */
public class StateChangeEvent
{
    private int row;
    private int col;

    public StateChangeEvent(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

}
