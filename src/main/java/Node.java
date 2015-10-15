public class Node implements Comparable <Node>{
    Node left;
    Node right;
    int freq;
    char ch;

    public Node(char ch, int freq){
        this.freq = freq;
        this.ch = ch;
    }

    public Node(int freq, Node left, Node right){
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf(){
        return left == null && right == null;
    }

    @Override
    public int compareTo(Node that) {
        return this.freq - that.freq;
    }

    @Override
    public String toString() {
        return "Node{" +
                "ch=" + ch +
                ", freq=" + freq +
                ", right=" + right +
                ", left=" + left +
                '}';
    }
}
