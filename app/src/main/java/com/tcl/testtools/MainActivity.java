package com.tcl.testtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.tcl.testtools.phone.PhoneActivity;
import com.tcl.testtools.sms.SmsActivity;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button phoneButton, smsButton;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, PhoneActivity.class);
        initView();
    }

    private void initView() {
        phoneButton = (Button) findViewById(R.id.phoneButton);
        smsButton = (Button) findViewById(R.id.smsButton);
        phoneButton.setOnClickListener(this);
        smsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.phoneButton:
                intent = new Intent(MainActivity.this, PhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.smsButton:
                intent = new Intent(MainActivity.this,SmsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
