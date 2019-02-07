package com.example.robotbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by aravi on 20/12/17.
 */

public class BT {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    boolean stopWorker;

    /**
     * To connect with bluetooth device
     *
     */
    void connect() {
        try {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice dev : pairedDevices) {
                    if (dev.getName().equals("HC-05")) {
                        mmDevice = dev;
                        break;
                    }
                }
            }
            if (mmDevice != null) {
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();

                EventBus.getDefault().post(new BTOnConnectEvent("Connected"));
            }
        } catch (Exception e) {
            EventBus.getDefault().post(new BTOnConnectException(e.getMessage()));
        }
    }

    /**
     * To send data
     *
     * @param msg - the message to be sent
     *
     */
    void sendData(String msg) {
        try {
            msg += "\n\r";
            mmOutputStream.write(msg.getBytes());
            EventBus.getDefault().post(new BTOnDataSentEvent("Data Sent"));
        } catch (Exception e) {
            EventBus.getDefault().post(new BTOnDataSentException(e.getMessage()));
        }
    }

    /**
     * To disconnect with bluetooth device
     *
     */
    void disconnect() {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            EventBus.getDefault().post(new BTOnDisconnectEvent("Disconnected"));
        } catch (Exception e) {
            EventBus.getDefault().post(new BTOnDisconnectException(e.getMessage()));
        }
    }

    public class BTOnConnectEvent {

        public String message;

        BTOnConnectEvent(String message) {
            this.message = message;
        }

    }

    public class BTOnConnectException {

        public String message;

        BTOnConnectException(String message) {
            this.message = message;
        }

    }

    public class BTOnDataSentEvent {

        public String message;

        BTOnDataSentEvent(String message) {
            this.message = message;
        }

    }

    public class BTOnDataSentException {

        public String message;

        BTOnDataSentException(String message) {
            this.message = message;
        }

    }

    public class BTOnDisconnectEvent {

        public String message;

        BTOnDisconnectEvent(String message) {
            this.message = message;
        }

    }

    public class BTOnDisconnectException {

        public String message;

        BTOnDisconnectException(String message) {
            this.message = message;
        }

    }
}
