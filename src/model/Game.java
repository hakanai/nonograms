package nonograms.model;

import java.util.Arrays;

/**
 * TODO: Document this file.
 */
public class Game
{
    private Field field;
    private Rule[] rowRules;
    private Rule[] columnRules;

    public Game(Field field, Rule[] rowRules, Rule[] columnRules)
    {
        if (rowRules.length != field.getRowCount())
        {
            throw new IllegalArgumentException("Wrong number of row rules, need " + field.getRowCount());
        }

        if (columnRules.length != field.getColumnCount())
        {
            throw new IllegalArgumentException("Wrong number of column rules, need " + field.getColumnCount());
        }

        this.field = field;
        this.rowRules = Arrays.copyOf(rowRules, rowRules.length);
        this.columnRules = Arrays.copyOf(columnRules, columnRules.length);
    }

    public Field getField()
    {
        return field;
    }

    public Rule[] getColumnRules()
    {
        return columnRules;
    }

    public Rule[] getRowRules()
    {
        return rowRules;
    }
}
