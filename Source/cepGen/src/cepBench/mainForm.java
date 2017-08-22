/**
 * Created by mohammadreza on 5/14/2016.
 */
package cepBench;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.lang.String;
import java.util.ArrayList;


public class mainForm extends JFrame{
    private JPanel main;
    private JButton closeButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton addEventButton;
    private JButton addMachineButton;
    private JComboBox timestampSquenceCombo;
    private JComboBox timestampUnitCombo;
    private JComboBox serializerCombo;
    private JTable machineTable;
    private JTable eventTypeTable;
    private JScrollPane jScrollPane23;
    private JTextField versionField;
    private JTextField serializerVersionField;
    private JButton editMachineButton;
    private JButton editEventTypeButton;
    private JButton generateEventsButton;
    private JLabel statusLabel;
    private JLabel confFolderLabel;
    private JButton selectConfigFolderButton;
    private JTextField timeLimitTextField;
    private JTextField countLimitTextField;
    private JLabel timeUnitLabel;
    private AddEventTypeDialog addEventTypeDialog;
    private AddMachineDialog addMachineDialog;
    private ArrayList<Machine> machineList;
    private ArrayList<EventType> eventTypeList;
    private String currentDirectory = "";


    public mainForm(){
        //================================================
        //Initialization and setting the form's elements.
        //================================================
        machineList = new ArrayList<Machine>();
        eventTypeList = new ArrayList<EventType>();
        String[] machineListColomnName = {"name","ip","port","split by type"};
        DefaultTableModel machineTabelModel = new DefaultTableModel();
        machineTabelModel.setColumnIdentifiers(machineListColomnName);
        machineTable.setModel(machineTabelModel);
        String[] eventTypeListColomnName = {"name","rate","dispatch type","Attribute Count","Destination Count"};
        DefaultTableModel eventTypeTabelModel = new DefaultTableModel();
        eventTypeTabelModel.setColumnIdentifiers(eventTypeListColomnName);
        eventTypeTable.setModel(eventTypeTabelModel);

        //================================================
        // Setting up the action listeners.
        //================================================

        // Display the add machine dialog
        addMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    addMachineDialog = new AddMachineDialog(machineList);
                    addMachineDialog.pack();
                    addMachineDialog.setVisible(true);
                    updateMachineTable(machineTabelModel);

            }
        });
        // Display the edit machine dialog
        editMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = machineTable.getSelectedRow();
                if (index!= -1){
                    addMachineDialog = new AddMachineDialog(machineList,index);
                    addMachineDialog.pack();
                    addMachineDialog.setVisible(true);
                    updateMachineTable(machineTabelModel);
                }

            }
        });
        // Display the add event type dialog
        addEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addEventTypeDialog = new AddEventTypeDialog(eventTypeList,machineList);
                addEventTypeDialog.pack();
                addEventTypeDialog.setVisible(true);
                updateEventTypeTable(eventTypeTabelModel);

            }
        });
        // Display the edit event type dialog
        editEventTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = eventTypeTable.getSelectedRow();
                if (index != -1){
                    addEventTypeDialog = new AddEventTypeDialog(eventTypeList,machineList,index);
                    addEventTypeDialog.pack();
                    addEventTypeDialog.setVisible(true);
                    updateEventTypeTable(eventTypeTabelModel);
                }
byte a = 0x7F;

            }
        });
        // Saves the currently loaded machines and event types into their respective XML files.
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveConfig(eventTypeTabelModel,machineTabelModel);
            }
        });
        // Loads a set of previously stored conifg files
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentDirectory != "") {
                    ConfigManager cm = new ConfigManager();
                    eventTypeList = new ArrayList<EventType>();
                    machineList = new ArrayList<Machine>();
                    cm.readEventTypes(eventTypeList, currentDirectory + "\\config.xml");
                    cm.readMachinesInfo(machineList, currentDirectory + "\\machineinfo.xml");
                    updateEventTypeTable(eventTypeTabelModel);
                    updateMachineTable(machineTabelModel);

                    if (cm.getSerializer().equals("java")) {
                        serializerCombo.setSelectedIndex(1);
                    } else if (cm.getSerializer().equals("protobuf")) {
                        serializerCombo.setSelectedIndex(2);
                    } else {
                        statusLabel.setText("Bad Serializer name.");
                    }

                    if (cm.getTimestampUnit().equals("mili")) {
                        timestampUnitCombo.setSelectedIndex(1);
                        timeUnitLabel.setText("miliSeconds");
                    } else if (cm.getTimestampUnit().equals("seconds")) {
                        timestampUnitCombo.setSelectedIndex(2);
                        timeUnitLabel.setText("Seconds");
                    } else {
                        statusLabel.setText("Bad Timestamp unit.");
                    }

                    if (cm.getIsTimestamp().equals("true")) {
                        timestampSquenceCombo.setSelectedIndex(1);
                    } else if (cm.getIsTimestamp().equals("false")) {
                        timestampSquenceCombo.setSelectedIndex(2);
                    } else {
                        statusLabel.setText("Bad IsTimestamp Value");
                    }
                }
                else {
                    statusLabel.setText("Config folder not selected.");
                }
            }
        });
        // generate events based on the latest loaded event type config files.
        generateEventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentDirectory != "") {
                    saveConfig(eventTypeTabelModel,machineTabelModel);
                    EventGenerator eg = new EventGenerator();
                    eg.run(currentDirectory,Double.parseDouble(timeLimitTextField.getText()),Integer.parseInt(countLimitTextField.getText()));
                }
                else {
                    statusLabel.setText("Config folder not selected yet.");
                }
            }
        });
        // Terminates the entire application
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        selectConfigFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser(".\\");
                // Demonstrate "Open" dialog:
                c.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                int rVal = c.showDialog(mainForm.this,"Select as config folder");
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    currentDirectory = c.getSelectedFile().getAbsolutePath();
                    confFolderLabel.setText(currentDirectory);}
                else {
                    statusLabel.setText("Config folder selection cancelled.");
                }
            }

        });
        timestampUnitCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timestampUnitCombo.getSelectedItem().equals("Seconds")){
                    timeUnitLabel.setText("Seconds");
                }
                else {
                    timeUnitLabel.setText("miliSeconds");
                }
            }
        });
    }

    /**
     * Gets the table model of the EventType Table and constanly update its table with regards to EventTypes ArrayList.
     * @param tm
     */
    private void updateEventTypeTable(DefaultTableModel tm){
        if (eventTypeList.size()>0) {
            ArrayList<String> tmpRow = new ArrayList<String>();
            while(tm.getRowCount() > 0)
            {
                tm.removeRow(0);
            }
            for (EventType et : eventTypeList) {
                tmpRow.add(et.name);
                tmpRow.add(String.valueOf(et.rate));
                tmpRow.add(et.dispatchType);
                tmpRow.add(String.valueOf(et.eventTypeAttributeList.size()));
                tmpRow.add(String.valueOf(et.eventTypeMachineList.size()));
                tm.addRow(tmpRow.toArray());
                tmpRow.clear();

            }
        }
    }

    /**
     * Gets the table model of the Machine Table and constanly update its table with regards to MachineList ArrayList.
     * @param tm this is the table model of Machine Table.
     */
    private void updateMachineTable(DefaultTableModel tm){

        if (machineList.size()>0) {
            ArrayList<String> tmpRow = new ArrayList<String>();
            while(tm.getRowCount() > 0)
            {
                tm.removeRow(0);
            }
            for(Machine m : machineList){
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

    /**
     * The method responsible for initializing and showing the main dialog. called in the Main.java .
     * @param args usually Null is passed because so far there is no need for any arguments.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("mainForm");
        frame.setContentPane(new mainForm().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("CEP Event Generator");
        frame.setVisible(true);

    }

    /**
     * generates the machineinfo.xml using the data stored in the machineList array.
     * @param mL machineList is passed into this method.
     * @return returns the string represntation of machineinfo.xml
     */
    private String generateMachineXML(ArrayList<Machine> mL){
        String machineTag = "<machines>\n";
        for (Machine m: mL){
            machineTag += "<machine><name>"+m.name+"</name><ip>"+m.ip+"</ip><port>"+String.valueOf(m.port)+"</port><eventfiles>"+m.eventFiles+"</eventfiles></machine>\n";
        }
        machineTag += "</machines>\n";


        return machineTag;
    }

    public void saveConfig(DefaultTableModel eventTypeTabelModel,DefaultTableModel machineTabelModel){
        if (currentDirectory!="") {
            try {
                PrintWriter out = new PrintWriter(currentDirectory + "\\" + "config.xml");
                out.println("<config>");
                out.println("<version>" + versionField.getText() + "</version>");
                if (timestampSquenceCombo.getSelectedItem().toString().equals("Timestamp")) {
                    out.println("<istimestamp>true</istimestamp>");
                } else {
                    out.println("<istimestamp>false</istimestamp>");
                }
                if (timestampUnitCombo.getSelectedItem().toString().equals("Seconds")) {
                    out.println("<unit>seconds</unit>");
                } else {
                    out.println("<unit>mili</unit>");
                }
                out.println("<serializer>" + serializerCombo.getSelectedItem().toString() + "</serializer>");
                out.println("<serializerVersion>" + serializerVersionField.getText() + "</serializerVersion>");

                for (EventType et : eventTypeList) {
                    out.println("<eventtype>");
                    out.println("<name>" + et.name + "</name>");
                    out.println("<rate>" + String.valueOf(et.rate) + "</rate>");
                    out.println("<dispatchtype>" + et.dispatchType + "</dispatchtype>");
                    out.println("<machines>");
                    for (EventTypeMachine etm : et.eventTypeMachineList) {
                        out.println("<machine><name>" + etm.name + "</name><percentage>" + etm.percentage + "</percentage></machine>");
                    }
                    out.println("</machines>");
                    out.println("<attributes>");
                    for (EventTypeAttribute eta : et.eventTypeAttributeList) {
                        out.println("<attribute><name>" + eta.name + "</name><distribution>" + eta.distribution + "</distribution></attribute>");
                    }

                    out.println("</attributes>");
                    out.println("</eventtype>");
                }
                out.println("</config>");
                out.close();

            } catch (Exception excp) {
                statusLabel.setText("IO ERROR: Error writing config.xml");
            }

            try {
                PrintWriter out = new PrintWriter(currentDirectory + "\\" + "machineinfo.xml");
                out.print(generateMachineXML(machineList));
                out.close();

            } catch (Exception excp2) {
                statusLabel.setText("IO ERROR: Error writing machineinfo.xml");
            }
            ConfigManager cm = new ConfigManager();
            eventTypeList = new ArrayList<EventType>();
            machineList = new ArrayList<Machine>();
            cm.readEventTypes(eventTypeList, currentDirectory + "\\config.xml");
            cm.readMachinesInfo(machineList, currentDirectory + "\\machineinfo.xml");
            updateEventTypeTable(eventTypeTabelModel);
            updateMachineTable(machineTabelModel);


        }
        else {
            statusLabel.setText("Config directory not selected yet.");
        }

    }
}
