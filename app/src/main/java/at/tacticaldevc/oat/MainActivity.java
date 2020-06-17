package at.tacticaldevc.oat;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import at.tacticaldevc.oat.listeners.LockDownScreenStateReceiver;
import at.tacticaldevc.oat.ui.FeaturePage.FeaturesPageActivity;
import at.tacticaldevc.oat.ui.PasswordSettings.PasswordSettingsActivity;
import at.tacticaldevc.oat.ui.TrustedNumbers.TrustedNumbersActivity;
import at.tacticaldevc.oat.utils.Perms;

public class MainActivity extends AppCompatActivity {

    private Button trustedNumbersButton;
    private Button featurePageButton;
    private Button passwordSettingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        trustedNumbersButton = findViewById(R.id.trusted_numbers_button);
        trustedNumbersButton.setOnClickListener(cl -> openActivityTrustedNumbers());

        featurePageButton = findViewById(R.id.feature_page_button);
        featurePageButton.setOnClickListener(cl -> openActivityFeaturePage());

        passwordSettingsButton = findViewById(R.id.password_settings_button);
        passwordSettingsButton.setOnClickListener(cl -> openActivityPasswordSettings());

        //Permission management
        Perms.loadPermissionStates(this);
        Perms.requestAllPermissions(this, this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(new LockDownScreenStateReceiver(), filter);
    }

    public void openActivityTrustedNumbers(){
        Intent intent = new Intent(this, TrustedNumbersActivity.class);
        startActivity(intent);
    }

    public void openActivityFeaturePage(){
        Intent intent = new Intent(this, FeaturesPageActivity.class);
        startActivity(intent);
    }

    public void openActivityPasswordSettings(){
        Intent intent = new Intent(this, PasswordSettingsActivity.class);
        startActivity(intent);
    }

}
