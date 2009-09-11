package nonograms.model;

import java.util.List;

/**
 * TODO: Document this file.
 */
public class Rule
{
    private int index;
    private List<Integer> counts;

    public Rule(int index, List<Integer> counts)
    {
        this.index = index;
        this.counts = counts;
    }

    public int getIndex()
    {
        return index;
    }

    public List<Integer> getCounts()
    {
        return counts;
    }
}