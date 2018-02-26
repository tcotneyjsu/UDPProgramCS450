/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpprogramcs450;
import java.util.Random;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.net.DatagramSocket;//Had to import both because of constructor errors
import java.net.DatagramPacket;//Had to import both because of constructor errors

/**
 *
 * @author Travis
 */
public class UDPServer extends Thread{
    Random random = new Random();
    public static String serverOutput = "";
    public UDPServer(){
    
    }
    public void run(){
        try{


            // Create a datagram socket, bound to the specific port 2000
            DatagramSocket socket = new DatagramSocket(2000);
            UDPGUI.outputText.append("Bound to local port " + socket.getLocalPort()+"\n");
            while(true){ 
                // Create a datagram packet, containing a maximum buffer of 256 byte 
            DatagramPacket packet = new DatagramPacket( new byte[256], 256 );

            // Receive a packet - remember by default this is a blocking operation

            socket.receive(packet);
            if(random.nextInt(3) != 1){
                //received = true;
                UDPGUI.outputText.append("Packet received at " + new Date( )+"\n");
                // Display packet information
                InetAddress remote_addr = packet.getAddress();
                UDPGUI.outputText.append("Sender: " + remote_addr.getHostAddress( )+"\n" );
                UDPGUI.outputText.append("from Port: " + packet.getPort()+"\n");

                // Display packet contents, by reading from byte array
                ByteArrayInputStream bin = new ByteArrayInputStream(packet.getData());

                // Display only up to the length of the original UDP packet
                for (int i=0; i < packet.getLength(); i++)  {
                        int data = bin.read();
                        if (data == -1) break;

                        else UDPGUI.outputText.append((char) data+"");

                }
                UDPGUI.outputText.append("\n\n");
                try{
                    int whatislove = random.nextInt(201);
                    Thread.sleep(whatislove);
                }
                catch(Exception e){}
                socket.send(packet);
            }
            else{
                UDPGUI.outputText.append("Packet "
                        + " was lost, now Hangry c(-_-c), " +"\n"
                        + "continuing to next iteration."+"\n\n");

                //clientSent++;

            }
            

        }
        }   
        catch (IOException e) 	{
                UDPGUI.outputText.append("Error - " + e);
        }
       UDPGUI.outputText.setText(serverOutput);
    }
public String getServerOutput(){
    return serverOutput;
}
}
