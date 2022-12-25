package nonograms.solver

import nonograms.model.CellState
import nonograms.model.MutableCell
import nonograms.model.Rule

/**
 * TODO: Document this file.
 */
class MutableRuleSolver(private val cells: List<MutableCell>, private val rule: Rule) {
    private val possibilities: MutableList<Array<CellState>> = ArrayList()

    init {
        rule.counts

        // Build the list of possible layouts.
        val ruleCounts = rule.counts
        val possibility = Array(cells.size) { CellState.CROSSED }
        collectPossibleLayouts(possibility, 0, ruleCounts)
    }

    private fun collectPossibleLayouts(possibility: Array<CellState>, startingOffset: Int, ruleCounts: List<Int>) {
        // Special case for when there are no counts from the start.
        if (ruleCounts.isEmpty()) {
            possibilities.add(possibility)
            return
        }
        val firstCount = ruleCounts[0]
        val remainingCounts = ruleCounts.subList(1, ruleCounts.size)
        for (offset in startingOffset..possibility.size - firstCount) {
            // Create a copy of the possibility and fill the cells representing this offset.
            val possibilityCopy = possibility.copyOf()
            for (i in 0 until firstCount) {
                possibilityCopy[offset + i] = CellState.FILLED
            }
            if (remainingCounts.isEmpty()) {
                // End of line, add to the list.
                possibilities.add(possibilityCopy)
            } else {
                // Recurse.
                val newStartingOffset = offset + firstCount + 1
                collectPossibleLayouts(possibilityCopy, newStartingOffset, remainingCounts)
            }
        }
    }

    /**
     * Performs one iteration of solving.
     *
     * @return `true` if there is still work to be done.
     */
    fun step(): Boolean {
        // Step 1. Go through the possibilities and remove the ones which can no longer possibly apply.
        val iter = possibilities.iterator()
        while (iter.hasNext()) {
            val possibility = iter.next()
            for (i in cells.indices) {
                val state = cells[i].state
                if (state != CellState.UNKNOWN && state != possibility[i]) {
                    iter.remove()
                    break
                }
            }
        }
        check(possibilities.size != 0) { "Puzzle is unsolvable" }

        // Step 2. Go through each index and if all possibilities have the same value,
        //         set that value in the cells.
        for (i in cells.indices) {
            var state = possibilities[0][i] // non-null by definition.
            for (j in 1 until possibilities.size) {
                if (possibilities[j][i] !== state) {
                    state = CellState.UNKNOWN
                    break
                }
            }
            if (state != CellState.UNKNOWN) {
                cells[i].state = state
            }
        }
        return possibilities.size > 1
    }
}