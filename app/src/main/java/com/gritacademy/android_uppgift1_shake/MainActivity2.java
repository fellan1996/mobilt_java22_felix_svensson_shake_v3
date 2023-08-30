package com.gritacademy.android_uppgift1_shake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private long lastUpdateForValues = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 400;
    private FragmentManager fm;
    private shakyFragment shakyFragmentInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button activityBtn = findViewById(R.id.activityBtn);

        fm = getSupportFragmentManager();
        shakyFragmentInstance = new shakyFragment();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        fm.beginTransaction()
                .add(R.id.frameLayout, BlankFragment.class, null) // gets the first animations
                .commit();

        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Jadååå", Toast.LENGTH_LONG).show();
                Intent A1 = new Intent(MainActivity2.this, MainActivity.class);
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

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            TextView sensorDataInfo = findViewById(R.id.sensordataInfo);

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
//                    Toast.makeText(MainActivity2.this, "Jadååå", Toast.LENGTH_LONG).show();
                    fm.beginTransaction()
                            .replace(R.id.frameLayout, shakyFragmentInstance, null) // gets the first animations
                            .commit();
//                    ______________Jag fick inte koden undertill till att fungera________________
//                    shakyFragmentInstance.updateText("Latest score: " + Math.round(speed));

                } else {
                    fm.beginTransaction()
                            .replace(R.id.frameLayout, BlankFragment.class, null) // gets the first animations
                            .commit();

                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
            if((curTime - lastUpdateForValues) > 1000) {
                lastUpdateForValues = curTime;
                float lessDecimalsX = Math.round(last_x * 10000);
                float lessDecimalsY = Math.round(last_y * 10000);
                float lessDecimalsZ = Math.round(last_z * 10000);
                sensorDataInfo.setText(" X: " + lessDecimalsX / 10000 + "   Y: " + lessDecimalsY / 10000 + "   Z: " + lessDecimalsZ / 10000);

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}