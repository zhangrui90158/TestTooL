package com.tcl.testtools.phone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.testtools.R;

import java.util.Random;

/**
 * Created by ruizhang on 2016/8/23.
 */
public class EndActivity extends Activity {
    private ContentValues values;
    private ContentResolver cr;
    private ProgressBar progressBar;
    private static final String[] strName = {
            "赵","钱", "孙","李", "周", "吴", "郑","王",
            "冯", "陈", "楮", "卫" ,"蒋", "沈", "韩", "杨","朱", "秦", "尤", "许", "何" ,
            "吕", "施", "张","孔," ,"曹", "严", "华", "金","魏","陶", "姜","戚", "谢", "邹",
            "喻", "柏", "水", "窦", "章", "云", "苏" ,"潘", "葛" ,"奚", "范", "彭", "郎",
            "鲁","韦" ,"昌","马","苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐",
            "费", "廉" ,"岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
            "乐","于","时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄",
            "和"," 穆", "萧", "尹", "姚","邵", "湛","汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧",
            "计", "伏", "成", "戴", "谈" ,"宋", "茅", "庞", "熊"};
    private TextView textView;
    private Bundle bundle;
    private Random random;
    private TextView hintText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endactivity);
        bindViewS();
        getReturnDate();
    }
    //异步任务处理
    class MyAsync extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            //接收第一个参数
            String str = strings[0];
            String Number = strings[1];
            String emailName = strings[2];
            random = new Random();
            for (int i = 1; i <= Integer.parseInt(str); i++) {
                //随机获取名字跟号码
                String name = strName[random.nextInt(strName.length)] + strName[random.nextInt(strName.length)] +strName[random.nextInt(strName.length)];
                //随机生成号码
                String phoneNumber = Number + String.valueOf(random.nextInt(10000));
                //向联系人中插入数据
                //初始化cr、values
                cr = getContentResolver();
                values = new ContentValues();
                Uri uri = cr.insert(ContactsContract.RawContacts.CONTENT_URI, values);
                long raw_contact_id = ContentUris.parseId(uri);
                values.clear();
                //插入人名
                values.put(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID,raw_contact_id);
                values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name);
                values.put(ContactsContract.CommonDataKinds.StructuredName.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                cr.insert(ContactsContract.Data.CONTENT_URI, values);
                //插入电话号码
                values.clear();
                values.put(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, raw_contact_id);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
                values.put(ContactsContract.CommonDataKinds.Phone.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                cr.insert(ContactsContract.Data.CONTENT_URI, values);
                //插入邮箱
                values.clear();
                values.put(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID,raw_contact_id);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Email.DATA, emailName);
                values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                cr.insert(ContactsContract.Data.CONTENT_URI, values);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //初始化进度条可见
            progressBar.setVisibility(View.VISIBLE);
            hintText.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //生成完毕，关闭进度条并提示用户已完成
            progressBar.setVisibility(View.GONE);
            hintText.setVisibility(View.GONE);
            Toast.makeText(EndActivity.this,"联系人已生成完成",Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            textView.setText("已生成联系人："+b.getString("number"));
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    public void bindViewS(){
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        hintText = (TextView) findViewById(R.id.hintText);
    }
    public void getReturnDate(){
        //获取传过来的参数
        Intent i = getIntent();
        bundle = i.getExtras();
        String number = bundle.getString("phoneNumber");
        String str = bundle.getString("number");
        String email = bundle.getString("email");
        new MyAsync().execute(new String[]{str,number,email});
    }
}
