package nonograms.solver;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

import nonograms.model.Game;
import nonograms.model.Field;
import nonograms.model.Rule;

/**
 * TODO: Document this file.
 */
public class GameSolver
{
    private Field field;

    private List<RuleSolver> ruleSolvers = new ArrayList<RuleSolver>();

    public GameSolver(Game game)
    {
        field = game.getField();

        Rule[] rowRules = game.getRowRules();
        for (int i = 0; i < rowRules.length; i++)
        {
            ruleSolvers.add(new RuleSolver(field.viewRow(i), rowRules[i]));
        }

        Rule[] columnRules = game.getColumnRules();
        for (int i = 0; i < columnRules.length; i++)
        {
            ruleSolvers.add(new RuleSolver(field.viewColumn(i), columnRules[i]));
        }
    }

    /**
     *
     * @return {@code true} if there is still work to be done.
     */
    public boolean step()
    {
        // Safety in case the caller doesn't notice the return value of the last iteration.
        if (ruleSolvers.isEmpty())
        {
            return false;
        }

        System.out.print("Running rule solvers ");
        Iterator<RuleSolver> iter = ruleSolvers.iterator();
        while (iter.hasNext())
        {
            RuleSolver ruleSolver = iter.next();
            if (!ruleSolver.step())
            {
                System.out.print("$");
                iter.remove();
            }
            else
            {
                System.out.print(".");
            }
        }
        System.out.println();

        System.out.println("New field state:");
        System.out.println(field);

        return !ruleSolvers.isEmpty();
    }
}
