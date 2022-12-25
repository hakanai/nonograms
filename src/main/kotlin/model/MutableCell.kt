package nonograms.model

/**
 * TODO: Document this file.
 */
class MutableCell internal constructor(private val grid: MutableGrid, private val row: Int, private val col: Int) {
    var state: CellState = CellState.UNKNOWN
        set(newState) {
            val oldState: CellState = state
            field = newState
            if (oldState != newState) {
                grid.fireStateChanged(row, col)
            }
        }

    override fun toString() = state.toString()
}