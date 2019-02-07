package com.example.mohandassample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBroadcast, btnIntentActivity, btnServiceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBroadcast = (Button) findViewById(R.id.btnBroadcast);
        btnIntentActivity = (Button) findViewById(R.id.btnIntentActivity);
        btnServiceActivity = (Button) findViewById(R.id.btnServiceActivity);

        btnBroadcast.setOnClickListener(this);
        btnIntentActivity.setOnClickListener(this);
        btnServiceActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBroadcast:
                Intent broadcastIntent = new Intent("com.example.mohandassample.CUSTOM_INTENT");
                sendBroadcast(broadcastIntent);
                break;
            case R.id.btnIntentActivity:
                startActivity(new Intent(this, IntentActivity.class));
                break;
            case R.id.btnServiceActivity:
                startActivity(new Intent(this, ServiceActivity.class));
                break;
        }
    }
}
