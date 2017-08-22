package cepBench;

import java.io.Console;

/**
 * Created by mohammadreza on 8/14/2016.
 */
public class Main {
    public static void main(String[] args){
//        TCPServer tc = new TCPServer("protobuf",9898,500000);
//        tc.run();

    mainForm mf = new mainForm();
        mf.main(null);
//        Console sc = System.console();
//        System.out.println("CEP SERVER \n");
//        System.out.println("========== \n");
//        System.out.println("TCP / UDP ? (Enter T or U)");
//        String mode = sc.readLine();
//        System.out.println("Enter port number :");
//        int port = Integer.parseInt(sc.readLine());
//        System.out.println("Enter serializer (J for java / P for protobuf) :");
//        String serializer = sc.readLine();
//        System.out.println("Enter destination Folder :");
//        String saveAdr = sc.readLine();
//        System.out.println("Enter Event Count Limit:");
//        int limit = Integer.parseInt(sc.readLine());
//
//
//
//
//        TCPServer tc ;
//        UDPServer uc ;
//        if (mode.equals("T")){
//            if (serializer.equals("J")){
//                tc = new TCPServer("java",port,limit,saveAdr);
//                tc.run();
//            }
//            else if(serializer.equals("P")){
//                tc = new TCPServer("protobuf",port,limit,saveAdr);
//                tc.run();
//            }
//
//        }else if (mode.equals("U")){
//
//            if (serializer.equals("J")){
//                uc = new UDPServer("java",port,limit,saveAdr);
//                uc.run();
//            }
//            else if(serializer.equals("P")){
//                uc = new UDPServer("java",port,limit,saveAdr);
//                uc.run();
//            }
//
//        }

    }
}
