package com.example.compass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView mCompassDegreeTxt;
    TextView mCompassDirectionTxt;
    TextView m1;
    TextView m2;
    DirectionView directionView;
    SensorManager manager;
    private static Context context = null;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera = null;
    private SurfaceView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();

        mCompassDegreeTxt = findViewById(R.id.textView);
        mCompassDirectionTxt = findViewById(R.id.textView2);
        m1 = findViewById(R.id.text1);
        m2 = findViewById(R.id.text2);
        directionView = findViewById(R.id.direction_view);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        camera = Camera.open();
        mPreview = findViewById(R.id.surfaceview);
        mPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try{
                    camera.setDisplayOrientation(90);
                    camera.setPreviewDisplay(mPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                Log.e("TAG","?????????surface"+mPreview.getHolder().toString());
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });
    }
    @Override
    protected void onResume() {

        /**
         *  ?????????????????????
         *  ??????SensorManager?????????????????????Sensor???????????????
         */
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //????????????????????????????????????
        manager.registerListener((SensorEventListener) this, sensor,
                SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
        if(camera != null){
            camera.startPreview();
        }
    }

    @Override
    protected void onPause() {
        //??????????????????????????????????????????
        manager.unregisterListener((SensorEventListener) this);
        super.onPause();
        if(camera != null){
            camera.stopPreview();
        }
    }
    protected void onDestroy() {

        super.onDestroy();
        if(camera != null){
            camera.release();
        }
    }
    private float predegree = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = event.values[0];// ??????????????????
        float[] values = event.values;
        float X = -values[0];
        float Y = -values[1];
        float Z = -values[2];
        if (Y < 45) {
            if(camera != null){
                camera.stopPreview();
            }
            predegree = -degree;
            m1.setText("");
            m2.setText("");
            directionView.rotate = predegree;
            directionView.postInvalidate();
            directionView.lim = 0;
            if((degree<360&&degree>337.5)||(degree>0&&degree<22.5)) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");
            }else if(degree>22.5&&degree<67.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");
            }else if(degree>67.5&&degree<112.5) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");
            }else if(degree>112.5&&degree<157.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");
            }else if(degree>157.5&&degree<202.5) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");

            }else if(degree>202.5&&degree<247.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");

            }else if(degree>247.5&&degree<292.5) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");

            }else if(degree>292.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");
            }

        }else if(Y>45){
            if(camera != null){
            camera.startPreview();
            }
            predegree = -degree;
            if(degree>0&&degree<180) {
                m1.setText("<=???");
                m2.setText("");
            }else if(degree>180&&degree<360){
                m1.setText("");
                m2.setText("???=>");
            }
            directionView.lim = 1;
            if((degree<360&&degree>337.5)||(degree>0&&degree<22.5)) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");

            }else if(degree>22.5&&degree<67.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");

            }else if(degree>67.5&&degree<112.5) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");

            }else if(degree>112.5&&degree<157.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");

            }else if(degree>157.5&&degree<202.5) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");

            }else if(degree>202.5&&degree<247.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");
            }else if(degree>247.5&&degree<292.5) {
                mCompassDegreeTxt.setText("???" + ((int) degree) + "??");

            }else if(degree>292.5) {
                mCompassDegreeTxt.setText("??????" + ((int) degree) + "??");
            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (accuracy == -1) {
            mCompassDirectionTxt.setText("SENSOR_STATUS_NO_CONTACT,?????????????????????8");
        }
        else if (accuracy == 0){
            mCompassDirectionTxt.setText("SENSOR_STATUS_UNRELIABLE???????????????????????????8");
        }
        else if (accuracy == 1){
            mCompassDirectionTxt.setText("SENSOR_STATUS_ACCURACY_LOW ?????????????????????8");
        }
        else if (accuracy == 2){
            mCompassDirectionTxt.setText("SENSOR_STATUS_ACCURACY_MEDIUM??????????????????????????????8???");
        }
        else if (accuracy == 3){
            mCompassDirectionTxt.setText("SENSOR_STATUS_ACCURACY_HIGH????????????");
        }


        }
    private void initPermission() {
        String permissions[] = {
                Manifest.permission.CAMERA,

        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //?????????????????????????????????.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }


}
