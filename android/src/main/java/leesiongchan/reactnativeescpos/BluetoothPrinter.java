package leesiongchan.reactnativeescpos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import io.github.escposjava.print.Printer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import android.util.Log;

public class BluetoothPrinter implements Printer {
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter adapter;

    private OutputStream printer = null;
    private final String address;

    public BluetoothPrinter(String address) {
        adapter = BluetoothAdapter.getDefaultAdapter();
        this.address = address;
    }

    public void open() throws IOException {
        BluetoothDevice device = adapter.getRemoteDevice(address);
        BluetoothSocket socket = null;

        try {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (Exception e) {Log.e("","Error creating socket");}

        try {
            socket.connect();
            Log.e("","Connected");
        } catch (IOException e) {
            Log.e("",e.getMessage());
            try {
                Log.e("","trying fallback...");

                socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                socket.connect();

                Log.e("","Connected");
            }
         catch (Exception e2) {
             Log.e("", "Couldn't establish Bluetooth connection!");
          }
        }
        printer = socket.getOutputStream();
    }

    public void write(byte[] command) {
        try {
            printer.write(command);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        printer.close();
    }
}
