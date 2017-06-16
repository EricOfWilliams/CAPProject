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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Eric W. on 05 25, 2017.
 */
public class TimerFragment extends Fragment {
    final private int TICK_INTERVAL = 100;
    private Button mButton; // Button to start the timer; changes to be the tap out button
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

        // Get the stored settings
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final int reps = sharedPref.getInt(getString(R.string.saved_reps), 0); // Amount of reps to do
        final int timeExercising = sharedPref.getInt(getString(R.string.saved_time_exercising), 0); // Time spent exercising
        final int timeResting = sharedPref.getInt(getString(R.string.saved_time_resting), 0); // Time spent resting
        final String exercise = sharedPref.getString(getString(R.string.exercise), "Pushups"); // Which exercise to be doing; used in storing
        final boolean soundEnabled = sharedPref.getBoolean(getString(R.string.soundEnabled), true); // Whether or not the sound is enabled

        /*
         * Timer stuff
         */
        long totalTime = (timeExercising + timeResting) * reps * 1000; // Total amount of time the timer will run for in milliseconds

        // Countdown listener, runs necessary methods on the timer
        mCountdownTimer = new CountDownTimer(totalTime, TICK_INTERVAL)
        {
            // Which rep the counter is on
            public int repNumber = 1;

            // Each tick
            @Override
            public void onTick(long l)
            {
                // Switching between exercising and resting
                if (mTimer.isTimeOut())
                {
                    mTimer.switchStatus();
                    mStatus.setText(mTimer.getStatus());

                    // Make a beep sound if sound enabled
                    if (soundEnabled)
                    {
                        /*
                         * Took this from: https://stackoverflow.com/questions/6462105/how-do-i-access-androids-default-beep-sound
                         * Plays the system's default notification sound
                         */
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
                mTime.setText(getSecondsLeft()); // Amount of seconds left on the timer
                mReps.setText("Rep #" + repNumber);
                mTimer.setIterations(repNumber);
            }

            // When finished
            @Override
            public void onFinish()
            {

                mTimer.stop();
                mStatus.setText("Finished!");
                storeString(exercise, repNumber); // Store the info on this exercise
            }
        };

        // Instantiate the fields
        mTime = (TextView) rootView.findViewById(R.id.timer);
        mStatus = (TextView) rootView.findViewById(R.id.status);
        mReps = (TextView) rootView.findViewById(R.id.reps);

        // Create the start button
        mButton = (Button) rootView.findViewById(R.id.startButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start off the timer
                mTimer = new Timer(reps, timeExercising, timeResting); // Create the timer
                mTime.setText(getSecondsLeft()); // Display the time left on the timer
                mStatus.setText("Exercising"); // Start w/ exercising
                mReps.setText("Rep #1"); // Start at rep 1

                mCountdownTimer.start(); // Start the timer

                // Switch to the stop button
                mButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                mButton.setText("Oh God, Kill Me Now");
                mButton.setOnClickListener(new View.OnClickListener(){
                    // Stop the timer prematurely
                    @Override
                    public void onClick(View view)
                    {
                        storeString(exercise, mTimer.getIterations()); // store the data of this workout
                        mCountdownTimer.cancel(); // Kill the timer
                        mStatus.setText("Stopped"); // Display that the timer was stopped, not finished
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
        // Get the date and time
        // Taken from: https://stackoverflow.com/questions/2271131/display-the-current-time-and-date-in-an-android-application
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateString = dateFormat.format(date); // Put the date into a string

        // Concatenate the data
        String string = reps + " rep(s) of " + exercise + " at " + dateString;

        // Store the string into our stored array
        // Prepare the shared preferences
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Read the data from the stored array
        // Taken from: http://www.sherif.mobi/2012/05/string-arrays-and-object-arrays-in.html
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

        // Commit the array
        editor.commit();
    }
}
