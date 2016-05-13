package br.com.motogp.guidao;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Keyboard keyboard;
    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration;

    Handler handler;
    boolean flag = false;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //sm = (SensorManager)getSystemService (SENSOR_SERVICE);
        //accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sm.registerListener(this, accelerometer, 1000000);

        acceleration = (TextView)findViewById(R.id.acceleration);

        handler = new Handler();
        mSensorManager = (SensorManager) getApplication().getSystemService(
                Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private final Runnable processSensors = new Runnable() {
        @Override
        public void run() {
            // Do work with the sensor values.

            flag = true;
            // The Runnable is posted to run again here:
            handler.postDelayed(this, 500);
        }
    };

    @Override
    public void onPause() {
        handler.removeCallbacks(processSensors);

        super.onPause();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (flag) {
            acceleration.setText("X: " + event.values[0] +
                    "\nY:" + event.values[1] +
                    "\nZ:" + event.values[2]);

            new ApiGuidao().setPosicao((int) event.values[1]);
            //Keyboard.EDGE_LEFT);
            //Y de -5 até 5 (esquerda e direita)
            flag = false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        handler.post(processSensors);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
