import javax.swing.*;

public class GameManager
{
    private Game game;

    private FileManager fileManager;
    private GameDataManager gameDataManager;
    private GamePlayManager gamePlayManager;
    private GUIManager guiManager;
    private boolean initialized;

    public GameManager(Game game)
    {
        this.game = game;
        gameDataManager = new GameDataManager(this);
        fileManager = new FileManager(game.getName(), gameDataManager, this);
        gamePlayManager = new GamePlayManager(game, fileManager, gameDataManager);
        guiManager = new GUIManager(game, this);
        initialized = false;
    }

    public void initialize()
    {
        long startTime = System.currentTimeMillis();
        IOManager.output("Initializing...");
        fileManager.initialize();
        gamePlayManager.initialize();
        initialized = true;
        IOManager.output("Done! (" + (System.currentTimeMillis() - startTime)/100/10.0 + "s)");
    }

    public int getNumPlayers()
    {
        return game.getNumPlayers();
    }

    public void playGame()
    {
        if (! initialized)
        {
            JOptionPane.showMessageDialog(null,
                    "You cannot start the game before the game manager has been initialized.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            gamePlayManager.start();
            fileManager.updateDeterminedLines();
            IOManager.output(gameDataManager.getNumDiscovered() + " new determined lines discovered.");
        }
    }

    public void setPlayers(String playersString)
    {
        fileManager.setPlayers(playersString);
        gamePlayManager.setPlayers(fileManager.getPlayers());
    }

    public void restartGame()
    {
        game.restart();
        gameDataManager.resetNumDiscovered();
    }

    public void printShortLines(String beginning, int maxLength)
    {
        for (String s : gameDataManager.getLinesList(beginning, maxLength))
        {
            System.out.println(s);
        }
    }
}
