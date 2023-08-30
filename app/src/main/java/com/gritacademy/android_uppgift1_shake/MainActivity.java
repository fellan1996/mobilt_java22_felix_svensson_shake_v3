package com.gritacademy.android_uppgift1_shake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button activityBtn = findViewById(R.id.activityBtn);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Jadååå", Toast.LENGTH_LONG).show();
                Intent A1 = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(A1);
            }
        });
    }

    protected void onPause() {

        super.onPause();

        senSensorManager.unregisterListener(this);

    }

    protected void onResume() {

        super.onResume();

        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if ( speed > SHAKE_THRESHOLD) {
                float lessDecimalsX = Math.round(x * 10000);
                float lessDecimalsY = Math.round(y * 10000);
                float lessDecimalsZ = Math.round(z * 10000);
                String toastText = " X: " + lessDecimalsX / 10000 + "   Y: " + lessDecimalsY / 10000 + "   Z: " + lessDecimalsZ / 10000;
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_LONG).show();
                }
                last_x = x;
                last_y = y;
                last_z = z;

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}