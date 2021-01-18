
public abstract class Player
{
    /**
     * Asks this player for the move they want to make for the game being played.
     *
     * @param playingGame The game being played
     * @return The move (as an integer) that this player wants to make.
     */
    public abstract int getMove(Game playingGame);

    /**
     * Not the actual name of the player, the String representation of the type of this player.
     * @return The name of the type of this player.
     */
    public abstract String getName();

    /**
     * Just some extra identifying info about this player (like their name, or their depth or something).
     * @return Extra info about the player necessary to identify them.
     */
    public abstract String getInfo();

    /**
     * Similar to getName, but it has to be one of the options for the string representation of this player
     * that GamePlayManager expects. This is for file writing and reading purposes.
     * @return The literal name of this type of player.
     */
    public abstract String getRawName();

    /**
     * Similar to getInfo, but without any fluff. This is for file writing and reading purposes.
     * @return a string containing any raw info related to this player.
     */
    public abstract String getRawInfo();

    @Override
    public String toString()
    {
        return getName() + ", " + getInfo();
    }
}

