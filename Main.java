import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws FileNotFoundException, IOException {
        Scanner in = new Scanner(System.in);
        String filename = "/Users/ha.bmc/Desktop/cs206/A9/Image/src/prinzipal.ppm";
        Pixel[][] colors = PPM.readPPM(filename);
        System.out.println("Row:" + colors.length);
        System.out.println("Col:" + colors[0].length);

        QuadTree tree = new QuadTree(colors);
        tree.split();
        PPM.writePPM("newest.ppm", colors);
    }
}
