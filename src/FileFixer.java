import java.io.*;
import java.util.Scanner;

public class FileFixer
{
    public static void main(String[] args)
    {
        File myFile = new File("Forceable-Wins_capture-mode_Mancala.txt");
        try {
            FileWriter f = new FileWriter("AnotherNew.txt");
            BufferedReader br = new BufferedReader(new FileReader("Forceable-Wins_capture-mode_Mancala.txt"));
            String line;
            while((line=br.readLine())!=null)
            {
                String front = line.substring(0, line.indexOf(';'));
                String middle = line.substring(line.indexOf(';') + 1);
                String back = middle.substring(middle.indexOf(";") + 1);
                String player = middle.substring(0, middle.indexOf(";"));
                int relativeScore = Integer.parseInt(back);
                int winningPlayer = Integer.parseInt(player);
                int p1Score;
                int p2Score;
                if (winningPlayer == 1)
                {
                    p1Score = (relativeScore + 48) / 2;
                    p2Score = 48 - p1Score;
                }
                else
                {
                    p2Score = (relativeScore = 48) / 2;
                    p1Score = 48 - p2Score;
                }
                System.out.println(front + ";" + player + ";" + p1Score + "," + p2Score);
                f.write(front + ";" + player + ";" + p1Score + "," + p2Score + "\n");
//                System.out.println(player + ", " + back);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
