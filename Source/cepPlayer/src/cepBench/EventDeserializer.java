package cepBench;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

/**
 * Created by mohammadreza on 8/5/2016.
 */
public class EventDeserializer {

    public JEvent deserializeJava(byte[] eventBytes) {
        try {
            JEvent newJEvent;
            ByteArrayInputStream bis = new ByteArrayInputStream(eventBytes);
            ObjectInput in = new ObjectInputStream(bis);
            newJEvent = (JEvent) in.readObject();
            return newJEvent;
        } catch (Exception e) {

        }
        return null;
    }

    public Pevent.event deserializeProtobuf(byte[] eventBytes) {
        try {
            Pevent.event tempPEvent = Pevent.event.parseFrom(eventBytes);
            return tempPEvent;
        }
        catch (Exception e) {

        }
        return null;

    }
}





//            cepBench.JEvent newEvent = new cepBench.JEvent();
//            newEvent.name = tempPEvent.getName();
//            newEvent.timestamp = tempPEvent.getTimestamp();
//            for (cepBench.Pevent.item pit : tempPEvent.getItemsList()) {
//                cepBench.Item tempItem = new cepBench.Item();
//                tempItem.key = pit.getKey();
//                tempItem.value = pit.getValue();
//                newEvent.items.add(tempItem);