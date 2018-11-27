package com.applications.bolt.photext;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.OutputConfiguration;
import android.support.annotation.NonNull;
import android.view.Surface;

public class CallBackCamera extends CameraDevice.StateCallback {
    @Override
    public void onOpened(@NonNull CameraDevice cameraDevice) {
        CaptureRequest.Builder test = null;
        SurfaceTexture testSurfaceTexture = new SurfaceTexture(1);
        Surface testSurface = new Surface(testSurfaceTexture);

        try {
            test = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        test.addTarget(testSurface);
        test.build();
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice cameraDevice) {

    }

    @Override
    public void onError(@NonNull CameraDevice cameraDevice, int i) {

    }
}
