package avnatarkin.hse.ru.gpscollector.auth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import avnatarkin.hse.ru.gpscollector.R;
import avnatarkin.hse.ru.gpscollector.auth.fragments.AuthFragment;
import avnatarkin.hse.ru.gpscollector.main.MainActivity;
import avnatarkin.hse.ru.gpscollector.util.constants.Constants;

public class AuticationActivity extends AppCompatActivity implements AuthFragment.MainFragmentButtonsInterface {
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
                AuthFragment mainFragment = new AuthFragment();
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

    // Method from AuthFragment
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

    // Method from AuthFragment
    @Override
    public void onSignUpButtonClick(Fragment fragment) {
       startActivity(new Intent(this,LoggingActivity.class));
       // startActivity(new Intent(this,Sender.class));
    }

}
