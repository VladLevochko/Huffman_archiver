import java.io.*;
import java.util.*;

public class Huffman {
    private static String fileIn = "";
    private static String fileOut = "";
    final private static int SIZE = 256;
    static private int[] symbolsFreq = new int[SIZE];
    static private double[] chance = new double[SIZE];
    static private String[] newCode = new String[SIZE];
    static private char[] oldCode = new char[SIZE];
    static private int dataSize;

    public Huffman(){}

    public ResultObject compress(String inputFilePath, String outFileName) throws IOException{
        fileIn = inputFilePath;
        fileOut = outFileName;

        final long timeStart = System.currentTimeMillis();

        Node root;
        getData(fileIn);

        double inputFileEntropy = getEntropy();

        root = buildTree();
        buildCode(root, "");

        double bitPerSymbol = writeData(root);

        getData(fileOut);
        double outputFileEntropy = getEntropy();

        additionalInformation(true, true, root);

        double executionTime = (System.currentTimeMillis() - timeStart) / 1000;

        return new ResultObject(inputFileEntropy, outputFileEntropy, bitPerSymbol, executionTime);
    }

    private void getData(String file) throws IOException{
        System.out.print("Reading data...");

        for (int i = 0; i < SIZE; i++)
            symbolsFreq[i] = 0;
        dataSize = 0;
        BinaryIn in = new BinaryIn(file);
        char c;
        while (!in.isEmpty()){
            c = in.readChar();
            dataSize++;
            symbolsFreq[(int) c]++;
        }

        /*for (int i = 0; i < SIZE; i++)
            chance[i] = (float)symbolsFreq[i] / dataSize;*/
        System.out.println("\t\tDone!");
    }

    private double writeData(Node root) throws IOException{
        BinaryOut out = new BinaryOut(fileOut);
        writeTree(root, out);
        out.write(dataSize);
        double bitPerSybmol = rewriteData(out);
        out.close();
        return bitPerSybmol;
    }

    private double rewriteData(BinaryOut out) throws IOException{
        System.out.print("Writing data...");
        BinaryIn in = new BinaryIn(fileIn);
        char c;
        int n = 0;
        long length = 0;
        while(!in.isEmpty()){
            c = in.readChar();
            String code = newCode[c];
            length += code.length();
            n++;
            if (code.length() == 0)
                out.write(false);
            for (int j = 0; j < code.length(); j++)
                if (code.charAt(j) == '0')
                    out.write(false);
                else
                    out.write(true);
        }
        System.out.println("\t\tDone!");
        return (length / n);
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

    public ResultObject uncompress(String inputFilePath, String outputFileName) throws IOException{
        fileIn = inputFilePath;
        fileOut = outputFileName;

        final long startRecover = System.currentTimeMillis();
        System.out.println("\n Recovery...");

        BinaryIn in = new BinaryIn(fileIn);
        BinaryOut out = new BinaryOut(fileOut);

        getData(fileIn);
        double inputFileEntropy = getEntropy();

        Node recoveryTree = readTree(in);
        additionalInformation(false, true, recoveryTree);

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

        getData(fileOut);
        double outputFileEntropy = getEntropy();

        double time = (System.currentTimeMillis() - startRecover) / 1000;
        return new ResultObject(inputFileEntropy, outputFileEntropy, 8, time);
    }

    private Node readTree(BinaryIn in){
        boolean bit = in.readBoolean();
        if (bit)
            return new Node(-1, readTree(in), readTree(in));
            return new Node(in.readChar(), -1);
    }

    private double getEntropy() throws IOException{
        double entropy = 0;
        for (int i = 0; i < SIZE; i++) {
            chance[i] = (double) symbolsFreq[i] / dataSize;
            if (chance[i] != 0)
                entropy += chance[i] * (Math.log(chance[i]) / Math.log(2));
        }
        return -entropy;
    }

    private void additionalInformation(boolean saveCodes, boolean saveTree, Node root){
        File in = new File(fileIn);
        String log = in.getPath() + "LOG.txt";
        try(PrintWriter out = new PrintWriter(new File(log))){
            if (saveCodes)
                saveCodes(out);
            if (saveTree)
                printTree(root, out);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveCodes(PrintWriter out){
        for (int i = 0; i < SIZE; i++)
            oldCode[i] = (char) i;
        for (int i = 0; i < SIZE; i++)
            for (int j = i + 1; j < SIZE; j++)
                if (chance[i] > chance[j]){
                    double x = chance[i];
                    chance[i] = chance[j];
                    chance[j] = x;
                    String y = newCode[i];
                    newCode[i] = newCode[j];
                    newCode[j] = y;
                    char z = oldCode[i];
                    oldCode[i] = oldCode[j];
                    oldCode[j] = z;
                }

        for (int i = 0; i < SIZE; i++)
            if (newCode[i] != null)
                out.println(oldCode[i] + "\t" + newCode[i] + "\t" + chance[i]);
    }

    public void printTree(Node root, PrintWriter out){
        out.println("Tree:");
        int depth = depth(root, 1);
        List<List<Character>> tree = new ArrayList<>();
        for (int i = 0; i < depth; i++)
            tree.add(new ArrayList<>());
        nodes(root, tree, depth, 0);

        for (int i = 0; i < depth; i++){
            for (int j = 0; j < Math.pow(2, depth - i - 1) - 1; j++)
                out.print(" ");
            int k = 0;
            while(k < tree.get(i).size()){
                out.print(tree.get(i).get(k));
                for (int x = 0; x < Math.pow(2, depth - i) - 1; x++)
                    out.print(" ");
                k++;
            }
            out.println();
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
