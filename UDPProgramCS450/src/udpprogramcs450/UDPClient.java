/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpprogramcs450;
import java.util.*;
import java.io.*;
import java.net.*;
import java.net.DatagramSocket;//Had to import both because of constructor errors
import java.net.DatagramPacket;//Had to import both because of constructor errors

/**
 *
 * @author Travis
 */
public class UDPClient extends Thread{
    long startTime;
    long stopTime;
    long waitTime;
    long totalDuration;
    int clientSent = 1;
    public boolean received;
    String clientOutput = "";
    public InetAddress targetAddress;
    
        //use localhost to experiment on a standalone computer
        //@Override
    

    public UDPClient(String ip){
        String[] stringArray = ip.split("\\.");
        byte[] address = new byte[4];
        try{
            address[0] = (byte)Integer.parseInt(stringArray[0]);
            address[1] = (byte)Integer.parseInt(stringArray[1]);
            address[2] = (byte)Integer.parseInt(stringArray[2]);
            address[3] = (byte)Integer.parseInt(stringArray[3]);

            targetAddress = InetAddress.getByAddress(address);
        }
        catch(ArrayIndexOutOfBoundsException | UnknownHostException e){
            //JOptionPane.showMessageDialog(null, "The target IP address you entered in invalid, please close the program and enter a valid one", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
        }
    }
        public void run(){
        String hostname="localhost";    String message = "HELLO USING UDP!";
        while(clientSent < 11){
            startTime = System.currentTimeMillis();
            try {
		// Create a datagram socket, look for the first available port
		DatagramSocket socket = new DatagramSocket();

		UDPGUI.outputText.append("Using local port: " + socket.getLocalPort()+"\n");
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                PrintStream pOut = new PrintStream(bOut);
                pOut.print(message);
                //convert printstream to byte array
                byte [ ] bArray = bOut.toByteArray();
		//Create a datagram packet, containing a maximum buffer of 256 bytes
		DatagramPacket packet=new DatagramPacket( bArray, bArray.length );

                UDPGUI.outputText.append("Looking for hostname " + hostname+"\n");
                    //get the InetAddress object
                //InetAddress remote_addr = InetAddress.getByName(hostname);
                //byte[] ipAddr = new byte[]{(byte)192, (byte)168, (byte)1, (byte)98};
                InetAddress remote_addr = targetAddress;
                //check its IP number
                UDPGUI.outputText.append("Hostname has IP address = " + remote_addr.getHostAddress()+"\n");
                        //configure the DataGramPacket
                        packet.setAddress(remote_addr);
                        packet.setPort(2000);
                        //send the packet
                        socket.send(packet);
		UDPGUI.outputText.append("Packet sent at: " + new Date()+"\n");

		// Display packet information
		UDPGUI.outputText.append("Sent by  : " + remote_addr.getHostAddress()+"\n");
        		UDPGUI.outputText.append("Send from: " + packet.getPort()+"\n");
                packetStatus status = new packetStatus(socket, packet);
                status.start();
                long elapsedTime=0;
                while((elapsedTime < 300) && !received){
                    waitTime = System.currentTimeMillis();
                    elapsedTime = waitTime - startTime;
                
                }
                status.interrupt();
                if(received){
                    //socket.receive(packet);
                    stopTime = System.currentTimeMillis();
                    totalDuration = stopTime - startTime;
                    UDPGUI.outputText.append("Host acknowledged packet " + clientSent + " after " +
                            totalDuration + " ms"+"\n\n");
                    received = false;
                    clientSent++;
                    
                }
                else {UDPGUI.outputText.append("Packet was"
                        + " not acknowleged by the host!\n\n");
                        received = false;
                        clientSent++;
                }
  
		}
                catch (UnknownHostException ue){
                        UDPGUI.outputText.append("Unknown host "+hostname+"\n");
                }
		catch (IOException e){
			UDPGUI.outputText.append("Error - " + e+"\n");
		}
            }
        //UDPGUI.outputText.setText(clientOutput);
        }
    public int getClientSent(){
        return clientSent;
    }
    public String getClientOutput(){
        return clientOutput;
    }
    class packetStatus extends Thread{
        public DatagramSocket s;
        public DatagramPacket p;
        public packetStatus(DatagramSocket socket, DatagramPacket packet) throws IOException{
                s = socket;
                p = packet;
        }
            @Override
        public void run(){
            try{
                s.receive(p);
                received = true;
            }
            catch(Exception e){}
        }
    }
}
