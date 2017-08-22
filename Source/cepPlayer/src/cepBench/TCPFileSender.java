package cepBench;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by mohammadreza on 8/5/2016.
 */
public class TCPFileSender implements Runnable {

    private  MachineP tmachine;
    private double trate;
    private EventFile eventFile;



    TCPFileSender(MachineP machine, double rate , EventFile eventFile){
        tmachine=machine;
        trate=rate;
        this.eventFile = eventFile ;
    }

    @Override
    public void run() {
        try {
            eventFile.readAll();
            Socket sender = new Socket(tmachine.ip, tmachine.port);

            if (eventFile.serializerType.equals("java")){
                ObjectOutputStream senderStream = new ObjectOutputStream(sender.getOutputStream());
                sendJFile(senderStream,trate);
                senderStream.close();
            }
            else if (eventFile.serializerType.equals("protobuf")){
                sendPFile(sender.getOutputStream(),trate);
            }
            sender.close();
        }
        catch (IOException e)
        {
            System.out.print(e);
            e.printStackTrace();
        }

    }
    private void sendJFile(ObjectOutputStream senderStream , double rate ) {

        int eventCount = eventFile.getLenght();

        //sequence id
        if (eventFile.getHeader().orderType == 1) {
            RateLimiter rl = null;
            //seconds
            if (eventFile.getHeader().timeStamp == 85) {
                rl = RateLimiter.create(rate);
            //milis
            } else if (eventFile.getHeader().timeStamp == -86) {
                rl = RateLimiter.create(rate*0.001);
            }
            for (int i = 0; i < eventCount; i++) {
                eventFile.get(i);
                rl.acquire();
                try {

                    senderStream.writeObject(eventFile.getCurrentJEvent());

                }
                catch (Exception e){e.printStackTrace();}
            }
        }
        // timestamp
        else if (eventFile.getHeader().orderType == 2) {
            Stopwatch st = Stopwatch.createUnstarted();
            TimeUnit timeUnit = null;
            //micros
            if (eventFile.getHeader().timeStamp == 85) {
                timeUnit = TimeUnit.SECONDS;
             //milis
            } else if (eventFile.getHeader().timeStamp == -86) {
                timeUnit = TimeUnit.MILLISECONDS;
            } else {
                System.out.println("BAD TIME UNIT");
            }

            st.start();
            for (int i = 0; i < eventCount; i++) {
                while (st.elapsed(timeUnit) - eventFile.getCurrentTS() < 0) {
                    continue;
                }
                eventFile.get(i);
                try {

                    senderStream.writeObject(eventFile.getCurrentJEvent());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("BAD ORDER TYPE");
        }


    }
    private void sendPFile( OutputStream senderStream , double rate ) {

        int eventCount = eventFile.getLenght();

        //sequence id
        if (eventFile.getHeader().orderType == 1) {
            RateLimiter rl = null;
            //micros
            if (eventFile.getHeader().timeStamp == 85) {
                rl = RateLimiter.create(rate);

            } else if (eventFile.getHeader().timeStamp == -86) {
                rl = RateLimiter.create(rate*0.001);
            }
            for (int i = 0; i < eventCount; i++) {
                eventFile.get(i);
                rl.acquire();
                try {
                   eventFile.getCurrentPEvent().writeDelimitedTo(senderStream);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        // timestamp
        else if (eventFile.getHeader().orderType == 2) {
            Stopwatch st = Stopwatch.createUnstarted();
            TimeUnit timeUnit = null;
            //micros
            if (eventFile.getHeader().timeStamp == 85) {
                timeUnit = TimeUnit.SECONDS;
            } else if (eventFile.getHeader().timeStamp == -86) {
                timeUnit = TimeUnit.MILLISECONDS;
            } else {
                System.out.println("BAD TIME UNT");
            }

            st.start();
            for (int i = 0; i < eventCount; i++) {

                while (st.elapsed(timeUnit) - eventFile.getCurrentTS() < 0) {
                    continue;
                }
                eventFile.get(i);
                try {
                  eventFile.getCurrentPEvent().writeDelimitedTo(senderStream);
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("BAD ORDER TYPE");
        }


    }


}
