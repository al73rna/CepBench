package cepBench;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammadreza on 7/12/2016.
 */
public class Event implements Serializable{
    private static final long serialVersionUID = 1L;
    public String name;
    public double timestamp;
    public List<Item> items;
    public Event(){
        items = new ArrayList<Item>();
    }
    public JEvent toJEvent(){
        JEvent jevent = new JEvent();
        jevent.name = name;
        jevent.items = items;
        return jevent;
    }
}
