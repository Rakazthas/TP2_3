package com.example.accel;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager snsManager;
    private Sensor aSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean aSensorDisp = false;

        snsManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aSensor = snsManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.current_view);

        if (aSensor != null){
            aSensorDisp = snsManager.registerListener((SensorEventListener) this, aSensor, SensorManager.SENSOR_DELAY_GAME);
        }else{
            TextView abs = new TextView(this);
            abs.setText("Pas d'accelerometre detecte, fonctionalite desactivee");
            ll.addView(abs);
        }

        if(!aSensorDisp){
            snsManager.unregisterListener((SensorEventListener) this, snsManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            TextView abs = new TextView(this);
            abs.setText("Accelerometre indisponible, fonctionalite desactivee");
            ll.addView(abs);
        }

        ll.setBackgroundColor(Color.BLACK);

        setContentView(ll);
    }

    @Override
    protected void onResume(){
        super.onResume();
        snsManager.registerListener((SensorEventListener) this, aSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause(){
        super.onPause();
        snsManager.unregisterListener((SensorEventListener) this, aSensor);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER :
                onAccelerometerChange(event);
                break;
            default:
                break;
        }
    }

    private void onAccelerometerChange(SensorEvent event) {
        View current;
        double sensorVal;

        current = findViewById(R.id.current_view);

        sensorVal = (event.values[0] + event.values[1] + event.values[2] - 9.81)/3;
        if (sensorVal < -0.5)
            current.setBackgroundColor(Color.GREEN);
        else if (sensorVal > 0.5)
            current.setBackgroundColor(Color.RED);
        else
            current.setBackgroundColor(Color.BLACK);

        setContentView(current);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Sensor", sensor.getType()+":"+accuracy);
    }
}