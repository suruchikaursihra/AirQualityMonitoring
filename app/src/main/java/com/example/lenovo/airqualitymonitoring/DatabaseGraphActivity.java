package com.example.lenovo.airqualitymonitoring;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class DatabaseGraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BluetoothSPP bt;

    TextView date,time,co2,co;
    private LineGraphSeries<DataPoint> series;
    private LineGraphSeries<DataPoint> series2;

    private int lastX = 0;
    Calendar calendar = Calendar.getInstance();

    MyHelper mh;
    SQLiteDatabase sqld;
    int c1,c2;
    String[] area = { "India", "USA", "China", "Japan", "Other",  };
    String item;
    String strDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_graph);

        bt = new BluetoothSPP(this);

        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);
        co=(TextView)findViewById(R.id.co);
        co2=(TextView)findViewById(R.id.co2);

        co2.setText(Html.fromHtml("CO<sub>2</sub>"));

        mh=new MyHelper(this);
        sqld=mh.getWritableDatabase();

        Spinner spinner = (Spinner) findViewById(R.id.Location);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Paschim Vihar");
        categories.add("JanakPuri");
        categories.add("Jail Road");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);

        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        series2 = new LineGraphSeries<DataPoint>();
        graph2.addSeries(series2);
        series.setColor(Color.GREEN);
        series2.setColor(Color.RED);

        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        Viewport viewport2 = graph2.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(10);
        viewport.setScrollable(true);
        viewport2.setXAxisBoundsManual(true);
        viewport2.setMinX(0);
        viewport2.setMaxX(10);
        viewport2.setScrollable(true);

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);// It will remove the background grids
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);// remove horizontal x labels and line
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);// remove vertical labels and lines

        //graph.setDrawBackground(true);
        graph.setBackgroundColor(getResources().getColor(android.R.color.background_dark));

        graph2.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);// It will remove the background grids
        graph2.getGridLabelRenderer().setHorizontalLabelsVisible(false);// remove horizontal x labels and line
        graph2.getGridLabelRenderer().setVerticalLabelsVisible(false);// remove vertical labels and lines

        //graph.setDrawBackground(true);
        graph2.setBackgroundColor(getResources().getColor(android.R.color.background_dark));

        series.setTitle("CO");
        series2.setTitle("CO2");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextSize(50);
        graph.getLegendRenderer().setTextColor(Color.WHITE);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph2.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setTextSize(50);
        graph2.getLegendRenderer().setTextColor(Color.WHITE);
        graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);


        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
               // Toast.makeText(DatabaseGraphActivity.this, message, Toast.LENGTH_SHORT).show();
                DataPutValues(message);
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();

                            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnConnect = (ImageButton)findViewById(R.id.BluetoothButton);
        btnConnect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        SimpleDateFormat sdf =new SimpleDateFormat("dd/mm/yyyy");
        Date d = calendar.getTime();
         strDate= sdf.format(d);
        date.setText(strDate);
    }

     public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                bt.connect(data);
            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);

            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    double ppm1=0,ppm2=0;
    void DataPutValues(String m){

        int a;
        double b,r;//ppm1=0,ppm2=0;
        String s1=m.substring(m.lastIndexOf(":")+2);

        if(m.indexOf("MQ-7") != -1)
        {
            a=Integer.parseInt(s1);
            b=(double)a/1023;
            r=(double) (5-b)/b;
            ppm1=(Math.pow(r,(-1.560228)))*94.12968;
            co.setText(String.format( "CO: %.2f", ppm1));
        }
        else
        {
            a=Integer.parseInt(s1);
            b=(double)a/1023;
            r=(double) (5-b)/b;
            ppm2=(Math.pow(r,(-2.710833)))*110.9148*100000;
            co2.setText(Html.fromHtml("CO<sub>2</sub>: ")+String.format( "%.2f", ppm2));
        }

        SimpleDateFormat sdf =new SimpleDateFormat("hh:mm:ss a");
        Date d = calendar.getTime();
        String strTime= sdf.format(d);
        time.setText(strTime);
        addEntry(ppm1,ppm2,strTime);
    }


    private void addEntry(double d1,double d2,String time) {
        mh.insertData(item,strDate,time,d1,d2);
        series.appendData(new DataPoint(lastX++,d1), true, 10);
        series2.appendData(new DataPoint(lastX++,d2), true, 10);


    }

}
