package cepBench;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by mohammadreza on 8/4/2016.
 */
public class Player {

    ArrayList<MachineP> machineList;
    ArrayList<String> eventFiles;
    String configDirectory;
    MachineP destination;
    ArrayList<String> currentAddreses;
    Player(String fAdress , String destMachineName) {
        machineList = new ArrayList<MachineP>();
        eventFiles = new ArrayList<String>();
        configDirectory = fAdress;
        File folder = new File(fAdress);
        File[] listOfEventFiles = folder.listFiles();
        for (int i = 0; i < listOfEventFiles.length; i++) {
            if (listOfEventFiles[i].isFile()) {
                eventFiles.add(listOfEventFiles[i].getName());
            }
        }
        MachineinfoReader mir = new MachineinfoReader();
        mir.readMachinesInfo(machineList, fAdress + "\\machineinfo.xml");
        for (MachineP m : machineList){
            if(m.name.equals(destMachineName)){
                destination = m ;
            }else {System.out.print("INVALID NAME");}
        }

        String currentFile = "";
        currentAddreses = new ArrayList<String>();
        for (String eventFile : eventFiles) {
            currentFile = eventFile;
            if (currentFile.split("_")[0].equals(destination.name)) {
                currentAddreses.add(configDirectory + "\\" + currentFile);
            }
        }
    }


    public void play(long r) {

        try {
            //cepBench.MachineP machine = m;
            long rate = r;
            //int n = 0;
            //int eventCount;


                ///ExecutorService es = Executors.
//            for (int i = 0 ; i < threadCount ; i++){

//            }

//                if (machine.eventFiles.equals("normal")) {
//                    Thread t = new Thread(new cepBench.TCPFileSender(machine,1,configDirectory+"\\"+machine.name));
//                    t.start();
//                    t.join();
//                } else if (machine.eventFiles.equals("split")) {
//                    for (String fileAddress: eventFiles){
//                        currentFile = fileAddress;
//                        if(fileAddress.split("_")[0].equals(machine.name)){
//                            currentAddreses.add(configDirectory+"\\"+currentFile);
//                        }
//                    }
// }

           // }


            //System.out.println("===>" + String.valueOf(n));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

