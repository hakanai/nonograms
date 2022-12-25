package nonograms.model

import nonograms.model.event.StateChangeEvent
import nonograms.model.event.StateChangeListener
import java.util.*

/**
 * TODO: Document this file.
 */
class MutableGrid(val rowCount: Int, val columnCount: Int) {
    private val cells: Array<Array<MutableCell>> = Array(rowCount) { row ->
        Array(columnCount) { col ->
            MutableCell(this, row, col)
        }
    }
    private val rowListeners = mutableListOf<List<StateChangeListener>>()
    private val columnListeners = mutableListOf<List<StateChangeListener>>()
    private val generalListeners = mutableListOf<StateChangeListener>()

    init {
        repeat(rowCount) {
            rowListeners.add(ArrayList<StateChangeListener>())
        }
        repeat(columnCount) {
            columnListeners.add(ArrayList<StateChangeListener>())
        }
    }

    fun getCell(row: Int, col: Int): MutableCell {
        require(row in cells.indices) { "Row out of range: $row" }
        require(col in cells[0].indices) { "Column out of range: $col" }
        return cells[row][col]
    }

    fun viewRow(row: Int): List<MutableCell> {
        return object : AbstractList<MutableCell>() {
            override val size: Int = columnCount

            override fun get(index: Int): MutableCell {
                return getCell(row, index)
            }
        }
    }

    fun viewColumn(col: Int): List<MutableCell> {
        return object : AbstractList<MutableCell>() {
            override val size: Int = rowCount

            override fun get(index: Int): MutableCell {
                return getCell(index, col)
            }
        }
    }

    fun fireStateChanged(row: Int, col: Int) {
        val event = StateChangeEvent(row, col)
        for (listener in generalListeners) {
            listener.stateChanged(event)
        }
        for (listener in rowListeners[row]) {
            listener.stateChanged(event)
        }
        for (listener in columnListeners[col]) {
            listener.stateChanged(event)
        }
    }

    fun addGeneralStateChangeListener(listener: StateChangeListener) {
        generalListeners.add(listener)
    }

    override fun toString(): String {
        return buildString {
            for (row in 0 until rowCount) {
                for (col in 0 until columnCount) {
                    when (getCell(row, col).state) {
                        CellState.UNKNOWN -> append(" ")
                        CellState.CROSSED -> append(".")
                        CellState.FILLED -> append("#")
                    }
                }
                append("\n")
            }
        }
    }
}