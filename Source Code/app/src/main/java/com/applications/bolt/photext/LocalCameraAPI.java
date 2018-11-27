package com.applications.bolt.photext;
/*
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class LocalCameraAPI {
    Context context;
    String[] cameraIdList;
    private static int CAMERA_REQUEST = 1888;
    LocalCameraAPI(Context con){
        this.context=con;
        getCamera();
         }
    void getCamera() {

        if(checkSelfPermission(context, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
            //Add Friendlyness
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
           }
        CameraManager cam = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = cam.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
    void debug(){

    }
}*/
