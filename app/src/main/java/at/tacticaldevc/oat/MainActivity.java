package at.tacticaldevc.oat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import at.tacticaldevc.oat.utils.Prefs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //testing
        Prefs.savePassword(getApplicationContext(), "password", "password");
        Prefs.saveTrustedContact(getApplicationContext(), "+15555215554", "Emulator Android 10");
        Prefs.saveTrustedContact(getApplicationContext(), "+15555215556", "Emulator Android 7");
        Prefs.saveFeatureEnabledStatus(getApplicationContext(), getApplicationContext().getString(R.string.oat_features_key_fetch_gps_position), true);
        Prefs.saveFeatureEnabledStatus(getApplicationContext(), getApplicationContext().getString(R.string.oat_features_key_lift_lockdown), true);
        Prefs.saveFeatureEnabledStatus(getApplicationContext(), getApplicationContext().getString(R.string.oat_features_key_trigger_lockdown), true);
        Prefs.saveFeatureEnabledStatus(getApplicationContext(), getApplicationContext().getString(R.string.oat_features_key_trigger_photo_trap), true);
    }

}
