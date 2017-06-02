package org.pltw.examples.hiit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Vikas A., Tudor C., Michael S., Eric W. on 05 25, 2017.
 */
public class TimerFragment extends Fragment {
    final private int TICK_INTERVAL = 10;
    private Button mStartButton;
    private TextView mTime; // Time left on the timer
    private TextView mStatus; // Whether resting or exercising
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


        // Instantiate the fields
        mTime = (TextView) rootView.findViewById(R.id.timer);
        mStatus = (TextView) rootView.findViewById(R.id.status);

        mStartButton = (Button) rootView.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimer = new Timer(reps, timeExercising, timeResting);
                mTime.setText(getSecondsLeft());
                mStatus.setText("Exercising");

                long totalTime = (timeExercising + timeResting) * reps * 1000; // Total amount of time the timer will run for in milliseconds
                mCountdownTimer = new CountDownTimer(totalTime, TICK_INTERVAL) {
                    @Override
                    public void onTick(long l) {
                        if (mTimer.isTimeOut())
                        {
                            mTimer.switchStatus();
                            mStatus.setText(mTimer.getStatus());
                        }
                        mTime.setText(getSecondsLeft());
                    }

                    @Override
                    public void onFinish() {
                        mTimer.stop();
                    }
                }.start();
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
        return Double.toString(mTimer.getTimeLeft()  / 1000.0);
    }
}
