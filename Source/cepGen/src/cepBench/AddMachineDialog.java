package cepBench;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddMachineDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField machineIpField;
    private JTextField machinePortField;
    private JTextField machineNameField;
    private JCheckBox splitFilesByEventCheckBox;

    public AddMachineDialog(ArrayList<Machine> machineList) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Machine tempMachine = new Machine();
                tempMachine.name = machineNameField.getText();
                tempMachine.ip = machineIpField.getText();
                tempMachine.port = Integer.parseInt(machinePortField.getText());
                if(splitFilesByEventCheckBox.isSelected()){
                    tempMachine.eventFiles = "split";
                }
                else{
                    tempMachine.eventFiles = "normal";
                }
                machineList.add(tempMachine);
                onOK();
            }
        });

    }
    public AddMachineDialog(ArrayList<Machine> machineList,int index){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        Machine inputMachine = new Machine();
        System.out.println(index);
        System.out.println(machineList.size());
        inputMachine = machineList.get(index);
        machineNameField.setText(inputMachine.name);
        machineIpField.setText(inputMachine.ip);
        machinePortField.setText(String.valueOf(inputMachine.port));
        if(inputMachine.eventFiles.equals("normal")){
            splitFilesByEventCheckBox.setSelected(false);
        }
        else if (inputMachine.eventFiles.equals("split")) {
            splitFilesByEventCheckBox.setSelected(true);
        }
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Machine tempMachine = new Machine();
                tempMachine.name = machineNameField.getText();
                tempMachine.ip = machineIpField.getText();
                tempMachine.port = Integer.parseInt(machinePortField.getText());
                if(splitFilesByEventCheckBox.isSelected()){
                    tempMachine.eventFiles = "split";
                }
                else{
                    tempMachine.eventFiles = "normal";
                }
                machineList.set(index,tempMachine);
                onOK();
            }
        });
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        AddMachineDialog dialog = new AddMachineDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//
//    }
}
