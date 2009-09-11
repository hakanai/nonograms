package nonograms.solver;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import nonograms.model.Game;
import nonograms.view.FieldView;
import nonograms.io.PuzzleNonogramsWeb;

/**
 * TODO: Document this file.
 */
public class Main
{
    public static void main(String[] args)
    {
        Game game;
        try
        {
            game = PuzzleNonogramsWeb.createGame(new File(System.getProperty("user.home"), "Desktop/nonograms.html"));
        }
        catch (IOException e)
        {
            // Barf.
            System.err.println("Error reading from file");
            return;
        }

        JFrame frame = new JFrame("Nonograms");
        frame.setLayout(new BorderLayout());
        frame.add(new FieldView(game.getField()), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        GameSolver solver = new GameSolver(game);
        int i = 1;
        while (solver.step())
        {
            i++;
        }

        System.out.println("Completed in " + i + " steps");
    }
}
