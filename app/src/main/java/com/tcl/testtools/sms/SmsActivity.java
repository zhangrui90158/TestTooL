package com.tcl.testtools.sms;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.testtools.R;

import java.util.Random;

/**
 * Created by ruizhang on 2016/9/29.
 */
public class SmsActivity extends Activity implements View.OnClickListener{
    private EditText getNumber,getText,getCount;
    private Button smsButton,differentButton;
    private String defaultSmsPkg;
    private String mySmsPkg;
    private ContentResolver resolver;
    private ContentValues values;
    private TextView hintText,toastText;
    private ProgressBar progressBar;
    private String smsNumber;
    private String smsText;
    private String smsCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        checkDefault();
        initView();
    }

    private void initView() {
        getNumber = (EditText) findViewById(R.id.getNumber);
        getText = (EditText) findViewById(R.id.getText);
        getCount = (EditText) findViewById(R.id.getCount);
        smsButton = (Button) findViewById(R.id.smsButton);
        hintText = (TextView) findViewById(R.id.hintText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toastText = (TextView) findViewById(R.id.toastText);
        differentButton = (Button) findViewById(R.id.smsDiffrentButton);
        smsText = getText.getText().toString();
        smsCount  = getCount.getText().toString();
        smsButton.setOnClickListener(this);
        differentButton.setOnClickListener(this);
    }
    public void checkDefault(){
        defaultSmsPkg = Telephony.Sms.getDefaultSmsPackage(this);
        mySmsPkg = this.getPackageName();
        if(!defaultSmsPkg.equals(mySmsPkg)) {
            //如果这个App不是默认的Sms App，则修改成默认的SMS APP
            //因为从Android 4.4开始，只有默认的SMS APP才能对SMS数据库进行处理
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, mySmsPkg);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.smsButton:
                new smsAsync().execute();
                break;
            case R.id.smsDiffrentButton:
                new smsAsync2().execute();
        }
    }
    public void writeSms(){
        if(mySmsPkg.equals(Telephony.Sms.getDefaultSmsPackage(SmsActivity.this))){
            if (getNumber.getText().toString().isEmpty()) {
                Toast.makeText(SmsActivity.this, "电话号码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if (getText.getText().toString().isEmpty()) {
                Toast.makeText(SmsActivity.this, "短信内容不能为空！", Toast.LENGTH_LONG).show();
                return;
            }
            resolver = getContentResolver();
            values = new ContentValues();
            for (int i = 0; i < Integer.parseInt(smsCount); i++) {
                smsNumber = getNumber.getText().toString();
                values.put(Telephony.Sms.DATE, System.currentTimeMillis());
                long dateSent = System.currentTimeMillis() - 5000;
                values.put(Telephony.Sms.DATE_SENT, dateSent);
                values.put(Telephony.Sms.ADDRESS, smsNumber);
                values.put(Telephony.Sms.BODY, smsText);
                values.put(Telephony.Sms.STATUS, Telephony.Sms.STATUS_COMPLETE);
                values.put(Telephony.Sms.TYPE, Telephony.Sms.MESSAGE_TYPE_INBOX);
                values.put(Telephony.Sms.READ, false);
                values.put(Telephony.Sms.SEEN, false);
                Uri uri = resolver.insert(Telephony.Sms.CONTENT_URI, values);
                if (uri != null) {
                    long uriId = ContentUris.parseId(uri);
                    System.out.println("uriId " + uriId);
                }
            }
        }else {
            Toast.makeText(SmsActivity.this,"不是默认短信应用",Toast.LENGTH_LONG).show();
        }
    }

    class smsAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            SmsActivity.this.writeSms();
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            hintText.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //生成完毕，关闭进度条并提示用户已完成
            progressBar.setVisibility(View.GONE);
            hintText.setVisibility(View.GONE);
            Toast.makeText(SmsActivity.this,"短信已生成完成",Toast.LENGTH_LONG).show();
            toastText.setText("已生信息："+ SmsActivity.this.getCount.getText().toString());
        }
    }
    class smsAsync2 extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            SmsActivity.this.writeSms2();
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            hintText.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //生成完毕，关闭进度条并提示用户已完成
            progressBar.setVisibility(View.GONE);
            hintText.setVisibility(View.GONE);
            Toast.makeText(SmsActivity.this,"短信已生成完成",Toast.LENGTH_LONG).show();
            toastText.setText("已生信息："+ SmsActivity.this.getCount.getText().toString());
        }
    }
    public void writeSms2(){
        if(mySmsPkg.equals(Telephony.Sms.getDefaultSmsPackage(SmsActivity.this))){
            if (getNumber.getText().toString().isEmpty()) {
                Toast.makeText(SmsActivity.this, "电话号码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if (getText.getText().toString().isEmpty()) {
                Toast.makeText(SmsActivity.this, "短信内容不能为空！", Toast.LENGTH_LONG).show();
                return;
            }
            resolver = getContentResolver();
            values = new ContentValues();
            Random random = new Random();
            for (int i = 0; i < Integer.parseInt(smsCount); i++) {
                smsNumber = "1501272"+ String.valueOf(random.nextInt(10000));
                values.put(Telephony.Sms.DATE, System.currentTimeMillis());
                long dateSent = System.currentTimeMillis() - 5000;
                values.put(Telephony.Sms.DATE_SENT, dateSent);
                values.put(Telephony.Sms.ADDRESS, smsNumber);
                values.put(Telephony.Sms.BODY, smsText);
                values.put(Telephony.Sms.STATUS, Telephony.Sms.STATUS_COMPLETE);
                values.put(Telephony.Sms.TYPE, Telephony.Sms.MESSAGE_TYPE_INBOX);
                values.put(Telephony.Sms.READ, false);
                values.put(Telephony.Sms.SEEN, false);
                Uri uri = resolver.insert(Telephony.Sms.CONTENT_URI, values);
                if (uri != null) {
                    long uriId = ContentUris.parseId(uri);
                    System.out.println("uriId " + uriId);
                }
            }
        }else {
            Toast.makeText(SmsActivity.this,"不是默认短信应用",Toast.LENGTH_LONG).show();
        }
    }
}
