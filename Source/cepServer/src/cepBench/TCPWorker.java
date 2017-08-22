package cepBench;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by mohammadreza on 8/14/2016.
 */
public class TCPWorker implements Runnable {
    Socket sock;
    String serializer;
    private double avgRate = 0;
    private int eventCount = 0;
    private String saveDir;
    private int eventLimit;
    private boolean saveEvents;

    ArrayList<JEvent> gatheredJEvents = new ArrayList<>();
    ArrayList<Pevent.event> gatheredPEvents = new ArrayList<>();
    int TID ;
    TCPWorker(Socket s, String serializer , int TID , int eventLimit , String saveDir , boolean saveEvents) {
        sock = s;
        this.serializer = serializer;
        this.TID = TID;
        this.eventLimit = eventLimit;
        this.saveDir = saveDir;
        this.saveEvents = saveEvents;
        try {
            //s.setReceiveBufferSize(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            long before = 1;
            before = System.nanoTime();
            if (serializer.equals("java")) {
                ObjectInputStream oin = new ObjectInputStream(sock.getInputStream());

                while (eventCount < eventLimit) {
                    gatheredJEvents.add((JEvent) oin.readObject());
                    eventCount++;
                    if( TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-before) > 1000 ){
                        System.out.println(String.valueOf(TID)+"-->"+eventCount);
                        eventCount = 0 ;
                        before = System.nanoTime();
                    }
                }
                oin.close();
            } else if (serializer.equals("protobuf")) {

                while (eventCount < eventLimit) {
                    try {
                        gatheredPEvents.add(Pevent.event.parseDelimitedFrom(sock.getInputStream()));
                        eventCount++;
                        if( TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-before) > 1000 ){
                            System.out.println(String.valueOf(TID)+"-->"+eventCount);
                            eventCount = 0 ;
                            before = System.nanoTime();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        if (saveEvents) {
            EventWriter ew = new EventWriter(saveDir, String.valueOf(TID), serializer);
            if (serializer.equals("java")) {
                ew.saveJEventListToFile(gatheredJEvents);
            } else if (serializer.equals("protobuf")) {
                ew.savePEventListToFile(gatheredPEvents);
            }
        }


    }

}
