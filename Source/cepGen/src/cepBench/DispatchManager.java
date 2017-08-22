package cepBench;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mohammadreza on 7/15/2016.
 */
public class DispatchManager {
    ArrayList<EventType> eventTypeList;
    ArrayList<Machine> machineList;

    public DispatchManager(ArrayList<EventType> etl , ArrayList<Machine> ml){
        eventTypeList = etl;
        machineList = ml;
    }

    /**
     * Assign the events to different machines based on each machine's dispatch type
     */
    public void dispatch(){
        for (EventType et: eventTypeList) {
            switch (et.dispatchType) {
                case "normal":
                    normal(et);
                    break;
                case "RR":
                    roundRobin(et);
                    break;
                case "percentage":
                    byPercentage(et);
                    break;
            }
        }
        // sort each event queue after being filled with events with regard to their timestamps.
        machinesSortQueueByTimestamp(machineList);

    }

    /**
     * Assign for the dispatch type "normal"
     * @param eventType
     */
    public void normal(EventType eventType){

            for (EventTypeMachine etm : eventType.eventTypeMachineList) {
                for (Machine m : machineList) {
                    if (m.name.equals(etm.name) ) {
                        m.eventQueue.addAll(eventType.instances);
                    }
                }
            }

    }
    /**
     * Assign for the dispatch type "roundrobin"
     * @param eventType
     */
    public void roundRobin(EventType eventType){
            // Extract the destination machines for each event type
            ArrayList<Machine> destMachines = new ArrayList<Machine>();
            for (EventTypeMachine etm : eventType.eventTypeMachineList){
                for (Machine m : machineList) {
                    if (m.name.equals( etm.name) ) {
                        destMachines.add(m);
                    }
                }
            }
            int i = 0 ;
            for(Event e: eventType.instances){
                    destMachines.get(i%destMachines.size()).eventQueue.add(e);
                i++;
                }

    }
    /**
     * Assign for the dispatch type "by percentage"
     */
    public void byPercentage(EventType et) {
        ArrayList<Integer> numberOfAddedEvents = new ArrayList<Integer>();
        ArrayList<Integer> finalNumberOfEvents = new ArrayList<Integer>();
        ArrayList<String> currentState = new ArrayList<String>();
        currentState.clear();
        numberOfAddedEvents.clear();
        finalNumberOfEvents.clear();
        ArrayList<Machine> destMachines = new ArrayList<Machine>();

        // Extract the destination machines for each event type
        for (EventTypeMachine etm : et.eventTypeMachineList) {
            for (Machine m : machineList) {
                if (m.name.equals(etm.name)) {
                    destMachines.add(m);
                    numberOfAddedEvents.add(0);
                    finalNumberOfEvents.add((int) (et.instances.size() * etm.percentage / 100));
                    if (etm.percentage != 0 ){
                    currentState.add("ready");}
                    else{
                        currentState.add("full");
                    }

                }
            }
        }

        // assigns to each machine by percentage and using round robin
        int i = 0;
        int ci = 0;
        for (Event e : et.instances) {
            int busyMachines = 0;
            while (currentState.get(ci) != "ready")
            {
                i++;
                ci = i % destMachines.size();
                busyMachines++;
            }
            destMachines.get(ci).eventQueue.add(e);
            numberOfAddedEvents.set(ci, numberOfAddedEvents.get(ci) + 1);
            if (numberOfAddedEvents.get(ci) >= finalNumberOfEvents.get(ci)) {
                currentState.set(ci, "full");
            }
            i++;
            ci = i % destMachines.size();
        }
    }



// function to sort machine queues based on the events timestamp
    private void machinesSortQueueByTimestamp(ArrayList<Machine> ml){
        for (Machine m:ml) {
            Collections.sort(m.eventQueue, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return Double.compare(o1.timestamp,o2.timestamp);
                }
            });
        }
    }



}
