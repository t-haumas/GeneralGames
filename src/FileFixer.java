import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileFixer
{
    public static void main(String[] args)
    {
        File myFile = new File("Mancala lines.txt");
        try {
            Scanner fileScanner = new Scanner(myFile);
            while (fileScanner.hasNextLine())
            {
                String currentString = fileScanner.nextLine();
                String front = currentString.substring(0, currentString.indexOf(';'));
                String middle = currentString.substring(currentString.indexOf(';') + 1);
                int score = Integer.parseInt(middle.substring(0,middle.indexOf(';')));
                int p1 = (score + 48) / 2;
                int p2 = 48 - p1;
                String newMiddle = p1 + "," + p2;
                String back = middle.substring(middle.indexOf(';'));
                StringBuilder newfront = new StringBuilder();
                while (front.length() > 0)
                {
                    newfront.append(front.substring(0, 2)).append(",");
                    front = front.substring(2, front.length());
                }
                newfront.deleteCharAt(newfront.length() - 1);
                System.out.println(newfront + ";" + newMiddle + back);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
