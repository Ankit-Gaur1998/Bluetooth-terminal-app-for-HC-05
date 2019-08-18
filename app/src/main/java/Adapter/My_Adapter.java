package Adapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Handler;
import android.renderscript.Type;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.Bluetooth_data;
import com.example.bluetooth.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import Model.List_ITEM;

import static androidx.core.content.ContextCompat.startActivity;

public class My_Adapter extends RecyclerView.Adapter<My_Adapter.ViewHolder> {

    private Context context;
    private List<Model.List_ITEM> list_items;

    BluetoothSocket bluetoothSocket;


     BluetoothDevice bt_devicee;

    private OutputStream outputStream;
    private InputStream  inputStream;

    public My_Adapter(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String s;
    //private Type.CubemapFace cubemapFace;

    public My_Adapter(Context context, List<List_ITEM> list_items) {
        this.context = context;
        this.list_items = list_items;
    }

    @NonNull
    @Override
    public My_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull My_Adapter.ViewHolder holder, int position) {
        Model.List_ITEM item=list_items.get(position);

        holder.name_of_BT.setText(item.getName());

    }

    @Override
    public int getItemCount() {
          return list_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name_of_BT;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            name_of_BT=(TextView)view.findViewById(R.id.tt_NAME);
            cardView=(CardView)view.findViewById(R.id.card_view);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,name_of_BT.getText().toString() + "  "+ getAdapterPosition(),Toast.LENGTH_LONG).show();
                    s=name_of_BT.getText().toString();

                     BluetoothAdapter bluetoothAddapter = BluetoothAdapter.getDefaultAdapter();

                    Set<BluetoothDevice> bluetoothDevces= bluetoothAddapter.getBondedDevices();
                    for (BluetoothDevice bluetoothDevice : bluetoothDevces) {

                        if (bluetoothDevice.getName().equals(s)) {
                            //bt_devicee = bluetoothDevice;
                            Intent intent=new Intent(context, Bluetooth_data.class);
                            intent.putExtra("bt_name",bluetoothDevice);
                            context.startActivity(intent);
                            //Log.d("/**", bt_devicee.getName().toString()+"   "+bt_devicee.getAddress().toString());
                            break;


                        }
                    }
//                    Log.d("/**", bt_devicee.getName().toString()+"   "+bt_devicee.getAddress().toString());
//                    UUID uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//                    try {
//                        bluetoothSocket=bt_devicee.createRfcommSocketToServiceRecord(uuid);
//                        bluetoothSocket.connect();
//                        outputStream=bluetoothSocket.getOutputStream();
//                        inputStream=bluetoothSocket.getInputStream();
//                        beginListenData();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                    /**TODO see this code from this link " http://wintechtutorials.com/blog/android-send-print-command-bluetooth-printer-mobile/ "*/

                }
            });
        }



    }

//    private void beginListenData() {
//        final Handler handler =new Handler();
//        final byte delimiter=10;
//        String msg="MY name is ankit";
//
//        // stopWorker =false;
//        // readBufferPosition=0;
//        // readBuffer = new byte[1024];
//
//        try {
//            outputStream.write(msg.getBytes());
//            int byteAvailable = inputStream.available();
//                byte[] packetByte = new byte[byteAvailable];
//                inputStream.read(packetByte);
//                Log.d("------------", String.valueOf(inputStream.read(packetByte)));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//
//    }
}
