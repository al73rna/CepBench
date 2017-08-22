package cepBench;

/**
 * Created by mohammadreza on 7/2/2016.
 */
public class EventTypeAttribute implements java.io.Serializable{
    public String name ;
    public String distribution;
    public String type ;
    public EventTypeAttribute(String n, String d){
        name = n ;
        distribution = d;
    }
}
