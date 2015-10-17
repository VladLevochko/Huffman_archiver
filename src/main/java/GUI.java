import javax.swing.*;
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
                statusLabelCmp.setText("Please wait. File in processing...");
                try {

                    boolean status = hf.compress(inputFile, outputFile);
                    if (status)
                        statusLabelCmp.setText("File is compressed!");
                    inputFile = null;
                    outputFile = null;
                    inputFileLabelCmp.setText("");
                    outputFileLabelCmp.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

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
                statusLabelUncmp.setText("Please wait. File in processing...");
                try {
                    boolean status = hf.uncompress(inputFile, outputFile);
                    if (status)
                        statusLabelUncmp.setText("File is uncompressed!");
                    inputFile = null;
                    outputFile = null;
                    inputFileLabelUncmp.setText("");
                    outputFileLabelUncmp.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
                    outputFile = selectedFile.getAbsolutePath() + outName;
                }

                labelSelected.setText(selectedFile.getName());
                labelName.setText(outName);

            } else {
                labelSelected.setText("Nothing selected...");
                inputFile = null;
                outputFile = null;
            }
        }
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.createUIComponents();
    }

}
