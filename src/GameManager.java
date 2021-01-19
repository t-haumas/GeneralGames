import javax.swing.*;

public class GameManager
{
    private Game game;

    private final FileManager fileManager;
    private final GameDataManager gameDataManager;
    private final GamePlayManager gamePlayManager;
    private final GUIManager guiManager;
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

    public void initialize(IOMethod displayMethod)
    {
//        IOManager.setInputMethod(displayMethod);
        IOManager.setOutputMethod(displayMethod);
        IOManager.setGuiManager(guiManager);

        long startTime = System.currentTimeMillis();
        if (displayMethod == IOMethod.GUI)
            guiManager.initialize();
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
        if (! initialized) {
            JOptionPane.showMessageDialog(null,
                    "You cannot set the players before the game manager has been initialized.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            fileManager.setPlayers(playersString);
            gamePlayManager.setPlayers(fileManager.getPlayers());
        }
    }

    public void restartGame()
    {
        if (! initialized)
        {
            JOptionPane.showMessageDialog(null,
                    "You cannot restart the game before the game manager has been initialized.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            game.restart();
            gameDataManager.resetNumDiscovered();
        }
    }

    public void outputShortLines(String beginning, int maxLength)
    {
        for (String s : gameDataManager.getLinesList(beginning, maxLength))
        {
            IOManager.output(s);
        }
    }
}
