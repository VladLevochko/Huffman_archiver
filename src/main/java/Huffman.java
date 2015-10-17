import java.io.*;
import java.util.*;

/**
 * /home/vlad/Downloads/wps-office_9.1.0.4975-a19p1_amd64.deb
 */

public class Huffman {
    private static String fileIn = "";
    private static String fileOut = "";
    final private static int SIZE = 256;
    static private int[] symbolsFreq = new int[SIZE];
    static private float[] chance = new float[SIZE];
    static private String[] newCode = new String[SIZE];
    static private char[] oldCode = new char[SIZE];
    static private int dataSize;

    public Huffman(){}

    public boolean compress(String inputFilePath, String outFileName) throws IOException{
        fileIn = inputFilePath;
        fileOut = outFileName;

        final long timeStart = System.currentTimeMillis();

        Huffman huffman = new Huffman();
        Node root;

        huffman.getData();

        root = huffman.buildTree();
        huffman.buildCode(root, "");

        huffman.writeData(root);

        System.out.println("\n\tExecution time: " + (float) (System.currentTimeMillis() - timeStart) / 1000 + "s");
        huffman.saveCodes();
        huffman.statistics();
        huffman.printTree(root);
        return true;
    }

    private void getData() throws IOException{
        System.out.print("Reading data...");

        for (int i = 0; i < SIZE; i++)
            symbolsFreq[i] = 0;
        dataSize = 0;
        BinaryIn in = new BinaryIn(fileIn);
        char c;
        while (!in.isEmpty()){
            c = in.readChar();
            dataSize++;
            symbolsFreq[(int) c]++;
        }

        for (int i = 0; i < SIZE; i++)
            chance[i] = (float)symbolsFreq[i] / dataSize;
        System.out.println("\t\tDone!");
    }

    private void writeData(Node root) throws IOException{
        BinaryOut out = new BinaryOut(fileOut);
        writeTree(root, out);
        out.write(dataSize);
        rewriteData(out);
        out.close();
    }

    private void rewriteData(BinaryOut out) throws IOException{
        System.out.print("Writing data...");
        BinaryIn in = new BinaryIn(fileIn);
        char c;
        while(!in.isEmpty()){
            c = in.readChar();
            String code = newCode[c];
            if (code.length() == 0)
                out.write(false);
            for (int j = 0; j < code.length(); j++)
                if (code.charAt(j) == '0')
                    out.write(false);
                else
                    out.write(true);
        }
        System.out.println("\t\tDone!");
    }

    private Node buildTree(){
        System.out.print("Building tree...");

        LinkedList<Node> nodes = new LinkedList<>();

        for (int i = 0; i < symbolsFreq.length; i++)
            if (symbolsFreq[i] != 0){
                addIn(nodes, new Node((char) i, symbolsFreq[i]));
            }

        while (nodes.size() > 1){
            Node l = nodes.poll();
            Node r = nodes.poll();

            addIn(nodes, new Node(l.freq + r.freq, l, r));
        }
        System.out.println("\tDone!");
        return nodes.poll();
    }

        private void addIn(LinkedList<Node> nodes, Node node){
            if (nodes.size() == 0) {
                nodes.add(node);
                return;
            }
            for (int i = 0; i < nodes.size(); i++)
                if (nodes.get(i).freq > node.freq) {
                    nodes.add(i, node);
                    return;
                }
            nodes.addLast(node);
        }

    private void writeTree(Node root, BinaryOut out){
        System.out.print("Writing tree...");

        writeNodes(root, out);

        System.out.println("\t\tDone!");
    }

    private void writeNodes(Node x, BinaryOut out){
        if (x.isLeaf()) {
            out.write(false);
            out.write(x.ch);
        } else {
            if (x.left != null) {
                out.write(true);
                writeNodes(x.left, out);
            }
            if (x.right != null) {
                writeNodes(x.right, out);
            }
        }
    }

    private void buildCode(Node node, String s){
        if (node.left != null) buildCode(node.left, s + "0");
        if (node.right != null) buildCode(node.right, s + "1");
        if (node.isLeaf()) newCode[node.ch] = s;
    }

    private void saveCodes(){
        for (int i = 0; i < SIZE; i++)
            oldCode[i] = (char) i;
        for (int i = 0; i < SIZE; i++)
            for (int j = i + 1; j < SIZE; j++)
                if (chance[i] > chance[j]){
                    float x = chance[i];
                    chance[i] = chance[j];
                    chance[j] = x;
                    String y = newCode[i];
                    newCode[i] = newCode[j];
                    newCode[j] = y;
                    char z = oldCode[i];
                    oldCode[i] = oldCode[j];
                    oldCode[j] = z;
                }

        try(PrintWriter out = new PrintWriter(new File("codes.txt"))){
            for (int i = 0; i < SIZE; i++)
                if (newCode[i] != null)
                    out.println(oldCode[i] + "\t" + newCode[i] + "\t" + chance[i]);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void statistics() throws IOException{
        System.out.println("\nStatistics:");
        System.out.println("Size of uncompressed file: " + dataSize + "byte");
        getData();
        float entropy = 0;
        for (int i = 0; i < SIZE; i++) {
            chance[i] = (float) symbolsFreq[i] / dataSize;
            if (chance[i] != 0)
                entropy += chance[i] * (Math.log(chance[i]) / Math.log(2));
        }

        System.out.println("\tEntropy: " + (-1) * entropy + "\n" +
                           "\tAmount of information: " + entropy * (-1) * dataSize);
    }

    public void printTree(Node root){
        System.out.println("Tree:");
        int depth = depth(root, 1);
        List<List<Character>> tree = new ArrayList<>();
        for (int i = 0; i < depth; i++)
            tree.add(new ArrayList<>());
        nodes(root, tree, depth, 0);

        for (int i = 0; i < depth; i++){
            for (int j = 0; j < Math.pow(2, depth - i - 1) - 1; j++)
                System.out.print(" ");
            int k = 0;
            while(k < tree.get(i).size()){
                System.out.print(tree.get(i).get(k));
                for (int x = 0; x < Math.pow(2, depth - i) - 1; x++)
                    System.out.print(" ");
                k++;
            }
            System.out.println();
        }
    }

    private void nodes(Node x, List<List<Character>> tree, int depth, int  i){
        if (i == depth)
            return;
        if (!x.isLeaf())
            tree.get(i).add('+');
        else
            tree.get(i).add(x.ch);
        if (i < depth - 1 && x.isLeaf())
            imagineNodes(tree, depth, i + 1, 2);
        else {
            if (x.left != null)
                nodes(x.left, tree, depth, i+1);
            else
                imagineNodes(tree, depth, i+1, 1);
            if (x.right != null)
                nodes(x.right, tree, depth, i+1);
            else
                imagineNodes(tree, depth, i+1, 1);
        }
    }

    private void imagineNodes(List<List<Character>> tree, int depth, int  i, int k){
        if (i >= depth) return;
        for (int j = 0; j < k; j++){
            List<Character> temp = tree.get(i);
            temp.add(' ');
        }
        if (i < depth - 1)
            imagineNodes(tree, depth, i+1, k*2);
    }

    private int depth(Node x, int depth){
        int l = depth, r = depth;
        if (!x.isLeaf()){
            if (x.left != null)
                l = depth(x.left, depth + 1);
            if (x.right != null)
                r = depth(x.right, depth + 1);
        }
        return Math.max(l, r);
    }


    public boolean uncompress(String inputFilePath, String outputFileName) throws IOException{
        fileIn = inputFilePath;
        fileOut = outputFileName;

        final long startRecover = System.currentTimeMillis();
        System.out.println("\n Recovery...");

        BinaryIn in = new BinaryIn(fileIn);
        BinaryOut out = new BinaryOut(fileOut);

        Node recoveryTree = readTree(in);
        printTree(recoveryTree);

        Node cur = recoveryTree;

        int bytes = in.readInt();

        while(bytes > 0){
            boolean b = in.readBoolean();
            if (!b && cur.left != null)
                cur = cur.left;
            else
                if (cur.right != null) cur = cur.right;
            if(cur.isLeaf()) {
                out.write(cur.ch);
                bytes--;
                cur = recoveryTree;
            }

        }
        out.close();
        System.out.println("\nRecovery finished at " + (float) (System.currentTimeMillis() - startRecover) / 1000 + "s");
        return true;
    }

    private Node readTree(BinaryIn in){
        boolean bit = in.readBoolean();
        if (bit)
            return new Node(-1, readTree(in), readTree(in));
            return new Node(in.readChar(), -1);
    }


    public static void main(String args[]){

        Scanner in = new Scanner(System.in);
        System.out.println("Commands:\n");
        System.out.println("\tcompress path/to/file path/to/compressedFileName");
        System.out.println("\tuncompress path/to/file path/to/uncompressedFileName");
        System.out.println("\texit");

        Huffman huffman = new Huffman();
        String line;
        boolean exit = false;
        do {
            try {
                line = in.nextLine();

                String[] tokens = line.split(" ");
                switch (tokens[0]) {
                    case "compress": {
                        String path = tokens[1];
                        String name = tokens[2];
                        try {
                            huffman.compress(path, name);
                        } catch (IOException e) {
                            System.out.println("error! invalid path...");
                        }
                        break;
                    }
                    case "uncompress": {
                        String path = tokens[1];
                        String name = tokens[2];
                        try {
                            huffman.uncompress(path, name);
                        } catch (IOException e) {
                            System.out.println("error! invalid path...");
                        }
                        break;
                    }
                    case "exit": {
                        exit = true;
                        break;
                    }
                    default:
                        System.out.println("something wrong...");
                }
            } catch (NullPointerException e){
                if (dataSize == 0)
                 System.out.println("File is empty. Nothing to compress");
            }
            catch (Exception e){
                    System.out.println(e.toString());
            }
        } while (!exit);

    }
}
