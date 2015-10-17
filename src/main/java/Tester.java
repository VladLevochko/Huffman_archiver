import java.io.IOException;

/**
 * Created by vlad on 17.10.15.
 */
public class Tester {
    public static void main(String args[]){
        Huffman huffman = new Huffman();

        String prefixTest = "/home/vlad/IdeaProjects/TIK_Lab_2_Huffman/tests/";
        String prefixRes = "/home/vlad/IdeaProjects/TIK_Lab_2_Huffman/tests_result/";
        String prefixBack = "/home/vlad/IdeaProjects/TIK_Lab_2_Huffman/tests_decomp/";
/*
        try{
            huffman.compress(prefixTest + "TEST1.DAT", prefixRes + "test1.cmp");
            huffman.compress(prefixTest + "TEST1M.DAT", prefixRes + "test1m.cmp");
            huffman.compress(prefixTest + "TEST2.DAT", prefixRes + "test2.cmp");
            //huffman.compress(prefixTest + "TEST3.DAT", prefixRes + "test3.cmp");
            huffman.compress(prefixTest + "TEST4.DAT", prefixRes + "test4.cmp");
            huffman.compress(prefixTest + "TEST5.DAT", prefixRes + "test5.cmp");
            huffman.compress(prefixTest + "TEST6_20.DAT", prefixRes + "test6_20.cmp");
            huffman.compress(prefixTest + "test20b.dat", prefixRes + "test20b.cmp");
        } catch(IOException e){
            e.printStackTrace();
        }
*/
        try{
            huffman.uncompress(prefixRes + "test1.cmp", prefixBack + "test1.dat");
            huffman.uncompress(prefixRes + "test1m.cmp", prefixBack + "test1m.dat");
            huffman.uncompress(prefixRes + "test2.cmp", prefixBack + "test2.dat");
            huffman.uncompress(prefixRes + "test4.cmp", prefixBack + "test4.dat");
            huffman.uncompress(prefixRes + "test5.cmp", prefixBack + "test5.dat");
            huffman.uncompress(prefixRes + "test6_20.cmp", prefixBack + "test6.dat");
            huffman.uncompress(prefixRes + "test20b.cmp", prefixBack + "test20b.dat");
        } catch(IOException e){
            e.printStackTrace();
        }

    }
}
