import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class exmpl {
    public static void main(String args[]) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(new File("/home/vlad/IdeaProjects/TIK_Lab_2_Huffman/text.txt")));
        for (int i = 0; i < 256; i++)
            out.write((char) i);
        out.close();
    }
}
