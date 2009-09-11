package nonograms.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import nonograms.model.Game;
import nonograms.model.Rule;
import nonograms.model.Field;

import com.somewhere.HtmlUtils;
import com.somewhere.CharSetUtils;

import org.htmlparser.util.NodeList;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;


/**
 * TODO: Document this file.
 */
public class PuzzleNonogramsWeb
{
    public static void main(String[] args) throws Exception
    {
        Game game = createGame(new File(System.getProperty("user.home"), "Desktop/nonograms.html"));
    }

    public static Game createGame(File htmlFile) throws IOException
    {
        NodeList top = HtmlUtils.parse(htmlFile, CharSetUtils.UTF_8);

        NodeList table = top.extractAllNodesThatMatch(new HasAttributeFilter("id", "NonogramsTable"), true);

        NodeList rows = table.extractAllNodesThatMatch(new TagNameFilter("tr"), true);

        // Retrieve the row rules.
        int rowCount = rows.size() - 1;
        Rule[] rowRules = new Rule[rowCount];
        for (int row = 0; row < rowCount; row++)
        {
            NodeList ruleSpans = rows.elementAt(row + 1).getChildren().elementAt(0).getChildren();
            List<Integer> counts = extractCounts(ruleSpans);
            rowRules[row] = new Rule(row, counts);
        }

        // Retrieve the column rules.
        NodeList firstRow = rows.elementAt(0).getChildren();
        int columnCount = firstRow.size() - 1;
        Rule[] columnRules = new Rule[columnCount];
        for (int col = 0; col < columnCount; col++)
        {
            NodeList ruleSpans = firstRow.elementAt(col + 1).getChildren();
            List<Integer> counts = extractCounts(ruleSpans);
            columnRules[col] = new Rule(col, counts);
        }

        return new Game(new Field(rowCount, columnCount), rowRules, columnRules);
    }

    private static List<Integer> extractCounts(NodeList ruleSpans)
    {
        ruleSpans = ruleSpans.extractAllNodesThatMatch(new TagNameFilter("span"));

        List<Integer> counts = new ArrayList<Integer>();
        for (int i = 0; i < ruleSpans.size(); i++)
        {
            String str = ruleSpans.elementAt(i).toPlainTextString();
            str = str.replaceAll("&nbsp;", "");
            counts.add(Integer.parseInt(str));
        }
        return counts;
    }


}
