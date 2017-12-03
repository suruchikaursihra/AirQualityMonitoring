package com.example.lenovo.airqualitymonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Thread thread=new Thread(){
            public void run(){
                try{

                    sleep(5000);


                }catch(Exception e){

                }finally {

                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);



                }
            }
        };thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}


