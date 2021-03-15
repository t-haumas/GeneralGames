import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

public class GUIManager {
    private final Game game;
    private final GameManager gameManager;
    public boolean waitingForMoveSubmission;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel moveOptionsPanel;
    private JScrollPane messages;
    private JEditorPane messagesTextPane;
    private JComboBox<String> moveOptions;
    private JLabel statusLabel;
    private JButton moveButton;

    private JScrollPane gameStateScrollPane;

    private boolean mainDisplayCreated;
    private final Color backgroundColor;
    private boolean initialized;

    public GUIManager(Game game, GameManager gameManager) {
        this.game = game;
        this.gameManager = gameManager;
        mainDisplayCreated = false;
        backgroundColor = UIManager.getColor("Panel.background");
        initialized = false;
        waitingForMoveSubmission = true;
    }

    public void initialize() {
        createMainDisplay();
        setUpMessages();

        gameStateScrollPane = new JScrollPane(game.getPanelRepresentingThisGame());
        gameStateScrollPane.setBorder(BorderFactory.createEmptyBorder());
        gameStateScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        gameStateScrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        gameStateScrollPane.setBackground(backgroundColor);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.fill = GridBagConstraints.BOTH;

        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(gameStateScrollPane, constraints);

        constraints.weightx = 2;
        constraints.gridx = 1;
        mainPanel.add(messages, constraints);

        updateGameStateScrollPane();

        moveOptionsPanel = new JPanel();
        moveOptionsPanel.setLayout(new BoxLayout(moveOptionsPanel, BoxLayout.X_AXIS));
        moveOptionsPanel.setBackground(new Color(200, 220, 250));
        moveOptions = new JComboBox<>();
        moveOptions.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    makeMove();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        moveOptionsPanel.add(moveOptions);
        moveButton = new JButton("Make move");
        moveButton.addActionListener(e -> makeMove());
        moveOptionsPanel.add(moveButton);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        statusLabel.setText("I'm supposed to say whose turn it is!");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.gridheight = 2;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(gameStateScrollPane, constraints);

        constraints.weightx = 0.3;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        mainPanel.add(statusLabel, constraints);

        constraints.gridy = 1;
        mainPanel.add(moveOptionsPanel, constraints);

        constraints.gridx = 2;
        constraints.weightx = 0.00001;
        constraints.weighty = 0.1;
        mainPanel.add(Box.createVerticalStrut((int) (game.getPanelRepresentingThisGame().getPreferredSize().getHeight() * 1.25)), constraints);

        constraints.weighty = 3;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        mainPanel.add(messages, constraints);


        initialized = true;
    }

    private void makeMove() {
        waitingForMoveSubmission = false;
    }

    private void updateGameStateScrollPane() {
        gameStateScrollPane.setViewportView(game.getPanelRepresentingThisGame());
    }

    private void setUpMessages() {
        messagesTextPane = new JEditorPane();
        messagesTextPane.setEditable(false);
        messagesTextPane.setBackground(backgroundColor);
        String defaultFont = new JLabel().getFont().getFontName();
        messagesTextPane.setFont(new Font(defaultFont, Font.PLAIN, 16));

        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BorderLayout());
        messagesPanel.add(messagesTextPane, BorderLayout.CENTER);
        messagesPanel.setBackground(backgroundColor);

        messages = new JScrollPane(messagesPanel);
        messages.setBorder(BorderFactory.createEmptyBorder());
        messages.getVerticalScrollBar().setUnitIncrement(10);
        messages.getHorizontalScrollBar().setUnitIncrement(10);
        messages.setBackground(backgroundColor);
    }

    private void createMainDisplay() {
        if (!mainDisplayCreated) {
            mainFrame = new JFrame(game.getName());
            Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
            mainFrame.setPreferredSize(new Dimension(screenDimension.width / 2, screenDimension.height / 2));

            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());

            mainFrame.getContentPane().add(mainPanel);
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            mainFrame.pack();
            mainFrame.setVisible(true);
            mainDisplayCreated = true;
        } else {
            mainFrame.removeAll();
            mainPanel.removeAll();
            mainFrame.getContentPane().add(mainPanel);
        }
    }

    public void display(String outputString, OutputType outputType) {
        if (initialized)
        {
            if (outputType == OutputType.MESSAGE)
            {
                messagesTextPane.setText(messagesTextPane.getText() + "\n" + outputString);
            }
            else if (outputType == OutputType.GAMESTATE)
            {
                updateGameStateScrollPane();
            }
            else if (outputType == OutputType.MOVE_OPTIONS) {
                updateMoveOptions(outputString);
            } else if (outputType == OutputType.WHOSE_TURN){
                statusLabel.setText(outputString + "'s turn");
            } else {
                throw new UnsupportedOperationException("output type " + outputType + " not supported yet.");
            }
            mainPanel.revalidate();
            mainFrame.revalidate();
            mainPanel.repaint();
            mainFrame.repaint();
        }
        else
        {
            throw new IllegalStateException("GUI has not been initialized yet!");
        }
    }

    private void updateMoveOptions(String optionsString) {
        moveOptions.removeAllItems();
        Scanner optionsScanner = new Scanner(optionsString);
        optionsScanner.useDelimiter(", ");
        while (optionsScanner.hasNext()) {
            moveOptions.addItem(optionsScanner.next());
        }
    }

    public String getMove() {
        return (String)moveOptions.getSelectedItem();
    }

    public void waitForMove() {
        waitingForMoveSubmission = true;
        moveOptions.requestFocusInWindow();
    }
}

//
//public class GUIManager
//{
//    private Game game;
//    private GameManager gameManager;
//    private JTextPane linesPane;
//    private JFrame mainFrame;
//
//    private JEditorPane winPositionsText;
//
//    public GUIManager(Game game, GameManager gameManager)
//    {
//        this.game = game;
//        this.gameManager = gameManager;
//    }
//
//    public void createWinPositionsWindow()
//    {
//        mainFrame = new JFrame(game.getName().substring(0,1).toUpperCase() + game.getName().substring(1) + " Game-ending Lines");
//        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
//        JPanel winPositionsTextPanel = new JPanel();
//        winPositionsTextPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        winPositionsTextPanel.setBackground(UIManager.getColor("Panel.background"));
////        winPositionsTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        JScrollPane winPositionsScrollPane = new JScrollPane(winPositionsTextPanel);
//        winPositionsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
//        winPositionsScrollPane.getHorizontalScrollBar().setUnitIncrement(10);
//        winPositionsScrollPane.setBackground(UIManager.getColor("Panel.background"));
//        winPositionsScrollPane.setBorder(BorderFactory.createEmptyBorder());
//        //winPositionsScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        mainFrame.getContentPane().add(winPositionsScrollPane);
//        winPositionsText = new JEditorPane();
//        winPositionsTextPanel.add(winPositionsText);
//        winPositionsText.setEditable(false);
//        winPositionsText.setContentType("text/html");
//        //winPositionsText.setAlignmentX(Component.LEFT_ALIGNMENT);
//        winPositionsText.setBackground(UIManager.getColor("Panel.background"));
//
//        mainFrame.pack();
//        mainFrame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
//        mainFrame.setVisible(true);
//    }
//
//    public void updateWinPositionsDisplay(TreeMap<String, GameInfo> winPositions)
//    {
//        System.out.println("updating");
//        StringBuilder winPositionsTextBuilder = new StringBuilder("<html><head><style>");
////        for (int i = 1; i <= game.getNumPlayers(); i++)
////        {
////            winPositionsTextBuilder.append(".player").append(i).append("{color:rgb(");
////            winPositionsTextBuilder.append(getRValue(i)).append(",").append(getGValue(i)).append(",").append(getBValue(i));
////            winPositionsTextBuilder.append(")} ");
////        }
//        winPositionsTextBuilder.append("</style></head><body><p>");
//        ArrayList<ArrayList<String>> playersDeterminedLines = new ArrayList<>();
//        for (int i = 1; i <= game.getNumPlayers(); i++)
//        {
//            playersDeterminedLines.add(new ArrayList<>());
//        }
//        for (Map.Entry<String, GameInfo> entry : winPositions.entrySet())
//        {
//            if ((entry.getKey().indexOf(game.getMoveString()) == 0 || game.getMoveString().indexOf(entry.getKey()) == 0) && entry.getKey().length() - game.getMoveString().length() < 15)
//            {
//                playersDeterminedLines.get(entry.getValue().getWinningPlayer() - 1).add(getFormattedMoveString(entry.getKey(), game.getMoveString()));
//            }
//        }
//        for (int i = 0; i < playersDeterminedLines.size(); i++)
//        {
//            winPositionsTextBuilder.append("<br>Winning lines for player ").append(i + 1).append(":<br>");
//            for (String determinedLine : playersDeterminedLines.get(i))
//            {
//                boolean yesAdd = true;
////                for (String checkingLine : playersDeterminedLines.get(i))
////                {
////                    if (checkingLine.contains(determinedLine) && ! checkingLine.equals(determinedLine))
////                    {
////                        yesAdd = false;
////                        break;
////                    }
////                }
//                if (yesAdd)
//                    winPositionsTextBuilder.append(determinedLine).append("<br>");
//            }
//        }
//        winPositionsTextBuilder.append("</p></body>");
//
//        try { SwingUtilities.invokeAndWait(new Runnable() {
//            @Override
//            public void run() {
//                winPositionsText.setText(winPositionsTextBuilder.toString());
//                winPositionsText.setCaretPosition(0);
//                mainFrame.revalidate();
//                mainFrame.repaint();
//            }
//        }); }
//        catch (Exception e)
//        {
//            System.err.println("SwingUtilitiesIssue!");
//        }
//        System.out.println("finished");
//    }
//
//    public void showLines()
//    {
//        JFrame gameInfoDisplayFrame = new JFrame(game.getName() + " info");
////        JPanel infoPanel = new JPanel();
////        infoPanel.setLayout(new BorderLayout());
//
//        linesPane = new JTextPane();
//        linesPane.setBackground(UIManager.getColor("Panel.background"));
//        linesPane.setEditable(false);
//        setUpJTextPane(linesPane);
//        JPanel linesPanel = new JPanel();
//        linesPanel.add(linesPane);
//
//        JScrollPane linesScrollPane = new JScrollPane(linesPanel);
//        linesScrollPane.getVerticalScrollBar().setUnitIncrement(10);
//        linesScrollPane.getHorizontalScrollBar().setUnitIncrement(10);
//        linesScrollPane.setBackground(UIManager.getColor("Panel.background"));
//
////        infoPanel.add(linesScrollPane, BorderLayout.CENTER);
//        gameInfoDisplayFrame.add(linesScrollPane);
//
//        gameInfoDisplayFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//        gameInfoDisplayFrame.addWindowListener(new WindowAdapter()
//        {
//            @Override
//            public void windowClosing(WindowEvent e)
//            {
//                /*
//                Do something special here?
//                 */
//                e.getWindow().dispose();
//            }
//        });
//
//        gameInfoDisplayFrame.pack();
//        gameInfoDisplayFrame.setSize(new Dimension(600, 400));
//        gameInfoDisplayFrame.setVisible(true);
//    }
//
//    private void setUpJTextPane(JTextPane textPane)
//    {
//        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
//        textPane.setEditorKit(htmlEditorKit);
//
//        Document doc = htmlEditorKit.createDefaultDocument();
//        textPane.setDocument(doc);
//
//        StyleSheet styleSheet = htmlEditorKit.getStyleSheet();
//        styleSheet.addRule(".player1 {color:rgb(200, 0, 0); }");
//        styleSheet.addRule(".player2 {color:rgb(125, 125, 0); }");
//        styleSheet.addRule(".player3 {color:rgb(0,200,0);}");
//        styleSheet.addRule(".player4 {color:rgb(0,125,125);}");
//        styleSheet.addRule(".player5 {color:rgb(0,0,200);}");
//        styleSheet.addRule(".player6 {color:rgb(125,0,125);}");
//    }
//
//    public void setGame(Game g)
//    {
//        game = g;
//    }
//
//    private String getFormattedMoveString(String longMoveString, String currentMoveString)
//    {
//        StringBuilder output = new StringBuilder();
//        output.append("<em>").append(currentMoveString).append("</em>");
//        if (longMoveString.equals(currentMoveString))
//        {
//            System.out.println("same");
//            return output.toString();
//        }
//        else {
//            String nextMove = "";
//            try {
//                nextMove = longMoveString.substring(currentMoveString.length() + 1);
//            } catch (StringIndexOutOfBoundsException e) {
//                nextMove = "";
//            }
//            try {
//                nextMove = nextMove.substring(0, nextMove.indexOf(","));
//            } catch (IndexOutOfBoundsException e) {
//                // It's already as long as possible.
//            }
//
//            output.append(",<strong>").append(nextMove).append("</strong>").append(longMoveString.substring(currentMoveString.length() + 1 + nextMove.length()));
//            return output.toString();
//        }
//    }
//
//    private String getScoresString(int[] scores)
//    {
//        StringBuilder output = new StringBuilder();
//        for (int i = 0; i < scores.length; i++)
//        {
//            if (isMaxScore(scores, i + 1) || isMaxTieScore(scores, i + 1))
//            {
//                output.append("<span class=\"player").append(Integer.toString(i + 1)).append("\">").append(scores[i]);
//                output.append("</span>, ");
//            }
//            else
//            {
//                output.append(scores[i]).append(", ");
//            }
//        }
//        output.delete(output.length() - 2, output.length() - 1);
//        // Could add margin stuff here.
//        output.append(" - ");
//        return output.toString();
//    }
//
//    private boolean isMaxTieScore(int[] scores, int isTiePlayer)
//    {
//        int maxScore = -1;
//        ArrayList<Integer> maxPlayers = new ArrayList<>();
//        boolean tie = false;
//        for (int i = 0; i < scores.length; i++)
//        {
//            if (scores[i] == maxScore)
//            {
//                tie = true;
//                maxPlayers.add(i);
//            }
//            if (scores[i] > maxScore)
//            {
//                tie = false;
//                maxScore = scores[i];
//                maxPlayers.clear();
//                maxPlayers.add(i);
//            }
//        }
//        return maxPlayers.contains(isTiePlayer - 1) && tie;
//    }
//
//    private boolean isMaxScore(int[] scores, int isMaxPlayer)
//    {
//        int maxScore = -1;
//        int maxPlayer = -1;
//        boolean tie = false;
//        for (int i = 0; i < scores.length; i++)
//        {
//            if (scores[i] == maxScore)
//            {
//                tie = true;
//            }
//            if (scores[i] > maxScore)
//            {
//                maxScore = scores[i];
//                maxPlayer = i;
//                tie = false;
//            }
//        }
//        return isMaxPlayer - 1 == maxPlayer && ! tie;
//    }
//
//    public void closeWindows()
//    {
//        mainFrame.dispose();
//    }
//
//    public void display(String outputString, OutputType outputType) {
//    }
//}
