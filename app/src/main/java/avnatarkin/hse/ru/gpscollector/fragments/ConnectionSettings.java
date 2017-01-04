package avnatarkin.hse.ru.gpscollector.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import avnatarkin.hse.ru.gpscollector.R;

/**
 * Created by sanjar on 03.01.17.
 */

public class ConnectionSettings extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_conn);
    }
}
