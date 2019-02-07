package com.example.robotbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnForward, btnBackward, btnStop, btnLeft, btnRight;
    MenuItem connect, disconnect;

    BT bt = new BT();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnForward = (Button) findViewById(R.id.btnForward);
        btnBackward = (Button) findViewById(R.id.btnBackward);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);

        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        if (!bt.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 100);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        connect = menu.getItem(0);
        disconnect = menu.getItem(1);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect:
                bt.connect();
                return true;
            case R.id.disconnect:
                bt.disconnect();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnForward:
                bt.sendData("8");
                break;
            case R.id.btnBackward:
                bt.sendData("2");
                break;
            case R.id.btnStop:
                bt.sendData("0");
                break;
            case R.id.btnLeft:
                bt.sendData("4");
                break;
            case R.id.btnRight:
                bt.sendData("6");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectEvent(BT.BTOnConnectEvent btOnConnectEvent) {
        Log.i("BT", btOnConnectEvent.message);
        connect.setEnabled(false);
        disconnect.setEnabled(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectException(BT.BTOnConnectException btOnConnectException) {
        Log.i("BT", btOnConnectException.message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDisconnectEvent(BT.BTOnDisconnectEvent btOnDisconnectEvent) {
        Log.i("BT", btOnDisconnectEvent.message);
        connect.setEnabled(true);
        disconnect.setEnabled(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDisconnectException(BT.BTOnDisconnectException btOnDisconnectException) {
        Log.i("BT", btOnDisconnectException.message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSentEvent(BT.BTOnDataSentEvent btOnDataSentEvent) {
        Log.i("BT", btOnDataSentEvent.message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSentException(BT.BTOnDataSentException btOnDataSentException) {
        Log.i("BT", btOnDataSentException.message);
        Toast.makeText(this, "Can't sent data. Error : " + btOnDataSentException.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        if (disconnect.isEnabled()) {
            bt.disconnect();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Bluetooth needs to be turned ON", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
