package org.pltw.examples.hiit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

/**
 * Created by Vikas A., Tudor C., Michael S., Eric W. on 05 19, 2017.
 */
public class TimerSettingsFragment extends android.support.v4.app.Fragment
{
    // Fields
    private EditText mEditReps;
    private EditText mEditTimeExercising;
    private EditText mEditTimeResting;
    private EditText mEditExercise;
    private ToggleButton mSoundSwitch;

    private Button mSaveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Instantiate the rootview
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_timer_settings, container, false);

        // Reading stuff from memory
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int reps = sharedPref.getInt(getString(R.string.saved_reps), 0);
        int timeExercising = sharedPref.getInt(getString(R.string.saved_time_exercising), 0);
        int timeResting = sharedPref.getInt(getString(R.string.saved_time_resting), 0);
        String exercise = sharedPref.getString(getString(R.string.exercise), "");
        boolean soundEnabled = sharedPref.getBoolean(getString(R.string.soundEnabled), true);

        // Instantiate the fields
        mEditReps = (EditText) rootView.findViewById(R.id.mEditReps);
        mEditTimeExercising = (EditText) rootView.findViewById(R.id.mEditTimeExercising);
        mEditTimeResting = (EditText) rootView.findViewById(R.id.mEditTimeResting);
        mEditExercise = (EditText) rootView.findViewById(R.id.editExercise);
        mSoundSwitch = (ToggleButton) rootView.findViewById(R.id.soundButton);

        mSaveButton = (Button) rootView.findViewById(R.id.mSaveButton);

        // Put the previous values into the fields
        mEditReps.setText(Integer.toString(reps));
        mEditTimeExercising.setText(Integer.toString(timeExercising));
        mEditTimeResting.setText(Integer.toString(timeResting));
        mEditExercise.setText(exercise);
        mSoundSwitch.setChecked(soundEnabled);

        // Create a listener
        final SharedPreferences.Editor editor = sharedPref.edit();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read values from the text fields
                int reps = Integer.valueOf(mEditReps.getText().toString());
                int exercising = Integer.valueOf(mEditTimeExercising.getText().toString());
                int resting = Integer.valueOf(mEditTimeResting.getText().toString());
                String exercise = mEditExercise.getText().toString();
                boolean soundEnabled = mSoundSwitch.isChecked();

                // Store text field values
                editor.putInt(getString(R.string.saved_reps), reps);
                editor.putInt(getString(R.string.saved_time_exercising), exercising);
                editor.putInt(getString(R.string.saved_time_resting), resting);
                editor.putString(getString(R.string.exercise), exercise);
                editor.putBoolean(getString(R.string.soundEnabled), soundEnabled);

                // Save the values
                editor.commit();
            }
        });

        return rootView;
    }
}
