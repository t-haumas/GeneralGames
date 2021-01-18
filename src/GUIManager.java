import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GUIManager
{
    private Game game;
    private GameManager gameManager;
    private JTextPane linesPane;
    private JFrame mainFrame;

    private JEditorPane winPositionsText;

    public GUIManager(Game game, GameManager gameManager)
    {
        this.game = game;
        this.gameManager = gameManager;
    }

    public void createWinPositionsWindow()
    {
        mainFrame = new JFrame(game.getName().substring(0,1).toUpperCase() + game.getName().substring(1) + " Game-ending Lines");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel winPositionsTextPanel = new JPanel();
        winPositionsTextPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        winPositionsTextPanel.setBackground(UIManager.getColor("Panel.background"));
//        winPositionsTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane winPositionsScrollPane = new JScrollPane(winPositionsTextPanel);
        winPositionsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        winPositionsScrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        winPositionsScrollPane.setBackground(UIManager.getColor("Panel.background"));
        winPositionsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        //winPositionsScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainFrame.getContentPane().add(winPositionsScrollPane);
        winPositionsText = new JEditorPane();
        winPositionsTextPanel.add(winPositionsText);
        winPositionsText.setEditable(false);
        winPositionsText.setContentType("text/html");
        //winPositionsText.setAlignmentX(Component.LEFT_ALIGNMENT);
        winPositionsText.setBackground(UIManager.getColor("Panel.background"));

        mainFrame.pack();
        mainFrame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        mainFrame.setVisible(true);
    }

    public void updateWinPositionsDisplay(TreeMap<String, GameInfo> winPositions)
    {
        System.out.println("updating");
        StringBuilder winPositionsTextBuilder = new StringBuilder("<html><head><style>");
//        for (int i = 1; i <= game.getNumPlayers(); i++)
//        {
//            winPositionsTextBuilder.append(".player").append(i).append("{color:rgb(");
//            winPositionsTextBuilder.append(getRValue(i)).append(",").append(getGValue(i)).append(",").append(getBValue(i));
//            winPositionsTextBuilder.append(")} ");
//        }
        winPositionsTextBuilder.append("</style></head><body><p>");
        ArrayList<ArrayList<String>> playersDeterminedLines = new ArrayList<>();
        for (int i = 1; i <= game.getNumPlayers(); i++)
        {
            playersDeterminedLines.add(new ArrayList<>());
        }
        for (Map.Entry<String, GameInfo> entry : winPositions.entrySet())
        {
            if ((entry.getKey().indexOf(game.getMoveString()) == 0 || game.getMoveString().indexOf(entry.getKey()) == 0) && entry.getKey().length() - game.getMoveString().length() < 15)
            {
                playersDeterminedLines.get(entry.getValue().getWinningPlayer() - 1).add(getFormattedMoveString(entry.getKey(), game.getMoveString()));
            }
        }
        for (int i = 0; i < playersDeterminedLines.size(); i++)
        {
            winPositionsTextBuilder.append("<br>Winning lines for player ").append(i + 1).append(":<br>");
            for (String determinedLine : playersDeterminedLines.get(i))
            {
                boolean yesAdd = true;
//                for (String checkingLine : playersDeterminedLines.get(i))
//                {
//                    if (checkingLine.contains(determinedLine) && ! checkingLine.equals(determinedLine))
//                    {
//                        yesAdd = false;
//                        break;
//                    }
//                }
                if (yesAdd)
                    winPositionsTextBuilder.append(determinedLine).append("<br>");
            }
        }
        winPositionsTextBuilder.append("</p></body>");

        try { SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                winPositionsText.setText(winPositionsTextBuilder.toString());
                winPositionsText.setCaretPosition(0);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        }); }
        catch (Exception e)
        {
            System.err.println("SwingUtilitiesIssue!");
        }
        System.out.println("finished");
    }

    public void showLines()
    {
        JFrame gameInfoDisplayFrame = new JFrame(game.getName() + " info");
//        JPanel infoPanel = new JPanel();
//        infoPanel.setLayout(new BorderLayout());

        linesPane = new JTextPane();
        linesPane.setBackground(UIManager.getColor("Panel.background"));
        linesPane.setEditable(false);
        setUpJTextPane(linesPane);
        JPanel linesPanel = new JPanel();
        linesPanel.add(linesPane);

        JScrollPane linesScrollPane = new JScrollPane(linesPanel);
        linesScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        linesScrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        linesScrollPane.setBackground(UIManager.getColor("Panel.background"));

//        infoPanel.add(linesScrollPane, BorderLayout.CENTER);
        gameInfoDisplayFrame.add(linesScrollPane);

        gameInfoDisplayFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        gameInfoDisplayFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                /*
                Do something special here?
                 */
                e.getWindow().dispose();
            }
        });

        gameInfoDisplayFrame.pack();
        gameInfoDisplayFrame.setSize(new Dimension(600, 400));
        gameInfoDisplayFrame.setVisible(true);
    }

    private void setUpJTextPane(JTextPane textPane)
    {
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        textPane.setEditorKit(htmlEditorKit);

        Document doc = htmlEditorKit.createDefaultDocument();
        textPane.setDocument(doc);

        StyleSheet styleSheet = htmlEditorKit.getStyleSheet();
        styleSheet.addRule(".player1 {color:rgb(200, 0, 0); }");
        styleSheet.addRule(".player2 {color:rgb(125, 125, 0); }");
        styleSheet.addRule(".player3 {color:rgb(0,200,0);}");
        styleSheet.addRule(".player4 {color:rgb(0,125,125);}");
        styleSheet.addRule(".player5 {color:rgb(0,0,200);}");
        styleSheet.addRule(".player6 {color:rgb(125,0,125);}");
    }

    public void setGame(Game g)
    {
        game = g;
    }

    private String getFormattedMoveString(String longMoveString, String currentMoveString)
    {
        StringBuilder output = new StringBuilder();
        output.append("<em>").append(currentMoveString).append("</em>");
        if (longMoveString.equals(currentMoveString))
        {
            System.out.println("same");
            return output.toString();
        }
        else {
            String nextMove = "";
            try {
                nextMove = longMoveString.substring(currentMoveString.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                nextMove = "";
            }
            try {
                nextMove = nextMove.substring(0, nextMove.indexOf(","));
            } catch (IndexOutOfBoundsException e) {
                // It's already as long as possible.
            }

            output.append(",<strong>").append(nextMove).append("</strong>").append(longMoveString.substring(currentMoveString.length() + 1 + nextMove.length()));
            return output.toString();
        }
    }

    private String getScoresString(int[] scores)
    {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < scores.length; i++)
        {
            if (isMaxScore(scores, i + 1) || isMaxTieScore(scores, i + 1))
            {
                output.append("<span class=\"player").append(Integer.toString(i + 1)).append("\">").append(scores[i]);
                output.append("</span>, ");
            }
            else
            {
                output.append(scores[i]).append(", ");
            }
        }
        output.delete(output.length() - 2, output.length() - 1);
        // Could add margin stuff here.
        output.append(" - ");
        return output.toString();
    }

    private boolean isMaxTieScore(int[] scores, int isTiePlayer)
    {
        int maxScore = -1;
        ArrayList<Integer> maxPlayers = new ArrayList<>();
        boolean tie = false;
        for (int i = 0; i < scores.length; i++)
        {
            if (scores[i] == maxScore)
            {
                tie = true;
                maxPlayers.add(i);
            }
            if (scores[i] > maxScore)
            {
                tie = false;
                maxScore = scores[i];
                maxPlayers.clear();
                maxPlayers.add(i);
            }
        }
        return maxPlayers.contains(isTiePlayer - 1) && tie;
    }

    private boolean isMaxScore(int[] scores, int isMaxPlayer)
    {
        int maxScore = -1;
        int maxPlayer = -1;
        boolean tie = false;
        for (int i = 0; i < scores.length; i++)
        {
            if (scores[i] == maxScore)
            {
                tie = true;
            }
            if (scores[i] > maxScore)
            {
                maxScore = scores[i];
                maxPlayer = i;
                tie = false;
            }
        }
        return isMaxPlayer - 1 == maxPlayer && ! tie;
    }

    public void closeWindows()
    {
        mainFrame.dispose();
    }
}