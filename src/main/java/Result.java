import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Result extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;

    private JLabel inFileEnthLabel;
    private JLabel outFileEnthLabel;
    private JLabel bitPerSymbolLabel;
    private JLabel timeLabel;

    public Result() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
// add your code here
        dispose();
    }

    public void showDialog(ResultObject res){
        inFileEnthLabel.setText(Double.toString(res.getEnthropyIn()));
        outFileEnthLabel.setText(Double.toString(res.getEnthropyOut()));
        bitPerSymbolLabel.setText(Double.toString(res.getBitPerSymbol()) + " bit/symb");
        timeLabel.setText(Double.toString(res.getTime()) + "s");
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        Result dialog = new Result();
        ResultObject res = new ResultObject(7.5888, 7.999, 15, 0.32);
        dialog.showDialog(res);
    }
}
