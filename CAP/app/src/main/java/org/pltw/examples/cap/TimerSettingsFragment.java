package org.pltw.examples.cap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Vikas A., Tudor C., Michael S., Eric W. on 05 19, 2017.
 */
public class TimerSettingsFragment extends android.support.v4.app.Fragment
{
    // Fields
    private EditText mEditReps;
    private EditText mEditTimeExercising;
    private EditText mEditTimeResting;

    private Button mSaveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Instantiate the rootview
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_timer_settings, container, false);

        // Instantiate the fields
        mEditReps = (EditText) rootView.findViewById(R.id.mEditReps);
        mEditTimeExercising = (EditText) rootView.findViewById(R.id.mEditTimeExercising);
        mEditTimeResting = (EditText) rootView.findViewById(R.id.mEditTimeResting);

        mSaveButton = (Button) rootView.findViewById(R.id.mSaveButton);

        final SharedPreferences sharedPref = getActivity().getSharedPreferences("com.pltw.examples.cap.TimerSettings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        /*mEditReps.setText(sharedPref.getInt(R.integer.saved_reps, 10));

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt(R.integer.saved_reps, mEditReps.getText());
                editor.commit();
            }
        });*/



        return rootView;
    }
}
