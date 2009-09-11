package nonograms.model;

/**
 * States of a cell.  {@code null} would represent the state being unknown.
 */
public enum CellState
{
    /** Cell is crossed out (known not to be filled.) */
    CROSSED,

    /** Cell is filled. */
    FILLED,
}