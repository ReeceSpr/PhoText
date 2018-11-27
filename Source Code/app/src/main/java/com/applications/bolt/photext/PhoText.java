package com.applications.bolt.photext;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import java.lang.Object.*;
import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class PhoText extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pho_text);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getCamera();



    }
    /*
      Local Camera Import for imageFrame
       */
    void getCamera() {
        String[] cameraIdList={"Null"};
        Context context = getApplicationContext();
        CallBackCamera callback = new CallBackCamera();
        android.os.Handler handlerCamera = new android.os.Handler();
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            //Add Friendlyness
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1888);
        }
        CameraManager cam = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
                cameraIdList = cam.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        for(String i:cameraIdList){
            System.out.println(i);
        }
        try {
            cam.openCamera(cameraIdList[0],callback,handlerCamera);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
