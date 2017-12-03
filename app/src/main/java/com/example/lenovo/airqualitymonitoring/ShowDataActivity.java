package com.example.lenovo.airqualitymonitoring;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;

public class ShowDataActivity extends AppCompatActivity {

    MyHelper mh;
    SQLiteDatabase sqld;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        mh=new MyHelper(this);
        sqld=mh.getWritableDatabase();
        show=(TextView)findViewById(R.id.showData);
        show.setMovementMethod(new ScrollingMovementMethod());
        show.setText("");

        getDataPoint();

    }

    private void getDataPoint() {
        String[] columns={"location","date","time","co","co2"};
        Cursor cursor=sqld.query("mytab",columns,null,null,null,null,null);
        String l,d,t;
        Double c1,c2;
        for(int i=0;i<cursor.getCount();i++)
        {
            cursor.moveToNext();
            l=cursor.getString(0);
            d=cursor.getString(1);
            t=cursor.getString(2);
            c1=cursor.getDouble(3);
            c2=cursor.getDouble(4);
            System.out.println(l+d+t+c1+c2);
            show.append("Location: "+l+"\n");
            show.append("Date: "+d+"  Time: "+t+"\n");
            show.append("CO: "+c1+" CO2: "+c2+"\n\n");

        }

    }
}
