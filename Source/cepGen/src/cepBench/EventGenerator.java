package cepBench;

import java.util.ArrayList;

/**
 * Created by mohammadreza on 7/12/2016.
 */
public class EventGenerator {

    private double event_Time_Limit;
    private int event_Count_Limit;
    private NumberGenerator numberGenerator ;
    private ConfigManager configManager;
    private DispatchManager dispatchManager;
    private ArrayList<EventType> eventTypesList ;
    private ArrayList<Machine> machineList;
    private HeaderManager headerManager ;
    public EventGenerator(){
        event_Time_Limit = 10;
        numberGenerator = new NumberGenerator();
        configManager = new ConfigManager();
        eventTypesList = new ArrayList<EventType>();
        machineList = new ArrayList<Machine>();
        headerManager = new HeaderManager();

    }

    /** generates a list of events for each event type
    * each time increasing the timestamp by 1/rate
    */
    public void generateEvents(ArrayList<EventType> eTL){

        if (configManager.getIsTimestamp().equals("true")) {
            double CurrentTimestamp;
            for (EventType eT : eTL) {
                CurrentTimestamp = 0;
                while (CurrentTimestamp < event_Time_Limit) {
                    Event newEvent = new Event();
                    newEvent.timestamp = CurrentTimestamp;
                    newEvent.name = eT.name;
                    //Generate Attributes for each event based on their distributions
                    generateAttibutes(eT, newEvent);
                    eT.instances.add(newEvent);
                    CurrentTimestamp += 1 / eT.rate;
                }
            }
        }
        else {

            int currentCount;
            for (EventType eT : eTL) {
                currentCount = 0;
                while (currentCount < event_Count_Limit) {
                    Event newEvent = new Event();
                    newEvent.timestamp = 0;
                    newEvent.name = eT.name;
                    //Generate Attributes for each event based on their distributions
                    generateAttibutes(eT, newEvent);
                    eT.instances.add(newEvent);
                    currentCount++;
                }
            }
        }
    }



    /**
     * generates attributes based on their distributions and their arguments
     * @param eT
     * @param ne
     */
    public void generateAttibutes(EventType eT , Event ne){
        //Generate Attributes
        eT.eventTypeAttributeList.forEach((at)->{
            String distribution = at.distribution;
            double tempAttributeValue = numberGenerator.generate(distribution);
            Item tempItem = new Item();
            tempItem.key = at.name ;
            tempItem.value = Double.toString(tempAttributeValue) ;
            ne.items.add(tempItem);
        });
    }

    /**
     * Serialize all of the machines and their events .
     * @param destinationDirectory everything is stored in this directory
     */
    private void saveAllMachines(String destinationDirectory){
        for(Machine m : machineList){
            EventSerializer es = new EventSerializer(destinationDirectory,headerManager,configManager,m);
            es.serialize();
        }
    }

    /** Start the Event Generator, Extract all the configs from the files and generate events for each machine.
     * later storing the event files in the fAdress directory
     **/
    public void run(String fAddress,double timelimit,int countlimit){
        event_Time_Limit = timelimit;
        event_Count_Limit = countlimit;

        configManager.readMachinesInfo(machineList,fAddress +"\\machineinfo.xml");
        configManager.readEventTypes(eventTypesList,fAddress+"\\config.xml");
        headerManager.generateHeader(configManager);
        generateEvents(eventTypesList);
        dispatchManager = new DispatchManager(eventTypesList,machineList);
        dispatchManager.dispatch();
        saveAllMachines(fAddress);
    }

}
