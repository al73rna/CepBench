package cepBench;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammadreza on 7/12/2016.
 */
public class JEvent implements Serializable{
    private static final long serialVersionUID = 66L;
    public String name;
    public List<Item> items;
    public JEvent(){
        items = new ArrayList<Item>();
    }
}
