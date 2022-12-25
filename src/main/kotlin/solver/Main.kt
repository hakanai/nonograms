package nonograms.solver

import nonograms.model.Game
import nonograms.view.GridView
import java.awt.BorderLayout
import javax.swing.JFrame

fun main() {
    val game = Game.fromBitmapResource("/Sample.png")
    val frame = JFrame("Nonograms").apply {
        layout = BorderLayout()
        add(GridView(game.grid), BorderLayout.CENTER)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        pack()
        isResizable = false
        isVisible = true
    }
    val solver = GameSolver(game)
    var i = 1
    while (solver.step()) {
        i++
    }
    println("Completed in $i steps")
}
