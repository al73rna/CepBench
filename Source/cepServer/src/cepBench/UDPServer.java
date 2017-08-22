package cepBench;



import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * Created by mohammadreza on 8/4/2016.
 */
public class UDPServer {
    private String serializer;
    private int eventCount = 0;
    private double avgRate = 0;
    private int port;
    private int eventLimit;
    private String saveDir;
    private boolean saveEvents;

    UDPServer(String serializer, int port , int eventLimit , String saveDir , boolean saveEvents){
        this.serializer = serializer;
        this.port = port ;
        this.eventLimit = eventLimit;
        this.saveDir = saveDir;
        this.saveEvents = saveEvents;
    }

    public  void run() {
        ArrayList<JEvent> gatheredJEvents = new ArrayList<>();
        ArrayList<Pevent.event> gatheredPEvents = new ArrayList<>();

        try {


            DatagramSocket dsock = new DatagramSocket(port);
            dsock.setReceiveBufferSize(Integer.MAX_VALUE);
            System.out.print("waiting...");

            long before = 1;


                before = System.nanoTime();
                if (serializer.equals("protobuf")) {
                    while (eventCount<eventLimit) {
                        gatheredPEvents.add(recvPObjFrom(dsock));
                        eventCount++;
                        if( TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-before) > 1000 ){
                            System.out.println(eventCount);
                            eventCount = 0 ;
                            before = System.nanoTime();
                        }

                    }
                }
                else if (serializer.equals("java")){
                    while (eventCount<eventLimit) {
                        gatheredJEvents.add((JEvent) recvJObjFrom(dsock));
                        eventCount++;
                        if( TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-before) > 1000 ){
                            System.out.println(eventCount);
                            eventCount = 0 ;
                            before = System.nanoTime();
                        }
                    }
                }

            dsock.close();

        } catch (Exception e) {
            System.out.print(e);
        }
        if (saveEvents) {
            EventWriter ew = new EventWriter(saveDir, String.valueOf("UDPEvent"), serializer);
            if (serializer.equals("java")) {
                ew.saveJEventListToFile(gatheredJEvents);
            } else if (serializer.equals("protobuf")) {
                ew.savePEventListToFile(gatheredPEvents);
            }
        }


    }



    private  Object recvJObjFrom(DatagramSocket dSock) {
        try {
            byte[] recvBuf = new byte[5000];
            DatagramPacket packet = new DatagramPacket(recvBuf,
                    recvBuf.length);
            dSock.receive(packet);
            int byteCount = packet.getLength();
            ByteArrayInputStream byteStream = new
                    ByteArrayInputStream(recvBuf);
            ObjectInputStream is = new
                    ObjectInputStream(new BufferedInputStream(byteStream));
            Object o = is.readObject();
            is.close();
            return (o);
        } catch (IOException e) {
            System.err.println("Exception:  " + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (null);
    }

    private  Pevent.event recvPObjFrom(DatagramSocket dSock) {
        try {
            byte[] recvBuf = new byte[5000];
            DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
            dSock.receive(packet);
            int byteCount = packet.getLength();
            byte[] tmp = new byte[byteCount];
            for(int i =0 ; i < byteCount;i++){
                tmp[i] = recvBuf[i];
            }
            Pevent.event tempEvent = Pevent.event.parseFrom(tmp);
            return (tempEvent);
        } catch (IOException e) {
            System.err.println("Exception:  " + e);
            e.printStackTrace();
        }

        return (null);
    }
}

