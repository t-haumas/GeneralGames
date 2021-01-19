import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class FileManager
{
    private final String configFilePath;
    private final String winsFilePath;
    private final GameManager mainManager;
    private final GameDataManager dataManager;

    private boolean askForPlayerTypes;

    boolean outputGameAndInfo;
    boolean outputMovestring;
    boolean playSoundAfterMoves;
    boolean showEngineRecommendations;
    private boolean showWinPositionsGUI;
    int engineDepth;

    private ArrayList<Player> players;

    public FileManager(Game game, GameDataManager dataManager, GameManager mainManager)
    {
        String gameString = game.getNumPlayers() + "-player_" + game.getName();
        this.dataManager = dataManager;
        this.mainManager = mainManager;
        this.configFilePath = "Game Files/GamePlayerConfig.txt";
        this.winsFilePath = "Game Files/Determined Lines/Determined-Lines_" + gameString.replace(' ', '_') + ".txt";

        initializeValues();

        players = new ArrayList<>();
    }

    private void initializeValues()
    {
        askForPlayerTypes = true;
        outputGameAndInfo = true;
        outputMovestring = false;
        playSoundAfterMoves = false;
        showEngineRecommendations = false;
        showWinPositionsGUI = false;
        engineDepth = 1;

        players = new ArrayList<>();
    }

    //<editor-fold desc="File management methods">
    private void getForceableWinPositionsFromFile()
    {
        File forceableWinsFile = new File(winsFilePath);
        try
        {
            Scanner winsFileScanner = new Scanner(forceableWinsFile);
            Scanner lineScanner;
            while (winsFileScanner.hasNextLine())
            {
                lineScanner = new Scanner(winsFileScanner.nextLine());
                lineScanner.useDelimiter(";");
                String knownEndgameMovestring = lineScanner.next();
                int knownEndgameWinningPlayer = lineScanner.nextInt();
                int knownEndgameWinningScore = lineScanner.nextInt();
                dataManager.saveDeterminedLine(knownEndgameMovestring, knownEndgameWinningPlayer, knownEndgameWinningScore);
            }
            dataManager.resetNumDiscovered();
        }
        catch (FileNotFoundException e)
        {
            createForceableWinsFile();
        }
    }

    private void createForceableWinsFile()
    {
        try
        {
            File forceableWinsFile = new File(winsFilePath);
            // Maybe can get rid of this.
            if (! forceableWinsFile.createNewFile())
                throw new RuntimeException("Forceable wins file was not able to be created!");
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "A new forceable wins file was not able to be created.",
                    "File error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateForceableWinsFile()
    {
        clearForceableWinsFile();
        writeToWinsFile();
    }

    private void writeToWinsFile()
    {
        try
        {
            FileWriter winsFileWriter = new FileWriter(winsFilePath);
            TreeMap<String, GameInfo> determinedLines = dataManager.getDeterminedLinesTreeMap();

            for (Map.Entry<String, GameInfo> entry : determinedLines.entrySet()) {
                String movestring = entry.getKey();
                int winningPlayer = entry.getValue().getWinningPlayer();
                int winningScore = entry.getValue().getWinningPlayersScore();
                winsFileWriter.write(movestring + ";" + winningPlayer + ";" + winningScore + "\n");
            }
            winsFileWriter.close();
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Issue", "Could not write to wins file!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForceableWinsFile()
    {
        File winsFile = new File(winsFilePath);
        if (winsFile.delete()) {
            createForceableWinsFile();
        }
        else
            throw new RuntimeException("Forceable wins file could not be deleted!");
    }

    private void createConfigFile()
    {
        try
        {
            File newConfigFile = new File(configFilePath);
            // Maybe can get rid of this.
            if (! newConfigFile.createNewFile())
                throw new RuntimeException("Config file was not able to be created!");
        }
        catch (IOException e)
        {
            System.err.println("An new config file was not able to be created.");
            e.printStackTrace();
        }
    }

    private void clearConfigFile()
    {
        File configFile = new File(configFilePath);
        if (configFile.delete()) {
            createConfigFile();
        }
        else
            throw new RuntimeException("Config file could not be deleted!");
    }

    private void writeToConfigFile()
    {
        try
        {

            FileWriter configFileWriter = new FileWriter(configFilePath);
            configFileWriter.write("***Use lowercase true and false. Example for player configuration: minimax, 6, human, Tom***\n");
            configFileWriter.write("Ask for types and names of players: " + askForPlayerTypes + "\n");
            configFileWriter.write("Print the game and info as it is played: " + outputGameAndInfo + "\n");
            configFileWriter.write("Print movestring after games are finished: " + outputMovestring + "\n");
            configFileWriter.write("Play a sound after moves are made: " + playSoundAfterMoves + "\n");
            configFileWriter.write("Show engine recommendations: " + showEngineRecommendations + "\n");
            configFileWriter.write("Engine depth: " + engineDepth + "\n");
            configFileWriter.write("Show lines helper during games: " + showWinPositionsGUI + "\n");
            StringBuilder playersStringBuilder = new StringBuilder();
            for (Player player : players)
            {
                playersStringBuilder.append(player.getRawName()).append(", ").append(player.getRawInfo()).append(", ");
            }
            String playersString = playersStringBuilder.toString();
            if (players.size() > 0)
                playersString = playersString.substring(0, playersString.length() - 2);
            configFileWriter.write("Player configuration: " + playersString + "\n");

            configFileWriter.close();
        }
        catch (IOException e)
        {
            System.err.println("Unable to write closing file.");
//			System.exit(1);
        }
    }

    private void updateConfigFile()
    {
        clearConfigFile();
        writeToConfigFile();
    }

    private void readAndApplyConfig()
    {
        File configFile = new File(configFilePath);
        //TODO: make option to print confidence. "think" vs "know" Px will lose/win.

        try {
            //	Set up scanners.
            Scanner fileScan = new Scanner(configFile);
            fileScan.nextLine();

            //	Extract askForPlayerTypes
            Scanner lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(": ");
            lineScan.next();
            askForPlayerTypes = lineScan.nextBoolean();

            //	Extract printGameAndInfo
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(": ");
            lineScan.next();
            outputGameAndInfo = lineScan.nextBoolean();

            //	Extract printMovestring
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(": ");
            lineScan.next();
            outputMovestring = lineScan.nextBoolean();

            //	Extract playSoundAfterMoves
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(": ");
            lineScan.next();
            playSoundAfterMoves = lineScan.nextBoolean();

            //	Extract showEngineRecommendations
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(": ");
            lineScan.next();
            showEngineRecommendations = lineScan.nextBoolean();

            //	Extract engineDepth
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(": ");
            lineScan.next();
            engineDepth = lineScan.nextInt();

            //	Extract showWinPositionsGUI
            lineScan = new Scanner(fileScan.nextLine());
            lineScan.useDelimiter(": ");
            lineScan.next();
            showWinPositionsGUI = lineScan.nextBoolean();

            //	Extract players
            lineScan = new Scanner(fileScan.nextLine());
            if (!askForPlayerTypes) {
                lineScan.useDelimiter(": ");
                lineScan.next();
                String playersString = lineScan.next();
                setPlayers(playersString);
            }

            //	Extract more?
        }
        catch (NoSuchElementException e)
        {
            JOptionPane.showMessageDialog(null, "" +
                            "Config file is improperly formatted (" + e.getMessage() + ").\nCreating a new config file now.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            updateConfigFile();
        }
        catch (FileNotFoundException e)
        {
            createConfigFile();
            writeToConfigFile();
        }
    }
    //</editor-fold>

    public void initialize()
    {
        boolean mkdirs = new File("./Game Files/Determined Lines").mkdirs();
        readAndApplyConfig();
        getForceableWinPositionsFromFile();

        if (askForPlayerTypes)
        {
            players = new ArrayList<>();
            askForPlayerTypes(mainManager.getNumPlayers());
        }
        updateConfigFile();
    }

    private void askForPlayerTypes(int numberOfPlayers)
    {
        boolean valid;
        String input;
        for (int i = 1; i <= numberOfPlayers; i++)
        {
            valid = false;
            while (! valid)
            {
                IOManager.output("What kind of player is player " + i + "? ");
                input = IOManager.getNextInput();
                if (input.equalsIgnoreCase("human") || input.equalsIgnoreCase("h"))
                {
                    System.out.print("What is player " + i + "'s name? ");
                    input = IOManager.getNextInput();
                    players.add(new HumanPlayer(input));
                    valid = true;
                }
                else if (input.equalsIgnoreCase("minimax"))
                {
                    int depth = engineDepth;
                    boolean validInt = false;
                    while (! validInt) {
                        System.out.print("What is player " + i + "'s maximum depth? ");
                        input = IOManager.getNextInput();
                        try {
                            depth = Integer.parseInt(input);
                            validInt = true;
                        } catch (InputMismatchException e) {
                            System.err.println("You have to put an integer.");
                        }
                    }
                    players.add(new SafeOptimalAI(depth, dataManager));
                    valid = true;
                }
                else
                {
                    System.err.println("Invalid input.");
                }
            }
        }
    }


    // Little dangerous...
    public void deleteExtraneousForceableWinPositions()
    {
        ArrayList<String> removingKeys = new ArrayList<>();
        TreeMap<String, GameInfo> determinedLines = dataManager.getDeterminedLinesTreeMap();

        for (Map.Entry<String, GameInfo> entry : determinedLines.entrySet()) {
            String movestring = entry.getKey();
            for (int i = 1; i < movestring.length(); i++) {
                if (determinedLines.containsKey(movestring.substring(0, movestring.length() - i))) {
                    removingKeys.add(movestring);
                    break;
                }
            }
        }
        for (String removingKey : removingKeys)
        {
            determinedLines.remove(removingKey);
        }
        updateForceableWinsFile();
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(String playersString)
    {
        players.clear();
        Scanner playersParser = new Scanner(playersString);
        playersParser.useDelimiter(", ");

        int playersCreated = 0;
        while (playersParser.hasNext()) {
            String playerString = playersParser.next();
            if (playerString.equalsIgnoreCase("human") || playerString.equalsIgnoreCase("h"))
            {
                players.add(new HumanPlayer(playersParser.next()));
                playersCreated++;
            }
            else if (playerString.equals("minimax"))
            {
                players.add(new SafeOptimalAI(playersParser.nextInt(), dataManager));
                playersCreated++;
            }
            else
            {
                JOptionPane.showMessageDialog(null,
                        "Player configuration contains invalid token(s): " + playersString,
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }

    public void updateDeterminedLines()
    {
        updateForceableWinsFile();
    }
}
