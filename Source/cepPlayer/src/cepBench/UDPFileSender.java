package cepBench;

/**
 * Created by mohammadreza on 8/14/2016.
 */

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;



/**
 * Created by mohammadreza on 8/5/2016.
 */
public class UDPFileSender implements Runnable {

    private  MachineP tmachine;
    private double trate;
    private EventFile eventFile;
    private DatagramSocket udpSocket;


    UDPFileSender(MachineP machine, double rate , EventFile eventFile){
        tmachine=machine;
        trate=rate;
        this.eventFile = eventFile ;
    }

    @Override
    public void run() {
        try {
            eventFile.readAll();
            udpSocket = new DatagramSocket();

            if (eventFile.serializerType.equals("java")){
                sendJFile(trate);

            }
            else if (eventFile.serializerType.equals("protobuf")){
                sendPFile(trate);
            }

        }
        catch (Exception e)
        {
            System.out.print(e);
            e.printStackTrace();
        }
    }
    private void sendJFile( double rate ) {

        int eventCount = eventFile.getLenght();

        //sequence id
        if (eventFile.getHeader().orderType == 1) {
            RateLimiter rl = null;
            //sec
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
                    javaSendTo(eventFile.getCurrentJEvent());
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
                    javaSendTo(eventFile.getCurrentJEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("BAD ORDER TYPE");
        }
    }
    private void sendPFile(double rate ) {

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
                    protoSendTo(eventFile.getCurrentPEvent());
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

                    protoSendTo(eventFile.getCurrentPEvent());
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }
        else {
            System.out.println("BAD ORDER TYPE");
        }


    }

    public void javaSendTo(Object o) {
        try {
            InetAddress address = InetAddress.getByName(tmachine.ip);
            ByteArrayOutputStream byteStream = new
                    ByteArrayOutputStream(5000);
            ObjectOutputStream os = new ObjectOutputStream(new
                    BufferedOutputStream(byteStream));
            os.flush();
            os.writeObject(o);
            os.flush();
            //retrieves byte array
            byte[] sendBuf = byteStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, address, tmachine.port);
            int byteCount = packet.getLength();
            udpSocket.send(packet);
            os.close();
        } catch (UnknownHostException e) {
            System.err.println("Exception:  " + e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void protoSendTo(Pevent.event o) {
        try {
            InetAddress address = InetAddress.getByName(tmachine.ip);
            //retrieves byte array
            byte[] sendBuf = o.toByteArray();
            DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, address, tmachine.port);
            int byteCount = packet.getLength();
            udpSocket.send(packet);
        } catch (UnknownHostException e) {
            System.err.println("Exception:  " + e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

