package cepBench;


import java.util.ArrayList;

/**
 * Created by mohammadreza on 7/2/2016.
 */
public class EventType implements java.io.Serializable {
    public String name;
    public double rate;
    public String dispatchType;
    public ArrayList<EventTypeAttribute> eventTypeAttributeList;
    public ArrayList<EventTypeMachine> eventTypeMachineList;
    public ArrayList<Event> instances;
        public EventType(){
            eventTypeAttributeList = new ArrayList<EventTypeAttribute>();
            eventTypeMachineList = new ArrayList<EventTypeMachine>();
            instances = new ArrayList<Event>();
        }
}