package nonograms.model

/**
 * States of a cell.
 */
enum class CellState {
    /** Cell is unknown */
    UNKNOWN,

    /** Cell is crossed out (known not to be filled.)  */
    CROSSED,

    /** Cell is filled.  */
    FILLED
}