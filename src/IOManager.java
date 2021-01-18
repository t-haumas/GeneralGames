import java.util.Scanner;

public class IOManager
{
    private static final Scanner keyboardInput = new Scanner(System.in);
    private static IOMethod inputMethod = IOMethod.SYSTEM_IN;
    private static IOMethod outputMethod = IOMethod.SYSTEM_OUT;

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
        if (inputMethod == IOMethod.SYSTEM_IN)
        {
            return getKeyboardInput();
        }
        else
        {
            return getGUITextInput();
        }
    }

    public static void output(String outputString)
    {
        if (outputMethod == IOMethod.SYSTEM_OUT)
        {
            System.out.println(outputString);
        }
        else
        {
            throw new UnsupportedOperationException("not sysout output not implemented in IOManager class.");
        }
    }
}

enum IOMethod
{
    SYSTEM_IN, GUI, SYSTEM_OUT;
}