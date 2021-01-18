import java.util.Arrays;

public class GameLine
{
    private final int[] scores;
    private final String movestring;
    private final String info;

    public GameLine(String movestring, int[] scores, String info)
    {
        this.movestring = movestring;
        this.info = info;
        this.scores = scores;
    }

    public int[] getScores() {
        return scores;
    }

    public String getMovestring() {
        return movestring;
    }

    public String getInfo() {
        return info;
    }

    public String toString()
    {
        return movestring + ";" + Arrays.toString(scores) + ";" + info;
    }
}
