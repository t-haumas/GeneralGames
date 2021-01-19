import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
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

    private static String getGUITextInput()
    {
        throw new UnsupportedOperationException("getGUITextInput not implemented in IOManager class.");
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
            return getGUITextInput();
        }
    }

    public static void output(String outputString, OutputType outputType)
    {
        if (outputMethod == IOMethod.SYSTEM)
        {
            System.out.println(outputString);
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
    MESSAGE, GAMESTATE, MOVE_OPTIONS
}