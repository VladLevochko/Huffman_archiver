import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GUI{

    private JLabel logoLabel;
    private JLabel statusLabelCmp;
    private JLabel statusLabelUncmp;
    private JLabel inputFileLabelCmp;
    private JLabel inputFileLabelUncmp;
    private JLabel outputFileLabelCmp;
    private JLabel outputFileLabelUncmp;

    private JButton chooseFileButtonCmp;
    private JButton chooseFileButtonUncmp;
    private JButton OKButtonCmp;
    private JButton OKButtonUncmp;

    private JPanel rootPanel;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;

    private JFileChooser fileChooser;

    private String inputFile;
    private String outputFile;

    public GUI() {

    }


    private void createUIComponents() {
        // TODO: place custom component creation code here

        JFrame frame = new JFrame("GUI");
        frame.setContentPane(this.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**compress----------------------------------------------------------------------------compress*/

        chooseFileButtonCmp.addActionListener(new SelectFile());
        OKButtonCmp.addActionListener(e -> {
            Huffman hf = new Huffman();

            if (inputFile == null)
                statusLabelCmp.setText("Select file!");
            else {
                System.out.println(inputFile + " " + outputFile);
                try {

                    ResultObject res = hf.compress(inputFile, outputFile);
                    Result result = new Result();
                    result.showDialog(res);
                    resetFiles();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NullPointerException e1){
                    statusLabelCmp.setText("File is empty!");
                    resetFiles();
                }
                statusLabelCmp.setText("File compressed");

            }

        });

        /**uncompress--------------------------------------------------------------------------uncompress*/

        chooseFileButtonUncmp.addActionListener(new SelectFile());
        OKButtonUncmp.addActionListener(e -> {
            Huffman hf = new Huffman();

            if (inputFile == null)
                statusLabelUncmp.setText("Select file!");
            else {
                System.out.println(inputFile + " " + outputFile);
                try {
                     ResultObject res = hf.uncompress(inputFile, outputFile);
                    Result result = new Result();
                    result.showDialog(res);
                    resetFiles();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                statusLabelUncmp.setText("File uncompressed");
            }

        });

        frame.pack();
        frame.setVisible(true);
    }

    public class SelectFile implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JLabel labelSelected;
            JLabel labelName;
            if (e.getSource() == chooseFileButtonCmp){
                labelSelected = inputFileLabelCmp;
                labelName = outputFileLabelCmp;
            }
            else {
                labelSelected = inputFileLabelUncmp;
                labelName = outputFileLabelUncmp;
            }

            fileChooser = new JFileChooser();

            if (e.getSource() == chooseFileButtonUncmp){
                FileFilter ff = new FileNameExtensionFilter("Compressed files", "cmp");
                fileChooser.setFileFilter(ff);
            }
            int returnedValue = fileChooser.showOpenDialog(rootPanel);
            if (returnedValue == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();

                inputFile = selectedFile.getAbsolutePath();

                String outName;
                if (e.getSource() == chooseFileButtonCmp){
                    outName = selectedFile.getName() + ".cmp";
                    outputFile = selectedFile.getAbsolutePath() + ".cmp";
                }

                else {
                    outName = selectedFile.getName().substring(0, selectedFile.getName().length() - 4);
                    outputFile = selectedFile.getAbsolutePath().substring(0, selectedFile.getAbsolutePath().length() - 4);
                }

                labelSelected.setText(selectedFile.getName());
                labelName.setText(outName);

            } else {
                labelSelected.setText("Nothing selected...");
                resetFiles();
            }
        }
    }

    public void resetFiles(){
        inputFile = null;
        outputFile = null;
        inputFileLabelCmp.setText("");
        inputFileLabelUncmp.setText("");
        outputFileLabelCmp.setText("");
        outputFileLabelUncmp.setText("");
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.createUIComponents();
    }

}
