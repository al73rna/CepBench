package cepBench;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mohammadreza on 8/13/2016.
 */
public class mainForm extends JFrame{
    private JButton browseButton;
    private JTextField confFolderField;
    private JButton sendEventFilesButton;
    private JPanel main;
    private JLabel statusLabel;
    private JTable machineJTable;
    private JTextField rateField;
    private JList eventFileJList;
    private JRadioButton TCPRadioButton;
    private JRadioButton UDPRadioButton;
    private JCheckBox customRatesCheckBox;
    private String currentDirectory;
    private ArrayList<MachineP> machineList;
    private MachineP currentMachine;
    private ArrayList<String> eventFiles;
    ArrayList<Long> rates ;
    public mainForm() {
        eventFiles = new ArrayList<>();
        machineList = new ArrayList<>();
        rates = new ArrayList<>();
        String[] machineListColomnName = {"name","ip","port","split by type"};
        DefaultTableModel machineTabelModel = new DefaultTableModel();
        machineTabelModel.setColumnIdentifiers(machineListColomnName);
        machineJTable.setModel(machineTabelModel);

        DefaultListModel eventFileListModel = new DefaultListModel();
        eventFileJList.setModel(eventFileListModel);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser(".\\");
                // Demonstrate "Open" dialog:
                c.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                int rVal = c.showDialog(mainForm.this,"Select as config folder");
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    currentDirectory = c.getSelectedFile().getAbsolutePath();
                    confFolderField.setText(currentDirectory);
                    MachineinfoReader mir = new MachineinfoReader();
                    mir.readMachinesInfo(machineList,currentDirectory+"\\"+"machineinfo.xml");
                    updateMachineTable(machineTabelModel);




                }
                else {
                    statusLabel.setText("Config folder selection cancelled.");
                }

            }
        });

        machineJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(machineJTable.getSelectedRow()!= -1){
                    currentMachine = machineList.get(machineJTable.getSelectedRow());
                    System.out.print(currentMachine.name);

                    eventFiles.clear();
                    File folder = new File(currentDirectory);
                    File[] listOfEventFiles = folder.listFiles();


                        for (int i = 0; i < listOfEventFiles.length; i++) {
                            if (listOfEventFiles[i].isFile()) {
                                if (listOfEventFiles[i].getName().startsWith(currentMachine.name)) {
                                    eventFiles.add(listOfEventFiles[i].getName());
                                }
                            }
                        }

                    updateEventFileList(eventFileListModel);
                }
                else {
                    statusLabel.setText("Please select a machine first.");
                }
            }
        });



        sendEventFilesButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ExecutorService threadpool = Executors.newFixedThreadPool(eventFiles.size());
                        ArrayList<UDPFileSender> tmpUDPs = new ArrayList<UDPFileSender>();
                        ArrayList<TCPFileSender> tmpTCPs = new ArrayList<TCPFileSender>();
                        if (UDPRadioButton.isSelected()) {

                            for (String efName : eventFiles) {
                                if (!customRatesCheckBox.isSelected()) {
                                    EventFile ef = new EventFile(currentDirectory + "\\" + efName);
                                    UDPFileSender fs = new UDPFileSender(currentMachine, Long.parseLong(rateField.getText()), ef);
                                    tmpUDPs.add(fs);
                                }else {
                                    EventFile ef = new EventFile(currentDirectory + "\\" + efName);
                                    UDPFileSender fs = new UDPFileSender(currentMachine, Long.parseLong(JOptionPane.showInputDialog(mainForm.this, "Enter the rate for : "+efName,rateField.getText() )), ef);
                                    tmpUDPs.add(fs);
                                }

                            }

                            for (UDPFileSender udpfs : tmpUDPs){
                                threadpool.submit(udpfs);
                            }

                        } else if (TCPRadioButton.isSelected()) {

                            for (String efName : eventFiles) {
                                if (!customRatesCheckBox.isSelected()) {
                                    EventFile ef = new EventFile(currentDirectory + "\\" + efName);
                                    TCPFileSender fs = new TCPFileSender(currentMachine, Long.parseLong(rateField.getText()), ef);
                                    tmpTCPs.add(fs);

                                } else {
                                    EventFile ef = new EventFile(currentDirectory + "\\" + efName);
                                    TCPFileSender fs = new TCPFileSender(currentMachine, Long.parseLong(JOptionPane.showInputDialog(mainForm.this, "Enter the rate for : "+efName,rateField.getText() )), ef);
                                    tmpTCPs.add(fs);
                                }
                            }

                            for (TCPFileSender tcpfs : tmpTCPs){
                                threadpool.submit(tcpfs);
                            }

                        }
                    }
                }
        );
        UDPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TCPRadioButton.setSelected(false);
            }
        });
        TCPRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UDPRadioButton.setSelected(false);
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

    private void updateMachineTable(DefaultTableModel tm){

        if (machineList.size()>0) {
            ArrayList<String> tmpRow = new ArrayList<String>();
            while(tm.getRowCount() > 0)
            {
                tm.removeRow(0);
            }
            for(MachineP m : machineList){
                tmpRow.add(m.name);
                tmpRow.add(m.ip);
                tmpRow.add(Integer.toString(m.port));
                if(m.eventFiles.equals("normal"))
                    tmpRow.add("False");
                else
                    tmpRow.add("True");

                tm.addRow(tmpRow.toArray());

                tmpRow.clear();

            }

        }

    }
    private void updateEventFileList(DefaultListModel lm){
        if(eventFiles.size()>0){
            lm.clear();
            for (String s : eventFiles){
                lm.addElement(s);
            }
        }
    }



}
