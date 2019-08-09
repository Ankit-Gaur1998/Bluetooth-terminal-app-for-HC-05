package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import Adapter.My_Adapter;
import Model.List_ITEM;

public class MainActivity<bluetoothAdapter> extends AppCompatActivity {
    BluetoothHeadset bluetoothHeadset;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bt_device;


    private Button bt_off_button;
    private Button bt_discoverable;
    private Button bt_paired_list;

    private RecyclerView recyclerView;
    private List<List_ITEM>list_items;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=(RecyclerView)findViewById(R.id.paired_LIST_VIEW_ID) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bt_off_button=(Button)findViewById(R.id.bt_OFF_ID);
        bt_discoverable=(Button)findViewById(R.id.discoverable_ID);
        bt_paired_list=(Button)findViewById(R.id.paired_devices_ID);



        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {

            Toast.makeText(MainActivity.this, "bluetooth is ON", Toast.LENGTH_SHORT).show();
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            if (bluetoothAdapter.isEnabled()) {
                Toast.makeText(MainActivity.this, "thanks for turning ON", Toast.LENGTH_SHORT).show();
            }
            else {
                startActivity(enableBtIntent);
            }
        }

        /*UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");*/




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        bt_off_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.disable();
            }
        });

        bt_discoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bluetoothAdapter.startDiscovery();
            Intent i=new Intent(bluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivity(i);
            }
        });

        bt_paired_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Set<BluetoothDevice> bluetoothDevices= bluetoothAdapter.getBondedDevices();
                list_items= new ArrayList<>();
                for (BluetoothDevice bluetoothDevice : bluetoothDevices)
                {

                    List_ITEM item=new List_ITEM(bluetoothDevice.getName());
                    list_items.add(item);

                }
                adapter=new My_Adapter(MainActivity.this,list_items);

                recyclerView.setAdapter(adapter);
//                Log.d("////",adapter);

                //__________________________________________
                //public InitializeSocket(BluetoothDevice bluetoothDevices){    try {        _socket = device.createRfcommSocketToServiceRecord(<Your app UDID>);    } catch (IOException e) {        //Error    }
                //_______________________________________

            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
