/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothexample;

import javax.bluetooth.*;


/**
 *
 * @author Anna
 */
public class CoveExample {

    private static Object lock = new Object();
    
    public static void main(String[] args){
        
        CoveExample c = new CoveExample();
        
        c.doThis();
    }

    public void doThis() {
        try {

            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());

            try {

                synchronized (lock) {

                    lock.wait();

                }

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            System.out.println("Device Inquiry Completed. ");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
    
    public class MyDiscoveryListener implements DiscoveryListener {

        @Override
        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
            String name;
            try {
                name = btDevice.getFriendlyName(false);
            } catch (Exception e) {
                name = btDevice.getBluetoothAddress();
            }
            System.out.println("device found: " + name);
        }

        @Override
        public void inquiryCompleted(int arg0) {
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public void serviceSearchCompleted(int arg0, int arg1) {
        }

        @Override
        public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
        }
    }

}
