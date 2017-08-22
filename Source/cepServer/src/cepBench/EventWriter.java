package cepBench;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by mohammadreza on 8/15/2016.
 */
public class EventWriter {
    private String destinationDirectory;
    private String serializer;
    private HeaderManager header;
    private String destinationFile;


    EventWriter(String destinationDirectory , String destinationFile, String serializer ){
        this.destinationDirectory = destinationDirectory;
        this.serializer = serializer;
        header = new HeaderManager();
        header.orderType = 0;
        if (serializer.equals("java")){
            header.serializerType = -52;
        }else if(serializer.equals("protobuf"))
        {
            header.serializerType = 51;
        }
        header.timeStamp = 0x5;
        header.version = 1;
        header.serializerVersion = 1;
        this.destinationFile = destinationFile;
    }


    public void saveJEventListToFile( ArrayList<JEvent> eventArray){
        try {

            FileOutputStream fo = new FileOutputStream(destinationDirectory + "\\" + destinationFile);
            fo.write(header.toByteArray());
            int sequenceId = 0;
            for (JEvent e : eventArray) {
                try {

                    byte cc = 60;
                    byte aa = -86;
                    int eLenght;
                    byte[] eventAsBytes = {};

                    try {
                        byte[] tmpByteArray;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(bos);
                        out.writeObject(e);
                        tmpByteArray = bos.toByteArray();
                        out.close();
                        bos.close();
                        eventAsBytes = tmpByteArray;
                    } catch (Exception ie) {
                        ie.printStackTrace();
                    }

                    eLenght = eventAsBytes.length;
                    ByteBuffer bb = ByteBuffer.allocate(eLenght + 2 + 1 + 1 + 8);

                    bb.put(cc);
                    bb.putShort((short) eLenght);
                    bb.put(aa);
                    // SEQUENCE ID
                    bb.putDouble(sequenceId);
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


    public void savePEventListToFile(ArrayList<Pevent.event> eventArray){
        try {

            FileOutputStream fo = new FileOutputStream(destinationDirectory + "\\" + destinationFile);

            fo.write(header.toByteArray());
            int sequenceId = 0;
            for (Pevent.event e : eventArray) {
                try {
                    byte cc = 60;
                    byte aa = -86;
                    int eLenght;
                    byte[] eventAsBytes = {};
                    eventAsBytes = e.toByteArray();
                    eLenght = eventAsBytes.length;
                    ByteBuffer bb = ByteBuffer.allocate(eLenght + 2 + 1 + 1 + 8);

                    bb.put(cc);
                    bb.putShort((short) eLenght);
                    bb.put(aa);
                    // SEQUENCE ID
                    bb.putDouble(sequenceId);

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
}
