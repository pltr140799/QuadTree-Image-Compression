import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class PPM {
    public static Pixel[][] readPPM(String file) throws FileNotFoundException {
        Scanner input = new Scanner(new File(file));
        String format = input.next(); // the PPM format

        int columns = input.nextInt();
        int row = input.nextInt();

        int max = input.nextInt();

        Pixel[][] colors = new Pixel[row][columns];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < columns; j++) {
                colors[i][j] = new Pixel(input.nextInt(),input.nextInt(),input.nextInt());
            }
        }
        return colors;
    }
    public static void writePPM(String filename, Pixel[][] colors) throws IOException {
        PrintWriter out = new PrintWriter(filename);
        out.print("P3 ");
        out.println(colors[0].length + " " + colors.length + " 255");
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[0].length; j++) {
                out.print(colors[i][j].getRed() + " " + colors[i][j].getGreen() + " " + colors[i][j].getBlue() + " ");
            }
            out.println();
        }
        out.close();
    }

    public static void main(String args[]) throws FileNotFoundException, IOException{
        Scanner in = new Scanner(System.in);
        String filename = in.next();
        Pixel[][] colors = readPPM(filename);
        writePPM("new.ppm", colors);
    }
}