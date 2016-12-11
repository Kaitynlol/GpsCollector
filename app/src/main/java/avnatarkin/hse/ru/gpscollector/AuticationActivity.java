package avnatarkin.hse.ru.gpscollector;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import avnatarkin.hse.ru.gpscollector.activities.LoggingActivity;
import avnatarkin.hse.ru.gpscollector.constants.Constants;
import avnatarkin.hse.ru.gpscollector.fragments.MainFragment;
import avnatarkin.hse.ru.gpscollector.activities.MainActivity;

public class AuticationActivity extends AppCompatActivity implements MainFragment.MainFragmentButtonsInterface {
    // We want to know if the user has logged in before
    private SharedPreferences mSharedPreferences;
    private boolean mUserFirstTime;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autication);
        mFragmentManager = getFragmentManager();
        // Get the preference to check if the user has logged in previously
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserFirstTime = mSharedPreferences.getBoolean(Constants.FIRST_TIME, true);

        if (savedInstanceState == null) {
            if (mUserFirstTime) {
                MainFragment mainFragment = new MainFragment();
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, mainFragment)
                        .commit();
            } else {
                Intent mainItent = new Intent(this, MainActivity.class);
                startActivity(mainItent);
                finish();
            }
        }
    }
    // Method from MainFragment
    @Override
    public void onLoginButtonClick(Fragment fragment) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("avnatarkin.hse.ru.gpscollector", Context.MODE_PRIVATE);
        boolean currentlyLogging = sharedPreferences.getBoolean("currentlyLogging",false);
        if(currentlyLogging) {
            startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
        }
        else {
            Toast.makeText(this, R.string.logging_empty, Toast.LENGTH_LONG).show();
        }


    }

    // Method from MainFragment
    @Override
    public void onSignUpButtonClick(Fragment fragment) {
       startActivity(new Intent(this,LoggingActivity.class));
       // startActivity(new Intent(this,Sender.class));
    }

}
