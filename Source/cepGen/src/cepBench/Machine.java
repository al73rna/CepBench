package cepBench;

import java.util.ArrayList;

/**
 * Created by mohammadreza on 7/13/2016.
 */
public class Machine {
    public String name;
    public String ip;
    public int port;
    String eventFiles;
    ArrayList<Event> eventQueue;

    public Machine(){
        eventQueue = new ArrayList<Event>();
    }

}
