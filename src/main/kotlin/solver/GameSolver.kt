package nonograms.solver

import nonograms.model.Game
import nonograms.model.MutableGrid
import nonograms.model.Rule

/**
 * TODO: Document this file.
 */
class GameSolver(game: Game) {
    private val grid: MutableGrid
    private val ruleSolvers: MutableList<MutableRuleSolver> = ArrayList()

    init {
        grid = game.grid
        val rowRules: List<Rule> = game.rowRules
        for (i in rowRules.indices) {
            ruleSolvers.add(MutableRuleSolver(grid.viewRow(i), rowRules[i]))
        }
        val columnRules: List<Rule> = game.columnRules
        for (i in columnRules.indices) {
            ruleSolvers.add(MutableRuleSolver(grid.viewColumn(i), columnRules[i]))
        }
    }

    /**
     *
     * @return `true` if there is still work to be done.
     */
    fun step(): Boolean {
        // Safety in case the caller doesn't notice the return value of the last iteration.
        if (ruleSolvers.isEmpty()) {
            return false
        }
        print("Running rule solvers ")
        val iter = ruleSolvers.iterator()
        while (iter.hasNext()) {
            val ruleSolver = iter.next()
            if (!ruleSolver.step()) {
                print("$")
                iter.remove()
            } else {
                print(".")
            }
        }
        println()
        println("New field state:")
        println(grid)
        return ruleSolvers.isNotEmpty()
    }
}