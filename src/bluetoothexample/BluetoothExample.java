/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bluetoothexample;




import com.intel.bluetooth.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
//import java.util.UUID;
import java.util.logging.Logger;
import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.swing.*;
import wiiremotej.*;
public class BluetoothExample {

    LocalDevice localDevice;
    DiscoveryAgent agent;
    RemoteDevice remoteDevice;
    
    Connection c;
    BlueCoveImpl i;
    UUID[] uuidSet = new UUID[5];
    int[] attrSet = {1,2,3,4,5}; 
    private JTextArea bluetoothSearchingOutputArea;
    private JFrame frame;
    private JPanel panel;
    private JButton blueSearchButton;
    Image wiiUPro = new ImageIcon(("src/images/wiiupro.png")).getImage();
    Image wiiMote = new ImageIcon(("src/images/wiimote.png")).getImage();
    Image iPhone = new ImageIcon(("src/images/iphone.png")).getImage();
    Image defaultPhone = new ImageIcon(("src/images/defaultphone.png")).getImage();
    Image petersPhone = new ImageIcon(("src/images/petersphone.png")).getImage();
    Image johnsPhone = new ImageIcon(("src/image/johnsphone.png")).getImage();
    Image mysteryDevice = new ImageIcon(("src/image/johnsphone.png")).getImage();
    ArrayList<Image> imagesToDraw;
    
    
    public static void main(String[] args) {
        
        BluetoothExample b = new BluetoothExample();
        b.setupGUI();
        
        
        
    }
    
    public void setupGUI(){
        
        blueSearchButton = new JButton("Search for Bluetooth devices.");
        blueSearchButton.addActionListener((ActionEvent e) -> {
            doBlueTooth();
            blueSearchButton.setEnabled(false);
        });
        
        bluetoothSearchingOutputArea = new JTextArea(19,0);
        bluetoothSearchingOutputArea.setText("Click the button to search for bluetooth devices.");
        bluetoothSearchingOutputArea.setEditable(false);
        JScrollPane scroller = new JScrollPane(bluetoothSearchingOutputArea);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        panel = new JPanel();
        frame = new JFrame("BlueTooth test");
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        panel.setBackground(Color.white);
        panel.add(new JLabel("Devices connected: "));
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getContentPane().add(BorderLayout.NORTH,scroller);
        frame.getContentPane().add(BorderLayout.SOUTH, blueSearchButton);
    }
    
    public void doBlueTooth(){
        imagesToDraw = new ArrayList<>();
        bluetoothSearchingOutputArea.setText("No devices connected yet");
        
        
        //UUID uuid1 = UUID.fromString("00001200-0000-1000-8000-00805f9b34fb");
        //UUID uuid2 = UUID.fromString("00001000-0000-1000-8000-00805f9b34fb");
        //UUID uuid3 = UUID.fromString("00001124-0000-1000-8000-00805f9b34fb");
        UUID uuid1 = new UUID(0x0000);
        UUID uuid2 = new UUID(0x0001);
        UUID uuid3 = new UUID(0x0002);
        UUID uuid4 = new UUID(0x0003);
        UUID uuid5 = new UUID(0x0004);
        
        uuidSet[0] = uuid1;
        uuidSet[1] = uuid2;
        uuidSet[2] = uuid3;
        uuidSet[3] = uuid4;
        uuidSet[4] = uuid5;
//        
        boolean b = LocalDevice.isPowerOn();
        System.out.println("Bluetooth is turned on? " + b);
        
        try{
          
            bluetoothSearchingOutputArea.append("\n" + "Searching for bluetooth devices..."); 
            localDevice = LocalDevice.getLocalDevice();
        agent = localDevice.getDiscoveryAgent();
        agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());
        panel.setBackground(Color.white);
        }
        catch(BluetoothStateException e){
            panel.setBackground(Color.red);
            System.err.println("BlueTooth is probably turned off.");
        }
        
        
        System.out.println("My bluetooth address is: " + localDevice.getBluetoothAddress());
        for(int i = 0; i < agent.retrieveDevices(1).length; i++){
            
           System.out.println("Bluetooth address from device is: " + agent.retrieveDevices(0)[i] );
           bluetoothSearchingOutputArea.append("\n" + agent.retrieveDevices(0)[i] + " is paired with this PC.");
        }
        
        WiiRemote remote;
        try {
            remote = WiiRemoteJ.connectToRemote("A4C0E13A5E54");
            System.out.println("connected to remote");
        } catch (Exception ex) {
            //deviceConnectedLabel.setText("");
        }
        
        
    }
    
    public class MyDiscoveryListener implements DiscoveryListener{

        RemoteDevice remote;
        @Override
        public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
            System.out.println("found a device!");
            remote = rd;
            
            
            try {
                System.out.println(rd.getFriendlyName(false));
                //deviceConnectedLabel.setText(rd.getFriendlyName(true));
                
                if(rd.getFriendlyName(false).equals("Nintendo RVL-CNT-01-UC")){
                  bluetoothSearchingOutputArea.append("\n" + "Wii U Pro Controller has been discovered. ");
                  draw(wiiUPro, false);
                }
                else if(rd.getFriendlyName(false).equals("Nintendo RVL-CNT-01")){
                  bluetoothSearchingOutputArea.append("\n" + "Wiimote has been discovered. "); 
                  draw(wiiMote,false);
                }
                else if(rd.getFriendlyName(false).contains("peter") || rd.getFriendlyName(false).contains("piotr")){
                  bluetoothSearchingOutputArea.append("\n" + "Peter's phone has been discovered. "); 
                  draw(petersPhone,false);
                }
                else if(rd.getFriendlyName(false).contains("jan") || rd.getFriendlyName(false).contains("john")){
                  bluetoothSearchingOutputArea.append("\n" + "Wiimote has been discovered. "); 
                  draw(johnsPhone,false);
                }
                else if(rd.getFriendlyName(false).contains("iPhone")){
                  bluetoothSearchingOutputArea.append("\n" + "iPhone has been discovered. "); 
                  draw(iPhone,false);
                }
                else if(rd.getFriendlyName(false).contains("hone")){
                  bluetoothSearchingOutputArea.append("\n" + rd.getFriendlyName(false) + "  has been discovered.");
                  draw(defaultPhone,false);
                }
                else{
                  bluetoothSearchingOutputArea.append("\n" + rd.getFriendlyName(false) + "  has been discovered.");
                  draw(mysteryDevice,false);  
                }
                
                frame.repaint();
                frame.validate();
                //agent.searchServices(attrSet, uuidSet, rd, this);
                //System.out.println("searched services but idk what happened");
            } catch (Exception ex) {
                System.out.println("unabletofind servesers");
            }
        }

        @Override
        public void servicesDiscovered(int i, ServiceRecord[] srs) {
            System.out.println("found services");
        }

        @Override
        public void serviceSearchCompleted(int i, int i1) {
            
            System.out.println("search Completed" + i + " " + i1);
            
            
        }

        @Override
        public void inquiryCompleted(int i) {
            System.out.println("inquirtyCompleted" + " " + i);
            bluetoothSearchingOutputArea.append("\n" + "Search completed.");
            blueSearchButton.setEnabled(true);
            draw(null, true);
            
//            try {
//                agent.selectService(new UUID(0x0000), ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
//                System.out.println("service done");
//            } catch (BluetoothStateException ex) {
//                System.out.println("couldn't find service");
//            }
            
        }
        
    }
    
    public void draw(Image image, boolean draw){
        imagesToDraw.add(image);
        
        if(draw == true){
        
            Graphics g = panel.getGraphics();
            int xLoc = 0;
            for (int i = 0; i < imagesToDraw.size(); i++) {
              Image im = imagesToDraw.get(i);
              
              g.drawImage(im, xLoc, 20, panel);
              
              if(im != null){
                  xLoc = xLoc + im.getWidth(frame);
              }
              
              
            }
        }
        
        
        
    }
    
    
    
    
}
