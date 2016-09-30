package com.tcl.testtools.phone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tcl.testtools.R;

/**
 * Created by ruizhang on 2016/9/27.
 */
public class PhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private Button add;
    private EditText editText,numberEdit,emailEdit;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_main);
        initView();
        checkPermission();
    }
    private void checkPermission(){
        //权限判断
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //申请 WRITE_CONTACTS 权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAdd:
                //用intent在ACTIVITY之间传输参数
                intent = new Intent(PhoneActivity.this,EndActivity.class);
                //获取输入的数字
                String number = editText.getText().toString();
                String phoneNumber = numberEdit.getText().toString();
                String email = emailEdit.getText().toString();
                bundle = new Bundle();
                bundle.putString("number",number);
                bundle.putString("phoneNumber",phoneNumber);
                bundle.putString("email",email);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }
    //初始化控件
    public void initView(){
        add = (Button) findViewById(R.id.buttonAdd);
        editText = (EditText) findViewById(R.id.editText);
        numberEdit = (EditText) findViewById(R.id.nuberEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        add.setOnClickListener(this);
    }
}
