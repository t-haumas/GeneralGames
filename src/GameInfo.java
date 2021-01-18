/**
 * Who's winning in this position, in which we know one player can force a win.
 */
public class GameInfo
{
    private final int winningPlayer;
    private final int winningPlayersScore;

    public GameInfo(int winningPlayer, int winningPlayersScore)
    {
        this.winningPlayer = winningPlayer;
        this.winningPlayersScore = winningPlayersScore;
    }

    public int getWinningPlayer()
    {
        return winningPlayer;
    }

    public int getWinningPlayersScore()
    {
        return winningPlayersScore;
    }
}
