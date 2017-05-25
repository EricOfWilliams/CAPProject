package org.pltw.examples.hiit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vikas A., Tudor C., Michael S., Eric W. on 05 23, 2017.
 */
public class ExerciseSelectionFragment extends android.support.v4.app.Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Instantiate the rootview
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_exercise_selection, container, false);


        return rootView;
    }
}
