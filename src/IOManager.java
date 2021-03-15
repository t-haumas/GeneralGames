import javax.swing.*;
import java.util.Scanner;

public class IOManager
{
    private static final Scanner keyboardInput = new Scanner(System.in);
    private static IOMethod inputMethod = IOMethod.SYSTEM;
    private static IOMethod outputMethod = IOMethod.SYSTEM;
    private static GUIManager guiManager;

    private static String getKeyboardInput()
    {
        return keyboardInput.nextLine();
    }

    private static String getGUIMoveInput()
    {
        guiManager.waitForMove();
        while (guiManager.waitingForMoveSubmission) {try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}}
        return guiManager.getMove();
    }

    public static void setInputMethod(IOMethod method)
    {
        inputMethod = method;
    }

    public static void setOutputMethod(IOMethod method)
    {
        outputMethod = method;
    }

    public static String getNextInput()
    {
        if (inputMethod == IOMethod.SYSTEM)
        {
            return getKeyboardInput();
        }
        else
        {
            return getGUIMoveInput();
        }
    }

    public static void output(String outputString, OutputType outputType)
    {
        if (outputMethod == IOMethod.SYSTEM)
        {
            if (outputType == OutputType.MOVE_OPTIONS) {
                System.out.println("Valid moves include: " + outputString);
            } else if (outputType == OutputType.WHOSE_TURN) {
                System.out.println(outputString + ", make a move.\n");
            }
            System.out.println(outputString + ".");
            System.out.print("What move would you like to make? ");
        }
        else if (outputMethod == IOMethod.GUI)
        {
            SwingUtilities.invokeLater(() -> guiManager.display(outputString, outputType));
        }
    }

    public static void output(String outputString)
    {
        output(outputString, OutputType.MESSAGE);
    }

    public static void setGuiManager(GUIManager guiManager)
    {
        IOManager.guiManager = guiManager;
    }
}

enum IOMethod
{
    GUI, SYSTEM
}

enum OutputType
{
    MESSAGE, GAMESTATE, MOVE_OPTIONS, WHOSE_TURN
}