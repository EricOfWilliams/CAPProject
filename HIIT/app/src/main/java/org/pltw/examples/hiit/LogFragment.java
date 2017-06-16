package org.pltw.examples.hiit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Eric W. on 6/12/2017.
 */
public class LogFragment extends Fragment {
    private TextView mList; // Displays the list of text

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Instantiate the rootview
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_log, container, false);

        // Instantiate mList
        mList = (TextView) rootView.findViewById(R.id.dataView);

        // Prep the shared preferences
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Read the data from the stored array
        int size = sharedPref.getInt(getString(R.string.data_array_size), 0);
        String[] dataArray = new String[size];
        for (int i = 0; i < size; i++)
        {
            dataArray[i] = sharedPref.getString("array_" + i, null);
        }

        // Put the data into the text view, starting with the most recent value, going to oldest
        String data = dataArray[size - 1];
        for (int i = size - 2; i >= 0; i--) // Go backwards through the array
        {
            if (dataArray[i] != null) { // Don't display any null values
                data = data + "\n" + dataArray[i]; // Format it into a list w/ line breaks
            }
        }

        mList.setText(data); // Display the data

        return rootView;
    }
}
