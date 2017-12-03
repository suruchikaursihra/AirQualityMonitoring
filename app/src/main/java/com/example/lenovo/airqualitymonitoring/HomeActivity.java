package com.example.lenovo.airqualitymonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button realtimebtn,show_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        realtimebtn= (Button) findViewById(R.id.realTime);
        realtimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I=new Intent(HomeActivity.this,DatabaseGraphActivity.class);
                startActivity(I);
            }
        });
        show_button= (Button) findViewById(R.id.show);
        show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I=new Intent(HomeActivity.this,ShowDataActivity.class);
                startActivity(I);
            }
        });
    }
}
