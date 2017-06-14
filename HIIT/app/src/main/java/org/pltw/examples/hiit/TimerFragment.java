package org.pltw.examples.hiit;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.media.SoundPool;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Vikas A., Tudor C., Michael S., Eric W. on 05 25, 2017.
 */
public class TimerFragment extends Fragment {
    final private int TICK_INTERVAL = 100;
    private Button mStartButton;
    private TextView mTime; // Time left on the timer
    private TextView mStatus; // Whether resting or exercising
    private TextView mReps; // Amount of reps
    private Timer mTimer = new Timer(0,0,0);
    private CountDownTimer mCountdownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Instantiate the rootview
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        // Get the values stored
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final int reps = sharedPref.getInt(getString(R.string.saved_reps), 0);
        final int timeExercising = sharedPref.getInt(getString(R.string.saved_time_exercising), 0);
        final int timeResting = sharedPref.getInt(getString(R.string.saved_time_resting), 0);
        final String exercise = sharedPref.getString(getString(R.string.exercise), "Pushups");
        final boolean soundEnabled = sharedPref.getBoolean(getString(R.string.soundEnabled), true);

        /*
         * Timer stuff
         */
        long totalTime = (timeExercising + timeResting) * reps * 1000; // Total amount of time the timer will run for in milliseconds

        // Countdown listener, runs necessary methods on the timer
        mCountdownTimer = new CountDownTimer(totalTime, TICK_INTERVAL)
        {
            public int repNumber = 1;

            @Override
            public void onTick(long l)
            {
                // Switching between exercising and resting
                if (mTimer.isTimeOut())
                {
                    mTimer.switchStatus();
                    mStatus.setText(mTimer.getStatus());

                    // Make a beep sound
                    if (soundEnabled)
                    {
                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Increment the reps
                    if (!mTimer.isResting())
                    {
                        repNumber++;
                    }

                }

                // Update the counters
                mTime.setText(getSecondsLeft());
                mReps.setText("Rep #" + repNumber);
                mTimer.setIterations(repNumber);
            }

            @Override
            public void onFinish()
            {
                mTimer.stop();
                mStatus.setText("Finished!");
                storeString(exercise, repNumber);
            }

            public int getRepNumber()
            {
                return repNumber;
            }
        };

        // Instantiate the fields
        mTime = (TextView) rootView.findViewById(R.id.timer);
        mStatus = (TextView) rootView.findViewById(R.id.status);
        mReps = (TextView) rootView.findViewById(R.id.reps);

        mStartButton = (Button) rootView.findViewById(R.id.startButton);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimer = new Timer(reps, timeExercising, timeResting);
                mTime.setText(getSecondsLeft());
                mStatus.setText("Exercising");
                mReps.setText("Rep #1");

                mCountdownTimer.start();

                // Switch to the stop button
                mStartButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                mStartButton.setText("Oh God, Kill Me Now");
                mStartButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view)
                    {
                        storeString(exercise, mTimer.getIterations());
                        mCountdownTimer.cancel();
                        mStatus.setText("Stopped");
                    }
                });
            }
        });

        return rootView;
    }

    /*
     * Returns the amount of time left on the timer, as a string
     * Formatted as seconds left
     */
    private String getSecondsLeft()
    {
        return Integer.toString(mTimer.getTimeLeft() / 1000) + "s";
    }

    /*
     * Stores the given exercise and reps
     */
    private void storeString(String exercise, int reps)
    {
        // Get the date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateString = dateFormat.format(date);

        // Concatenate the data
        String string = reps + " rep(s) of " + exercise + " at " + dateString;

        // Store the string
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Read the data from the stored array
        int size = sharedPref.getInt(getString(R.string.data_array_size), 0);
        String[] dataArray = new String[size];
        for(int i=0; i < size; i++)
        {
            dataArray[i] = sharedPref.getString("array_" + i, null);
        }

        // Convert the array to arraylist
        List<String> dataList = new ArrayList<String>();
        for (String entry : dataArray) {
            dataList.add(entry);
        }

        // Add the string to the array
        dataList.add(string);

        // Convert the updated arraylist to array
        dataArray = new String[size+1];
        for (int i = 0; i < size + 1; i++)
        {
            dataArray[i] = dataList.get(i);
        }

        // Store the array
        editor.putInt(getString(R.string.data_array_size), dataArray.length);
        for(int i=0; i < dataArray.length; i++) {
            editor.putString("array_" + i, dataArray[i]);
        }

        editor.commit();
    }
}
