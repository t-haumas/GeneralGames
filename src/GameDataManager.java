import java.util.*;

public class GameDataManager
{
    private final TreeMap<Integer, Minimaxer> engines;
    private GameManager mainManager;
    private final TreeMap<String, GameInfo> knownDeterminedLines;
    private int numDiscoveredLines;
    private ArrayList<MinimaxResult> valuations;

    public GameDataManager(GameManager mainManager)
    {
        this.mainManager = mainManager;
        knownDeterminedLines = new TreeMap<>();
        engines = new TreeMap<>();
        valuations = new ArrayList<>();
    }

    public TreeMap<String,GameInfo> getDeterminedLinesTreeMap()
    {
        return knownDeterminedLines;
    }

    public void createEngines(int smallestDepth, int largestDepth)
    {
        if (smallestDepth > largestDepth || smallestDepth < 1)
        {
            throw new RuntimeException("Invalid depths specified in createEngines.");
        }
        for (int currentDepth = smallestDepth; currentDepth <= largestDepth; currentDepth++)
        {
            getEngine(currentDepth);
        }
    }

    public boolean knowMyOutcome(Game game, int playerNumber)
    {
        if (knownDeterminedLines.containsKey(game.getMoveString()))
        {
            return knownDeterminedLines.get(game.getMoveString()).getWinningPlayer() == playerNumber;
        }
        return false;
    }

    public GameInfo getOutcome(Game game)
    {
        return knownDeterminedLines.get(game.getMoveString());
    }

    public void saveDeterminedLine(String moveString, int winningPlayer, int winningPlayersScore)
    {
        if (! knownDeterminedLines.containsKey(moveString))
        {
            knownDeterminedLines.put(moveString, new GameInfo(winningPlayer, winningPlayersScore));
            increaseNumDiscovered();
        }
    }

    public MinimaxResult getRecommendedMoveAndInfo(Game game, int depth)
    {
        valuations = new ArrayList<>(getEngine(depth).getMoveValuations(game));
        return Collections.max(valuations);
    }

    private Minimaxer getEngine(int depth)
    {
        if (! engines.containsKey(depth))
        {
            engines.put(depth, new Minimaxer(depth, this));
        }
        return engines.get(depth);
    }

    public void increaseNumDiscovered()
    {
        numDiscoveredLines++;
    }

    public int getNumDiscovered()
    {
        return numDiscoveredLines;
    }

    public void resetNumDiscovered()
    {
        numDiscoveredLines = 0;
    }

    public LinkedList<String> getLinesList(String beginning, int maxLength)
    {
        LinkedList<String> shortLines = new LinkedList<>();
        for (Map.Entry<String, GameInfo> entry : knownDeterminedLines.entrySet())
        {
            String movestring = entry.getKey();
            if (movestring.indexOf(beginning) == 0 && movestring.length() <= maxLength)
            {
                shortLines.add(movestring);
            }
        }

        return shortLines;
    }

    public ArrayList<MinimaxResult> getValuations() {
        return valuations;
    }
}
