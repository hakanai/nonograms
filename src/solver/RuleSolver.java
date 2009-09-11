package nonograms.solver;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import nonograms.model.Rule;
import nonograms.model.Cell;
import nonograms.model.CellState;

/**
 * TODO: Document this file.
 */
public class RuleSolver
{
    private List<Cell> cells;

    private List<CellState[]> possibilities = new ArrayList<CellState[]>();

    public RuleSolver(List<Cell> cells, Rule rule)
    {
        this.cells = cells;

        rule.getCounts();

        // Build the list of possible layouts.
        List<Integer> ruleCounts = rule.getCounts();
        CellState[] possibility = new CellState[cells.size()];
        for (int i = 0; i < possibility.length; i++)
        {
            possibility[i] = CellState.CROSSED;
        }
        collectPossibleLayouts(possibility, 0, ruleCounts);
    }

    private void collectPossibleLayouts(CellState[] possibility, int startingOffset, List<Integer> ruleCounts)
    {
        // Special case for when there are no counts from the start.
        if (ruleCounts.isEmpty())
        {
            possibilities.add(possibility);
            return;
        }

        int firstCount = ruleCounts.get(0);
        List<Integer> remainingCounts = ruleCounts.subList(1, ruleCounts.size());

        for (int offset = startingOffset; offset <= possibility.length - firstCount; offset++)
        {
            // Create a copy of the possibility and fill the cells representing this offset.
            CellState[] possibilityCopy = Arrays.copyOf(possibility, possibility.length);
            for (int i = 0; i < firstCount; i++)
            {
                possibilityCopy[offset + i] = CellState.FILLED;
            }

            if (remainingCounts.isEmpty())
            {
                // End of line, add to the list.
                possibilities.add(possibilityCopy);
            }
            else
            {
                // Recurse.
                int newStartingOffset = offset + firstCount + 1;
                collectPossibleLayouts(possibilityCopy, newStartingOffset, remainingCounts);
            }
        }
    }

    /**
     * Performs one iteration of solving.
     *
     * @return <code>true</code> if there is still work to be done.
     */
    public boolean step()
    {
        // Step 1. Go through the possibilities and remove the ones which can no longer possibly apply.
        Iterator<CellState[]> iter = possibilities.iterator();
        while (iter.hasNext())
        {
            CellState[] possibility = iter.next();

            for (int i = 0; i < cells.size(); i++)
            {
                CellState state = cells.get(i).getState();
                if (state != null && possibility[i] != state)
                {
                    iter.remove();
                    break;
                }
            }
        }

        if (possibilities.size() == 0)
        {
            throw new IllegalStateException("Puzzle is unsolveable");
        }

        // Step 2. Go through each index and if all possibilities have the same value,
        //         set that value in the cells.

        for (int i = 0; i < cells.size(); i++)
        {
            CellState state = possibilities.get(0)[i]; // non-null by definition.

            for (int j = 1; j < possibilities.size(); j++)
            {
                if (possibilities.get(j)[i] != state)
                {
                    state = null;
                    break;
                }
            }

            if (state != null)
            {
                cells.get(i).setState(state);
            }
        }

        return possibilities.size() > 1;
    }
}
