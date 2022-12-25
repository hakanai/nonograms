package nonograms.view

import nonograms.model.CellState
import nonograms.model.MutableGrid
import nonograms.model.event.StateChangeEvent
import nonograms.model.event.StateChangeListener
import java.awt.*
import javax.swing.JComponent

/**
 * TODO: Document this file.
 */
class GridView(private val grid: MutableGrid) : JComponent() {
    init {
        grid.addGeneralStateChangeListener(object : StateChangeListener {
            override fun stateChanged(event: StateChangeEvent?) {
                repaint()
            }
        })
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(
            grid.columnCount * CELL_WIDTH + 1,
            grid.rowCount * CELL_HEIGHT + 1
        )
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        val rowCount = grid.rowCount
        val columnCount = grid.columnCount
        val xMax = grid.columnCount * CELL_WIDTH + 1
        val yMax = grid.rowCount * CELL_HEIGHT + 1

        // Background.
        g.setColor(BACKGROUND_COLOR)
        g.fillRect(0, 0, xMax, yMax)

        // Lines.
        g.setColor(LINE_COLOR)
        g2.stroke = BasicStroke(1f)
        for (row in 0..rowCount) {
            val y = row * CELL_HEIGHT
            g.drawLine(0, y, xMax, y)
        }
        for (col in 0..columnCount) {
            val x = col * CELL_WIDTH
            g.drawLine(x, 0, x, yMax)
        }
        g2.stroke = BasicStroke(2f)

        // Cell contents.
        for (row in 0 until rowCount) {
            val y0 = row * CELL_HEIGHT + EDGE_OFFSET
            val y1 = (row + 1) * CELL_HEIGHT - EDGE_OFFSET
            for (col in 0 until columnCount) {
                val x0 = col * CELL_WIDTH + EDGE_OFFSET
                val x1 = (col + 1) * CELL_WIDTH - EDGE_OFFSET
                when (grid.getCell(row, col).state) {
                    CellState.UNKNOWN -> continue

                    CellState.CROSSED -> {
                        g.setColor(CROSS_COLOR)
                        g.drawLine(x0, y0, x1, y1)
                        g.drawLine(x1, y0, x0, y1)
                    }

                    CellState.FILLED -> {
                        g.setColor(FILL_COLOR)
                        g.fillRect(x0, y0, x1 - x0, y1 - y0)
                    }
                }
            }
        }
    }

    companion object {
        private const val CELL_WIDTH = 20
        private const val CELL_HEIGHT = 20
        private const val EDGE_OFFSET = 3
        private val BACKGROUND_COLOR = Color.WHITE
        private val LINE_COLOR = Color.BLACK
        private val CROSS_COLOR = Color.RED
        private val FILL_COLOR = Color.BLACK
    }
}