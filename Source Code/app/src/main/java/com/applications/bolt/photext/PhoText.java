package com.applications.bolt.photext;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.lang.Object.*;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class PhoText extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextMessage;
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    int counter=0;
    String rawString;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode)
            {
                case RequestCameraPermissionID:
                {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        try {
                            cameraSource.start(cameraView.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        }
                }
            }
    }

        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_previous:
                    mTextMessage.setText(R.string.title_previous);
                    return true;
                case R.id.navigation_capture:
                    mTextMessage.setText(R.string.title_capture);
                    return true;
                case R.id.navigation_help:
                    mTextMessage.setText(R.string.title_help);
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
        getCamera(savedInstanceState);
        addButtonListeners();
    }

    public void addButtonListeners(){
        Button num = (Button)findViewById(R.id.buttonNum);
        Button all = (Button)findViewById(R.id.buttonAll);
        Button text = (Button)findViewById(R.id.buttonText);
        num.setOnClickListener(this);
        all.setOnClickListener(this);
        text.setOnClickListener(this);
    }

    public void onClick(View v) {
        String output = "";
        switch (v.getId()) {
            case  R.id.buttonNum: {
                if(rawString!=null){
                    output = filterNum();
                    ClipData clip = ClipData.newPlainText("NUMBERS", output);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(output);
                }
                break;
            }

            case R.id.buttonAll: {
                if(rawString!=null){
                    output = rawString;
                    ClipData clip = ClipData.newPlainText("NUMBERS", output);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(output);
                }
                break;
            }
            case R.id.buttonText: {
                if(rawString!=null){
                    output = filterText();
                    ClipData clip = ClipData.newPlainText("NUMBERS", output);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(output);
                }
            }
            //.... etc
        }
    }

    public void getCamera(Bundle savedInstanceState) {
        cameraView = findViewById(R.id.surface_view);
        textView = findViewById(R.id.text_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Dependencies are not available");

        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(720, 1280)
                    .setRequestedFps(60)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PhoText.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (counter < 4) {
                        counter++;
                    } else {
                        counter = 0;
                        if (items.size() != 0) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i = 0; (i < items.size()); i++) {
                                        TextBlock item = items.valueAt(i);
                                        stringBuilder.append(item.getValue());
                                        stringBuilder.append(" ");
                                    }
                                    textView.setText(stringBuilder.toString());
                                    rawString = stringBuilder.toString();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /*Filters the numbers from the strings
    IMPLEMENTED
    */
    public String filterNum(){
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < rawString.length(); i++){
            if (Character.isDigit(rawString.charAt(i))){
                out.append(rawString.charAt(i));
            }
        }
        return out.toString();
        }

    /*Filters the text from the strings
     Would like spell check compare
     */
    public String filterText(){
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < rawString.length(); i++){
            if (Character.isLetter(rawString.charAt(i))){
                out.append(rawString.charAt(i));
            }
        }
        return out.toString();
    }


    //Forced Declaration, unimplemented
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


