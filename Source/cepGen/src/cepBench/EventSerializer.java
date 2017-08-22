package cepBench;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohammadreza on 8/2/2016.
 */
public class EventSerializer {
    String serializer;
    String isTimestamp;
    HeaderManager header;
    String destinationDirectory;
    String name;
    ArrayList<Event> eventQueue;
    String eventFiles;

    EventSerializer(String destination, HeaderManager hm , ConfigManager cnfm ,Machine m){
        destinationDirectory = destination;
        this.header = hm;
        serializer = cnfm.getSerializer();
        isTimestamp = cnfm.getIsTimestamp();
        name = m.name;
        eventQueue = m.eventQueue;
        eventFiles = m.eventFiles;
    }

    public void serialize(){

        if(eventFiles.equals("normal")){
            serilizeToSingleFile();
        }
        else if (eventFiles.equals("split")){
            serializeToMultiFiles();
        }
    }

    private void serilizeToSingleFile(){
        saveEventListToFile(name,eventQueue);
    }
    private void serializeToMultiFiles(){


        HashMap<String,ArrayList<Event>> fileList = new HashMap<>();
        byte[] events = header.toByteArray();
        for (Event e : eventQueue) {
            if(fileList.containsKey(e.name)){
                fileList.get(e.name).add(e);
            }
            else{
                ArrayList<Event> tmpArrayList = new ArrayList<>();
                tmpArrayList.add(e);
                fileList.put(e.name,tmpArrayList);
            }
        }

        for(HashMap.Entry<String ,ArrayList<Event>> entry: fileList.entrySet()){
            saveEventListToFile(name+"_"+entry.getKey(),entry.getValue());
        }

    }
    private void saveEventListToFile(String destinationFile , ArrayList<Event> eventArray){
        try {

            FileOutputStream fo = new FileOutputStream(destinationDirectory + "\\" + destinationFile);

            fo.write(header.toByteArray());
            int sequenceId = 0;
            for (Event e : eventArray) {
                try {

                    byte c3 = 60;
                    byte aa = -86;
                    int eLenght;
                    byte[] eventAsBytes = {};
                    if (serializer.equals("java")) {
                        eventAsBytes = serializeEventJava(e.toJEvent());
                    } else if (serializer.equals("protobuf")) {
                        eventAsBytes = serializeEventProtoBuf(e);
                    }
                    eLenght = eventAsBytes.length;
                    ByteBuffer bb = ByteBuffer.allocate(eLenght + 2 + 1 + 1 + 8);
                    bb.put((byte) 60);
                    bb.putShort((short) eLenght);
                    bb.put((byte) -86);
                    if (isTimestamp.equals("true")) {
                        bb.putDouble(e.timestamp);
                    } else {
                        bb.putDouble(sequenceId);
                    }

                    bb.put(eventAsBytes);
                    fo.write(bb.array());
                } catch (Exception i) {
                    i.printStackTrace();
                }
                sequenceId++;
            }



            fo.close();
        }
        catch (Exception e){
            //TODO
        }
    }
    private byte[] serializeEventProtoBuf(Event e ){
        byte[] returnEventArray ;
        Pevent.event.Builder tempPevent = Pevent.event.newBuilder();
        tempPevent.setName(e.name);
        for(Item it : e.items){
            Pevent.item.Builder tempItem = Pevent.item.newBuilder();
            tempItem.setKey(it.key);
            tempItem.setValue(it.value);
            tempPevent.addItems(tempItem.build());
        }
        returnEventArray = tempPevent.build().toByteArray();
        return returnEventArray;
    }
    private byte[] serializeEventJava(JEvent e ){
        byte[] returnEventArray = {};
        try {
            byte[] tmpByteArray ;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(e);
            tmpByteArray = bos.toByteArray();
            out.close();
            bos.close();
            returnEventArray = tmpByteArray;
        }
        catch (Exception ie)
        {
            ie.printStackTrace();
        }
        return returnEventArray;
    }
    private byte[] mergeByteArrays(byte[] one , byte[] two){
        byte[] combined = new byte[one.length + two.length];
        System.arraycopy(one,0,combined,0         ,one.length);
        System.arraycopy(two,0,combined,one.length,two.length);
        return combined;
    }
}
