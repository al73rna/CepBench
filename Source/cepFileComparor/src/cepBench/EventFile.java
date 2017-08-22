package cepBench;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by mohammadreza on 8/5/2016.
 */
public class EventFile {
    private DataInputStream ds ;
    private byte[] headerBytes;
    private JEvent currentJEvent;
    private Pevent.event currentPEvent;
    private int currentLenght;
    private byte currentCC ;
    private byte currentAA;
    private double currentTS;
    private ArrayList<Integer> lenghts;

    public ArrayList<Pevent.event> getPevents() {
        return pevents;
    }

    public ArrayList<JEvent> getJevents() {
        return jevents;
    }

    private ArrayList<Byte> CCs ;
    private ArrayList<Byte> AAs;
    private ArrayList<Double> TSs;
    ArrayList<Pevent.event> pevents;
    ArrayList<JEvent> jevents;
    private HeaderManager header;
    String serializerType = null;

    public JEvent getCurrentJEvent() {
        return currentJEvent;
    }
    public Pevent.event getCurrentPEvent() {
        return currentPEvent;
    }

    public int getCurrentLenght() {
        return currentLenght;
    }

    public byte getCurrentCC() {
        return currentCC;
    }

    public byte getCurrentAA() {
        return currentAA;
    }

    public double getCurrentTS() {
        return currentTS;
    }


    EventFile(String fAdress){
        CCs = new ArrayList<Byte>();
        AAs = new ArrayList<>();
        lenghts = new ArrayList<>();
        pevents = new ArrayList<>();
        jevents = new ArrayList<>();
        TSs = new ArrayList<>();

        try {
            ds = new DataInputStream(new FileInputStream(fAdress));
            headerBytes = new byte[4];
            ds.read(headerBytes);
        }
        catch (Exception e) {
            System.out.print(e.getStackTrace().toString());
        }
        header= new HeaderManager();
        header.readHeader(headerBytes);
        if (header.serializerType==-52){
            serializerType = "java";
        }
        else if (header.serializerType==51){
            serializerType = "protobuf";
        }
        else {
            System.out.print("BAD serialzer type in header");
        }

    }
public int getLenght(){
    if (serializerType.equals("java"))
        return jevents.size();
    else if (serializerType.equals("protobuf")){
        return pevents.size();
    }
    return -1;

}
    public HeaderManager getHeader() {
        return header;
    }

    //public boolean hasNext() {
    //    return (index < events.size());
    //}
    public void get(int index){
        currentAA = AAs.get(index);
        currentCC = CCs.get(index);
        if (serializerType.equals("java")){
        currentJEvent = jevents.get(index);}
        else if (serializerType.equals("protobuf")){
            currentPEvent = pevents.get(index);
        }
        currentLenght = lenghts.get(index);
        currentTS = TSs.get(index);

    }
    public void readAll(){
        try {

            byte[] eventHeader = new byte[12];
            while ((ds.read(eventHeader))!=-1){
                ByteBuffer bEventHeader = ByteBuffer.wrap(eventHeader);
                CCs.add(bEventHeader.get());
                int elenght = bEventHeader.getShort();
                lenghts.add(elenght);
                AAs.add(bEventHeader.get());
                TSs.add(bEventHeader.getDouble());
                byte[] buffer = new byte[elenght];
                ds.read(buffer);
                if (serializerType.equals("java")){
                    //jevents.add(buffer);
                    JEvent newJEvent;
                    ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                    ObjectInput in = new ObjectInputStream(bis);
                    newJEvent = (JEvent) in.readObject();
                    jevents.add(newJEvent);
                }
                else if (serializerType.equals("protobuf")){
                    Pevent.event tempPEvent = Pevent.event.parseFrom(buffer);
                    pevents.add(tempPEvent);
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
