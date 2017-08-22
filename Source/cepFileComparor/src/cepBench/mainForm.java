package cepBench;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.String;
import java.util.ArrayList;

/**
 * Created by mohammadreza on 8/11/2016.
 */
public class mainForm extends JFrame{
    private JTextField file1AddressField;
    private JTextField file2AddressField;
    String file1Address;
    String file2Address;
    private JButton browseButton1;
    private JButton browseButton2;
    private JList file1List;
    private JList file2List;
    private JButton compareButton;
    private JLabel statusLabel;
    private JPanel main;
    private JPanel JPanel;
    private JLabel onlyInFile1Label;
    private JLabel onlyInFile2Label;
    private JLabel totalInFile1Label;
    private JLabel totalInFile2Label;
    private JList onlyFile1List;
    private JList onlyFile2List;
    private String file1Serializer;
    private String file2Serializer;
    private ArrayList<JEvent> jeventsA;
    private ArrayList<JEvent> jeventsB;
    private ArrayList<Pevent.event> peventsA;
    private ArrayList<Pevent.event> peventsB;

    public mainForm() {
        DefaultListModel file1model = new DefaultListModel();
        DefaultListModel file2model = new DefaultListModel();
        file1List.setModel(file1model);
        file2List.setModel(file2model);
        DefaultListModel onlyFile1ListModel = new DefaultListModel();
        DefaultListModel onlyFile2ListModel = new DefaultListModel();
        onlyFile1List.setModel(onlyFile1ListModel);
        onlyFile2List.setModel(onlyFile2ListModel);

        browseButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser(".\\");
                // Demonstrate "Open" dialog:
                c.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
                int rVal = c.showDialog(mainForm.this,"Select File1");
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    file1Address = c.getSelectedFile().getAbsolutePath();
                    file1AddressField.setText(file1Address);
                    EventFile file1 = new EventFile(file1Address);
                    file1.readAll();
                    file1Serializer = file1.serializerType;
                    if (file1Serializer.equals("java")){
                        jeventsA = file1.getJevents();
                        updateListJ(file1model,jeventsA,file1List);
                    }
                    else if (file1Serializer.equals("protobuf")){
                        peventsA = file1.getPevents();
                        updateListP(file1model,peventsA,file1List);
                    }
                }
                else {
                    statusLabel.setText("Config folder selection cancelled.");
                }



            }
        });
        browseButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser(".\\");
                // Demonstrate "Open" dialog:
                c.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
                int rVal = c.showDialog(mainForm.this,"Select File2");
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    file2Address = c.getSelectedFile().getAbsolutePath();
                    file2AddressField.setText(file2Address);
                    EventFile file2 = new EventFile(file2Address);
                    file2.readAll();
                    file2Serializer = file2.serializerType;
                    if (file2Serializer.equals("java")){
                        jeventsB = file2.getJevents();
                        updateListJ(file2model,jeventsB,file2List);
                    }
                    else if (file2Serializer.equals("protobuf")){
                        peventsB = file2.getPevents();
                        updateListP(file2model,peventsB,file2List);
                    }
                }
                else {
                    statusLabel.setText("Config folder selection cancelled.");
                }




            }
        });
        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileComparer fc = new FileComparer();
                if (file1Serializer.equals(file2Serializer)){
                    if (file1Serializer.equals("java")){
                        fc.compareJ(jeventsA,jeventsB);
                        totalInFile1Label.setText(String.valueOf(jeventsA.size()));
                        totalInFile2Label.setText(String.valueOf(jeventsB.size()));
                        onlyInFile1Label.setText(String.valueOf(fc.getOnlyinAj().size()));
                        onlyInFile2Label.setText(String.valueOf(fc.getOnlyinBj().size()));
                        updateListJ(onlyFile1ListModel,fc.getOnlyinAj(),onlyFile1List);
                        updateListJ(onlyFile2ListModel,fc.getOnlyinBj(),onlyFile2List);
                    }
                    else if (file1Serializer.equals("protobuf")){
                        fc.compareP(peventsA,peventsB);
                        totalInFile1Label.setText(String.valueOf(peventsA.size()));
                        totalInFile2Label.setText(String.valueOf(peventsB.size()));
                        onlyInFile1Label.setText(String.valueOf(fc.getOnlyinAp().size()));
                        onlyInFile2Label.setText(String.valueOf(fc.getOnlyinBp().size()));
                        updateListP(onlyFile1ListModel,fc.getOnlyinAp(),onlyFile1List);
                        updateListP(onlyFile2ListModel,fc.getOnlyinBp(),onlyFile2List);
                    }
                }
                else {
                    statusLabel.setText("SERIALIZER MISMATCH");
                }
            }
        });
    }

    private void updateListJ(DefaultListModel lm , ArrayList<JEvent> el , JList list){
        DefaultListModel tmplm = new DefaultListModel();
        lm = tmplm;
        list.setModel(lm);
        for (JEvent e : el){
            String att = "";
            for (Item it : e.items){
                att += " "+it.key+"->"+it.value+"|";
            }
            lm.addElement(e.name+" :"+att);
        }

    }
    private void updateListP(DefaultListModel lm,ArrayList<Pevent.event> el,JList list){
        DefaultListModel tmplm = new DefaultListModel();
        lm = tmplm;
        list.setModel(lm);
        for (Pevent.event e : el ){
            String att = "";
            for (Pevent.item it : e.getItemsList()){
                att += " "+it.getKey()+"->"+it.getValue()+"|";
            }
            lm.addElement(e.getName()+" :"+att);
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("mainForm");
        frame.setContentPane(new mainForm().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setTitle("JEvent File Comparer");
        frame.setVisible(true);

    }
}
