package org.pltw.examples.hiit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Michael Sato on 6/16/2017.
 */
public class MovementDetectorFragment extends Fragment implements SensorEventListener {
    SensorManager sm;
    TextView tv;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    Button b;
    boolean incrementOneStep = true;
    boolean incrementOneKB = true;
    boolean trackToggle = false;
    int stepCount = 0;
    int kbCount = 0;
    int stopDelay = 0;
    boolean onlyOnce = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview = inflater.inflate(R.layout.fragment_movement_detector, container, false);
        tv=(TextView)rootview.findViewById(R.id.textView1);
        tv2=(TextView)rootview.findViewById(R.id.textView2);
        tv3=(TextView)rootview.findViewById(R.id.textView3);
        tv4=(TextView)rootview.findViewById(R.id.textView4);
        tv5=(TextView)rootview.findViewById(R.id.textView5);
        b=(Button)rootview.findViewById(R.id.trackButton);


        //Code executed when "Begin Motion Tracking" button is pressed
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(trackToggle == true) {
                    b.setText("Begin Motion Tracking");
                    stepCount = 0;
                    kbCount = 0;
                    trackToggle = false;
                    tv.setText("MoveText");
                    tv2.setText("vMoveText");
                    tv3.setText("StepText");
                    tv4.setText("hMoveText");
                    tv5.setText("kbText");
                }
                else
                {
                    b.setText("Stop Motion Tracking");
                    trackToggle = true;
                }
            }
        });
        //Get sensor service
        sm=(SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        //Register listener: registerListener(listener, Sensor, sensor delay)
        sm.registerListener(this,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        return rootview;
    }
    //Override abstract method 'onAccuracyChanged(Sensor, int)' from SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}
    //This method is called when movement is detected
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(trackToggle == true) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //get x, y, z values
                float value[] = event.values;
                float x = value[0];
                float y = value[1];
                float z = value[2];
                float movement = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH *
                        SensorManager.GRAVITY_EARTH);
                float vMovement = (y * y) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
                float xMovement = (x * x) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
                float zMovement = (z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

                //The value compared to 'movement' reflects the sensitivity of movement at which code activates. Lower = more sensitive.
                //Using a value lower than ~0.94 results in the if statement passing even when the device isn't moving. Don't do that.

                //Detects general movement- in any direction
                if (movement >= 1.10) {
                    stopDelay = 0;
                    if(onlyOnce == false) {
                        tv.setText("Moving");
                        onlyOnce = true;
                    }
                } else {
                    stopDelay++;

                    /*Counts up 'stopDelay' while device is not moving, then changes the displayed text to say "No movement detected" after it reaches a threshold,
                    such that the text doesn't change too rapidly*/

                    if (stopDelay >= 150) {
                        if(onlyOnce == true) {
                            tv.setText("No movement detected");
                            onlyOnce = false;
                        }
                    }
                }
                //Intended for use with a step counter- only tracks vertical movement
                if (vMovement >= 1.25) {
                    if (incrementOneStep) {
                        stepCount += 1;
                    }
                    incrementOneStep = false;
                    tv2.setText("vMoving");
                    //For testing purposes; displays value for yMovement
                    //tv2.setText(String.valueOf(yMovement)
                    tv3.setText("Steps: " + stepCount);
                } else {
                    incrementOneStep = true;
                    tv2.setText("No vmovement detected");
                    //For testing purposes; displays value for yMovement
                    //tv2.setText(String.valueOf(yMovement)
                }
                //Experimental: intended to detect the motion of a kettlebell swing- detects quick horizontal (both x and z) movements
                if (xMovement >= 1.10 || zMovement >= 1.10) {
                    if (incrementOneKB) {
                        kbCount += 1;
                    }
                    incrementOneKB = false;
                    tv4.setText("hMoving");
                    //For testing purposes; displays values for xMovement and zMovement
                    //tv4.setText(String.valueOf(xMovement) + ", " + String.valueOf(zMovement));
                    tv5.setText("Swings: " + kbCount / 2);
                } else {
                    incrementOneKB = true;
                    tv4.setText("No hmovement dectected");
                    //For testing purposes; displays values for xMovement and zMovement
                    //tv4.setText(String.valueOf(xMovement) + ", " + String.valueOf(zMovement));
                }
            }
        }
    }
}
