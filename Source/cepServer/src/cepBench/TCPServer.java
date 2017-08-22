package cepBench;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mohammadreza on 8/14/2016.
 */
public class TCPServer {
    private String serializer;
    private int port;
    private int eventLimit;
    private String saveDir;
    private boolean saveEvents;


    TCPServer(String serializer , int port, int eventLimit , String saveDir ,boolean saveEvents){
        this.port = port;
        this.eventLimit = eventLimit;
        this.serializer = serializer;
        this.saveDir = saveDir;
        this.saveEvents = saveEvents;

    }


    public void run() {

        try {
            ServerSocket ss = new ServerSocket(port);
            int tID = 0;
            ExecutorService es = Executors.newCachedThreadPool();
            while (true) {
                Socket p = ss.accept();
                System.out.println("connected");
                TCPWorker tw = new TCPWorker(p,serializer,tID,eventLimit,saveDir,saveEvents);
                es.submit(tw);
                tID++;
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
