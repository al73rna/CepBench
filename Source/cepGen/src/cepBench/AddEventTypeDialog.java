package cepBench;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddEventTypeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField eventTypeNameField;
    private JTextField eventTypeRateField;
    private JComboBox eventTypeDispatchCombo;
    private JTextField machinePercentageField;
    private JList destinationList;
    private JButton addMachineButton;
    private JTextField attributeNameField;
    private JComboBox attributeDistributionCombo;
    private JTextField attributeDistributionArg1;
    private JTextField attributeDistributionArg3;
    private JTextField attributeDistributionArg2;
    private JList attributeList;
    private JButton addAttributeButton;
    private JLabel attributeArg1Label;
    private JLabel attributeArg2Label;
    private JLabel attributeArg3Label;
    private JComboBox machineNameCombo;
    private JButton removeDestinationButton;
    private JButton removeAttributeButton;
    private JLabel statusLabel;
    public ArrayList<EventTypeMachine> eventTypeMachines;
    public ArrayList<EventTypeAttribute> eventTypeAttributes;


    // Constructor for Add Event type dialog
    public AddEventTypeDialog(ArrayList<EventType> eventTypeList , ArrayList<Machine> machinesList) {

        // initializing the dialog
        setContentPane(contentPane);
        setModal(true);
        eventTypeMachines = new ArrayList<EventTypeMachine>();
        eventTypeAttributes = new ArrayList<EventTypeAttribute>();
        DefaultListModel destinationListModel = new DefaultListModel();
        DefaultListModel attributeListModel = new DefaultListModel();
        destinationList.setModel(destinationListModel);
        attributeList.setModel(attributeListModel);
        getRootPane().setDefaultButton(buttonOK);
        for(Machine m:machinesList){
            machineNameCombo.addItem(m.name);
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventType tempEvent = new EventType();
                if (eventTypeDispatchCombo.getSelectedItem().toString().equals("Normal")){
                    tempEvent.dispatchType = "normal";
                }
                else if (eventTypeDispatchCombo.getSelectedItem().toString().equals("Round Robin"))
                {
                    tempEvent.dispatchType = "RR";
                }
                else if (eventTypeDispatchCombo.getSelectedItem().toString().equals("By Percentage"))
                {
                    tempEvent.dispatchType = "percentage";
                }

                tempEvent.rate = Double.parseDouble(eventTypeRateField.getText());
                tempEvent.name = eventTypeNameField.getText();
                tempEvent.eventTypeMachineList = eventTypeMachines;
                tempEvent.eventTypeAttributeList = eventTypeAttributes;
                eventTypeList.add(tempEvent);
                onOK();
            }
        });




        // updates the enability of argument fields based on the selected distribution.
        attributeDistributionCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (attributeDistributionCombo.getSelectedItem().toString().equals("triangular")){
                    attributeDistributionArg3.setEnabled(true);
                    attributeArg1Label.setText("A");
                    attributeArg2Label.setText("B");
                    attributeArg3Label.setText("C");
                }
                else {
                    attributeDistributionArg3.setEnabled(false);
                    if (attributeDistributionCombo.getSelectedItem().toString().equals("normal")){
                        attributeArg1Label.setText("Mean");
                        attributeArg2Label.setText("Standard Deviation");
                        attributeArg3Label.setText("");
                    }
                    if (attributeDistributionCombo.getSelectedItem().toString().equals("poisson")){
                        attributeArg1Label.setText("p");
                        attributeArg2Label.setText("epsilon");
                        attributeArg3Label.setText("");
                    }
                    if (attributeDistributionCombo.getSelectedItem().toString().equals("logistic")){
                        attributeArg1Label.setText("Mu");
                        attributeArg2Label.setText("S");
                        attributeArg3Label.setText("");
                    }
                }
            }
        });
        // adds a new attribute to the current event type.
        addAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String distributionArgument ;
                if (attributeDistributionCombo.getSelectedItem().toString().equals("triangular")) {
                    distributionArgument = attributeDistributionCombo.getSelectedItem().toString()+" "+attributeDistributionArg1.getText().toString()+" "+
                            attributeDistributionArg2.getText().toString()+" "+attributeDistributionArg3.getText().toString();
                }
                else {
                    distributionArgument = attributeDistributionCombo.getSelectedItem().toString()+" "+attributeDistributionArg1.getText().toString()+" "+
                            attributeDistributionArg2.getText().toString();
                }
                EventTypeAttribute tempEventTypeAttribute = new EventTypeAttribute(attributeNameField.getText().toString(),distributionArgument);
                eventTypeAttributes.add(tempEventTypeAttribute);
                attributeListModel.addElement(attributeNameField.getText().toString());


            }
        });
        // adds a new destination the the current event type.
        addMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                destinationListModel.addElement(machineNameCombo.getSelectedItem().toString());
                EventTypeMachine tempEventTypeMachine = new EventTypeMachine();
                tempEventTypeMachine.name = machineNameCombo.getSelectedItem().toString();
                if (!machinePercentageField.getText().equals("")) {
                    tempEventTypeMachine.percentage = Double.parseDouble(machinePercentageField.getText());
                }
                else {
                    tempEventTypeMachine.percentage = 100;
                }
                eventTypeMachines.add(tempEventTypeMachine);

            }
        });
        // update the enability of the percenatge field based on the dispatch type.
        eventTypeDispatchCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(eventTypeDispatchCombo.getSelectedItem().toString().equals("By Percentage")) {
                    machinePercentageField.setEnabled(true);
                }
                else {
                    machinePercentageField.setEnabled(false);
                }
            }
        });
        // remove the selected destination.
        removeDestinationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ri = destinationList.getSelectedIndex();
                if (ri!=-1) {
                    destinationListModel.removeElementAt(ri);
                    System.out.println(machinesList.size());
                    eventTypeMachines.remove(ri);
                }
                else {
                    statusLabel.setText("No destination selected.");
                }
            }
        });
        // removes the selected attribute.
        removeAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ri = attributeList.getSelectedIndex();
                if (ri!=-1) {
                    attributeListModel.removeElementAt(ri);
                    eventTypeAttributes.remove(ri);
                }
                else{
                    statusLabel.setText("No attribute selected.");
                }

            }
        });
        // call onCancel() when cross is clicked.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        //closes this dialog
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });



    }
    // Constructor for Edit event type dialog
    public AddEventTypeDialog(ArrayList<EventType> eventTypeList , ArrayList<Machine> machinesList , int index) {
        setContentPane(contentPane);
        setModal(true);
        EventType inputEventType = eventTypeList.get(index);
        eventTypeMachines = inputEventType.eventTypeMachineList;
        eventTypeAttributes = inputEventType.eventTypeAttributeList;
        DefaultListModel destinationListModel = new DefaultListModel();
        DefaultListModel attributeListModel = new DefaultListModel();
        destinationList.setModel(destinationListModel);
        attributeList.setModel(attributeListModel);
        getRootPane().setDefaultButton(buttonOK);
        for(Machine m:machinesList){
            machineNameCombo.addItem(m.name);
        }
        for (EventTypeAttribute eta : eventTypeAttributes){
            attributeListModel.addElement(eta.name);
        }
        for (EventTypeMachine etm : eventTypeMachines){
            destinationListModel.addElement(etm.name);
        }

        if (inputEventType.dispatchType == "normal"){
            eventTypeDispatchCombo.setSelectedIndex(0);
        }
        else if (inputEventType.dispatchType == "RR"){
            eventTypeDispatchCombo.setSelectedIndex(1);
        }
        else if(inputEventType.dispatchType == "percentage"){
            eventTypeDispatchCombo.setSelectedIndex(2);
        }
        else {
            statusLabel.setText("Bad input dispatch type");
        }
        eventTypeNameField.setText(inputEventType.name);
        eventTypeRateField.setText(String.valueOf(inputEventType.rate));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventType tempEvent = new EventType();
                if (eventTypeDispatchCombo.getSelectedItem().toString().equals("Normal")){
                    tempEvent.dispatchType = "normal";
                }
                else if (eventTypeDispatchCombo.getSelectedItem().toString().equals("Round Robin"))
                {
                    tempEvent.dispatchType = "RR";
                }
                else if (eventTypeDispatchCombo.getSelectedItem().toString().equals("By Percentage"))
                {
                    tempEvent.dispatchType = "percentage";
                }
                else {
                    statusLabel.setText("Bad output dispatch type");
                }

                tempEvent.rate = Double.parseDouble(eventTypeRateField.getText());
                tempEvent.name = eventTypeNameField.getText();
                tempEvent.eventTypeMachineList = eventTypeMachines;
                tempEvent.eventTypeAttributeList = eventTypeAttributes;
                eventTypeList.set(index,tempEvent);
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });


        addMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                destinationListModel.addElement(machineNameCombo.getSelectedItem().toString());
                EventTypeMachine tempEventTypeMachine = new EventTypeMachine();
                tempEventTypeMachine.name = machineNameCombo.getSelectedItem().toString();
                if (!machinePercentageField.getText().equals("")) {
                    tempEventTypeMachine.percentage = Double.parseDouble(machinePercentageField.getText());
                }
                else {
                    tempEventTypeMachine.percentage = 100;
                }
                eventTypeMachines.add(tempEventTypeMachine);

            }
        });
        attributeDistributionCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (attributeDistributionCombo.getSelectedItem().toString().equals("triangular")){
                    attributeDistributionArg3.setEnabled(true);
                    attributeArg1Label.setText("A");
                    attributeArg2Label.setText("B");
                    attributeArg3Label.setText("C");
                }
                else {
                    attributeDistributionArg3.setEnabled(false);
                    if (attributeDistributionCombo.getSelectedItem().toString().equals("normal")){
                        attributeArg1Label.setText("Mean");
                        attributeArg2Label.setText("Standard Deviation");
                        attributeArg3Label.setText("");
                    }
                    if (attributeDistributionCombo.getSelectedItem().toString().equals("poisson")){
                        attributeArg1Label.setText("p");
                        attributeArg2Label.setText("epsilon");
                        attributeArg3Label.setText("");
                    }
                    if (attributeDistributionCombo.getSelectedItem().toString().equals("logistic")){
                        attributeArg1Label.setText("Mu");
                        attributeArg2Label.setText("S");
                        attributeArg3Label.setText("");
                    }
                }
            }
        });
        addAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String distributionArgument ;
                if (attributeDistributionCombo.getSelectedItem().toString().equals("triangular")) {
                    distributionArgument = attributeDistributionCombo.getSelectedItem().toString()+" "+attributeDistributionArg1.getText().toString()+" "+
                            attributeDistributionArg2.getText().toString()+" "+attributeDistributionArg3.getText().toString();
                }
                else {
                    distributionArgument = attributeDistributionCombo.getSelectedItem().toString()+" "+attributeDistributionArg1.getText().toString()+" "+
                            attributeDistributionArg2.getText().toString();
                }
                EventTypeAttribute tempEventTypeAttribute = new EventTypeAttribute(attributeNameField.getText().toString(),distributionArgument);
                eventTypeAttributes.add(tempEventTypeAttribute);
                attributeListModel.addElement(attributeNameField.getText().toString());


            }
        });
        eventTypeDispatchCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(eventTypeDispatchCombo.getSelectedItem().toString().equals("By Percentage")) {
                    machinePercentageField.setEnabled(true);
                }
                else {
                    machinePercentageField.setEnabled(false);
                }
            }
        });

        removeDestinationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ri = destinationList.getSelectedIndex();
                if (ri!=-1) {
                    destinationListModel.removeElementAt(ri);
                    System.out.println(machinesList.size());
                    eventTypeMachines.remove(ri);
                }
                else {
                    statusLabel.setText("No destination selected.");
                }
            }
        });
        removeAttributeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ri = attributeList.getSelectedIndex();
                if (ri!=-1) {
                    attributeListModel.removeElementAt(ri);
                    eventTypeAttributes.remove(ri);
                }
                else {
                    statusLabel.setText("No attribute selected.");
                }

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

    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }


}
