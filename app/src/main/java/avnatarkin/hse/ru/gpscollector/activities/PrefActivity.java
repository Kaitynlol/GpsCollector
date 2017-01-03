package avnatarkin.hse.ru.gpscollector.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import avnatarkin.hse.ru.gpscollector.fragments.SettingsFragment;

public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }
}
