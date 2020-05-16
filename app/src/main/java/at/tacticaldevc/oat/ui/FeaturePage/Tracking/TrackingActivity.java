package at.tacticaldevc.oat.ui.FeaturePage.Tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Switch;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Prefs;

public class TrackingActivity extends AppCompatActivity {

    private CheckBox accept;
    private Switch activate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        accept = findViewById(R.id.tracking_checkbox);
        accept.setOnClickListener(cl -> conditionsAcceptedCheck());

        activate = findViewById(R.id.tracking_switch);
        activate.setOnClickListener(cl -> activateFeature());
        activate.setClickable(false);

        featureActiveCheck();
    }

    public void featureActiveCheck(){
        if(Prefs.fetchFeatureEnabledStatus(this,getString(R.string.oat_features_key_trigger_photo_trap))) {
            accept.setChecked(true);
            activate.setChecked(true);
        }else{
            accept.setChecked(false);
            activate.setChecked(false);
        }
    }

    public void conditionsAcceptedCheck(){
        if(!accept.isChecked()){
            activate.setChecked(false);
            activate.setClickable(false);
        }else{
            activate.setClickable(true);
        }
    }

    public void activateFeature(){
        if(activate.isChecked()){
            Prefs.saveFeatureEnabledStatus(this, getString(R.string.oat_features_key_fetch_gps_position), true);
        }else{
            Prefs.saveFeatureEnabledStatus(this, getString(R.string.oat_features_key_fetch_gps_position), false);
        }
    }
}
