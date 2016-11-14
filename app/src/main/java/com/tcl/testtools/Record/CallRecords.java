package com.tcl.testtools.Record;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tcl.testtools.R;

import java.util.Random;

/**
 * Created by ruizhang on 2016/11/10.
 */
public class CallRecords extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private Button btnSure;
    private RadioGroup radioGroup;
    private EditText number, count;
    private String phoneNumber, phoneCount;
    public static final int PERMISSON_CODE = 1;
    private ProgressDialog progressDialog;
    private int condition;
    private RadioButton rd;
    private Random random;
    private ToggleButton togButton;
    private boolean same =true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callrecords);
        initView();
        checkPermisson();
    }

    private void initView() {
        btnSure = (Button) findViewById(R.id.buttonEnd);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        number = (EditText) findViewById(R.id.editTextNumber);
        count = (EditText) findViewById(R.id.editTextCount);
        togButton = (ToggleButton) findViewById(R.id.togButton);
        togButton.setOnCheckedChangeListener(this);
        btnSure.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        random = new Random();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEnd:
                new MyAsyncTask().execute();
                break;
        }
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        rd = (RadioButton) findViewById(checkedId);
        if (rd.getText().equals("已接电话")) {
            condition = 1;
        }
        if (rd.getText().equals("已拨电话")) {
            condition = 2;
        }
        if (rd.getText().equals("未接电话")) {
            condition = 3;
        }
        if (rd.getText().equals("随机类型")) {
            condition = random.nextInt(3) + 1;
        }
    }


    private void insertCalls() {
        if(same){
            int j, ii;
            j = random.nextInt(6000) + 1;
            ii = random.nextInt(2);
            ContentValues cv = new ContentValues();
            ContentResolver cr = getContentResolver();
            phoneCount = count.getText().toString();
            phoneNumber = number.getText().toString();
            for (int i = 0; i < Integer.parseInt(phoneCount); i++) {
                cv.put("number", phoneNumber);
                cv.put("type", condition);
                cv.put("date", Long.valueOf(System.currentTimeMillis()));
                cv.put("duration", j);
                cv.put("new", ii);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CallRecords.this,"未获取通话记录权限",Toast.LENGTH_LONG).show();
                }else {
                    cr.insert(CallLog.Calls.CONTENT_URI, cv);
                }
            }
        }else {
            int j, ii;
            j = random.nextInt(6000) + 1;
            ii = random.nextInt(2);
            ContentValues cv = new ContentValues();
            ContentResolver cr = getContentResolver();
            phoneCount = count.getText().toString();
            for (int i = 0; i < Integer.parseInt(phoneCount); i++) {
                String phoneNumber = String.valueOf(Long.valueOf(number.getText().toString())+random.nextInt(10000));
                cv.put("number", phoneNumber);
                cv.put("type", condition);
                cv.put("date", Long.valueOf(System.currentTimeMillis()));
                cv.put("duration", j);
                cv.put("new", ii);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CallRecords.this,"未获取通话记录权限",Toast.LENGTH_LONG).show();
                }else {
                    cr.insert(CallLog.Calls.CONTENT_URI, cv);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermisson(){
        if(checkSelfPermission(Manifest.permission.WRITE_CALL_LOG)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(CallRecords.this,"已获取通话记录权限",Toast.LENGTH_LONG).show();
        }else {
            requestPermissions(new String[]{Manifest.permission.WRITE_CALL_LOG},PERMISSON_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(CallRecords.this,"已获取通话记录权限",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(CallRecords.this,"已拒绝通话记录权限",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.togButton:
                if(buttonView.isChecked()){
                    same = true;
                }else {
                    same = false;
                }
        }
    }


    class MyAsyncTask extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {
            insertCalls();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =  new ProgressDialog(CallRecords.this);
            progressDialog.setMessage("记录生成中请不要退出........");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(CallRecords.this,"已生成完成",Toast.LENGTH_SHORT).show();
        }
    }

}
