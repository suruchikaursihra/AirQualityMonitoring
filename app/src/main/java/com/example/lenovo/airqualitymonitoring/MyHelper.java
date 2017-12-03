package com.example.lenovo.airqualitymonitoring;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MyHelper  extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context, "mygraph1", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="create table if not exists mytab(location VARCHAR(20),date VARCHAR(20),time VARCHAR(20),co DOUBLE(10,2),co2 DOUBLE(10,2));";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String l,String d,String t,Double co,Double co2)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("location",l);
        contentValues.put("date",d);
        contentValues.put("time",t);
        contentValues.put("co",co);
        contentValues.put("co2",co2);

        db.insert("mytab",null,contentValues);

        System.out.println("Values inserted into database");
    }
}
