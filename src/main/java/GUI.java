import javax.swing.*;

/**
 * Created by vlad on 14.10.15.
 */
public class GUI {
    private JLabel logoLabel;
    private JButton chooseFileButton;
    private JLabel statusLabel;
    private JTextField textField;
    private JPanel rootPanel;
    private JPanel panel1;

    public GUI() {
        /*JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);*/
    }

    public static void main(String[] args) {
       /* GUI g = new GUI();*/

        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
