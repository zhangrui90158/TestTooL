package com.tcl.testtools.phone;

import android.content.pm.PackageManager;
import android.widget.Toast;

import com.tcl.testtools.MainActivity;

/**
 * Created by ruizhang on 2016/8/9.
 */
public class Permisson extends MainActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleGrantResults(requestCode,grantResults);
    }
    private void handleGrantResults(int requestCode, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 获得权限后执行xxx
            } else {
                // Permission Denied 拒绝后xx的操作。
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
