package avnatarkin.hse.ru.gpscollector.activities;

import android.preference.PreferenceActivity;

import java.util.List;

import avnatarkin.hse.ru.gpscollector.R;

public class PrefActivity extends PreferenceActivity {

    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }
}
