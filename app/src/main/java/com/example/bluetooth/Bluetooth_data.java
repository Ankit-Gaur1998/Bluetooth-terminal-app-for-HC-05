package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import Adapter.My_Adapter;

public class Bluetooth_data extends AppCompatActivity {

    private EditText editText;
    private Button send_button;

    private TextView textView;

    BluetoothSocket bluetoothSocket;
    BluetoothDevice bt_devicee;
    private OutputStream outputStream;
    private InputStream inputStream;
    Thread thread;


    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_data);

        editText = (EditText) findViewById(R.id.editText_ID);
        send_button = (Button) findViewById(R.id.send_button_ID);

        textView = (TextView) findViewById(R.id.textView_screen);
        Toast.makeText(Bluetooth_data.this, "ON CREATE", Toast.LENGTH_SHORT).show();

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bt_devicee = (BluetoothDevice) bundle.get("bt_name");
            Log.d("11111111", bt_devicee.getName());
        }
        try {
            final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            bluetoothSocket = bt_devicee.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            if (bluetoothSocket.isConnected()) {
                Toast.makeText(Bluetooth_data.this, "Bluetooth :   "+bt_devicee.getName() + " is connected 😀", Toast.LENGTH_LONG).show();
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();
                beginListenData();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /**TODO BUTTON SEND*/

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String ss = editText.getText().toString();
                    if (ss.isEmpty()) {
                        Toast.makeText(Bluetooth_data.this, "Add data to send", Toast.LENGTH_LONG).show();
                    } else {
                        outputStream.write(ss.getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editText.setText("");
            }
        });
    }

        private void beginListenData() {
            try {
                final Handler handler = new Handler();
                final byte delimiter = 10;
                stopWorker = false;
                readBuffer = new byte[1024];
                readBufferPosition = 0;
                final String ss = editText.getText().toString();

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (!Thread.currentThread().isInterrupted()&&!stopWorker)
                        {
                            try {
                                int byteAvailable=inputStream.available();
                                if (byteAvailable>0){
                                    byte[] packetbyte=new byte[byteAvailable];
                                    inputStream.read(packetbyte);

                                    for (int i=0;i<byteAvailable;i++){
                                        byte b=packetbyte[i];
                                        if (b==delimiter){
                                            byte[] encodedByte=new byte[readBufferPosition];
                                            System.arraycopy(
                                                    readBuffer,0,
                                                    encodedByte,0,
                                                    encodedByte.length
                                            );
                                            final String data=new String(encodedByte,"US-ASCII");
                                            readBufferPosition=0;
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {


                                                    textView.setText(textView.getText().toString()+data);
                                                    Log.d("------------",data);

                                                }
                                            });

                                        }
                                        else {
                                            readBuffer[readBufferPosition++]=b;
                                        }
                                    }
                                }
                            }catch (Exception ex)
                            {
                                stopWorker=true;
                            }
                        }
                    }
                });
                thread.start();

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(Bluetooth_data.this,"ON PAUSE",Toast.LENGTH_SHORT).show();
//
//    }
    //    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        Toast.makeText(Bluetooth_data.this,"ON POST_RESUME",Toast.LENGTH_SHORT).show();
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(Bluetooth_data.this,"ON START",Toast.LENGTH_LONG).show();
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(Bluetooth_data.this,"ON DESTROY",Toast.LENGTH_SHORT).show();
        stopWorker=true;
        try {
            inputStream.close();
            outputStream.close();
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(Bluetooth_data.this,"ON STOP",Toast.LENGTH_SHORT).show();
//    }
}
