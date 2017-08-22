package cepBench;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mohammadreza on 8/15/2016.
 */
public class mainForm extends JFrame{

    private JPanel main;
    private JTextField portField;
    private JComboBox serializerCombo;
    private JButton browseButton;
    private JTextField saveDirField;
    private JRadioButton TCPRadioButton;
    private JRadioButton UDPRadioButton;
    private JRadioButton saveEventsRadioButton;
    private JButton startServerButton;
    private JTextField eventLimitField;
    private JLabel statusLabel;
    String currentDirectory ="";


    public mainForm() {


        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser(".\\");
                // Demonstrate "Open" dialog:
                c.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                int rVal = c.showDialog(mainForm.this,"Select as config folder");
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    currentDirectory = c.getSelectedFile().getAbsolutePath();
                    saveDirField.setText(currentDirectory);

                }
                else {
                    statusLabel.setText("Config folder selection cancelled.");
                }
            }
        });
        UDPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TCPRadioButton.setSelected(!UDPRadioButton.isSelected());
            }
        });
        TCPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UDPRadioButton.setSelected(!TCPRadioButton.isSelected());
            }
        });
        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port =Integer.parseInt( portField.getText());
                int eventLimit = Integer.parseInt(eventLimitField.getText());
                String serializer = serializerCombo.getSelectedItem().toString();
                boolean saveEvents = saveEventsRadioButton.isSelected();
                if(UDPRadioButton.isSelected()){
                    UDPServer us = new UDPServer(serializer,port,eventLimit,currentDirectory,saveEvents);
                    us.run();
                }
                else if(TCPRadioButton.isSelected()){
                    TCPServer ts = new TCPServer(serializer,port,eventLimit,currentDirectory,saveEvents);
                    ts.run();
                }
            }
        });

        saveEventsRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButton.setEnabled(saveEventsRadioButton.isSelected());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("mainForm");
        frame.setContentPane(new mainForm().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("Event File Player");
        frame.setVisible(true);

    }
}
