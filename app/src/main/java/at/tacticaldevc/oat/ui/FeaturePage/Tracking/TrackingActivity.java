package at.tacticaldevc.oat.ui.FeaturePage.Tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acceptCondition();
            }
        });

        activate = findViewById(R.id.tracking_switch);
        activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activateFeature();
            }
        });
        activate.setClickable(false);

        featureActiveCheck();
        featureConditionCheck();
    }

    public void featureConditionCheck(){
        if(Prefs.fetchConditionAccepted(this, getString(R.string.oat_features_key_fetch_gps_position))){
            accept.setChecked(true);
        }else{
            accept.setChecked(false);
        }
    }

    public void featureActiveCheck(){
        if(Prefs.fetchFeatureEnabledStatus(this,getString(R.string.oat_features_key_fetch_gps_position))) {
            activate.setChecked(true);
            activate.setClickable(true);
        }else{
            activate.setChecked(false);
        }
    }

    public void acceptCondition(){
        if(accept.isChecked()){
            Prefs.saveConditionAccepted(this, getString(R.string.oat_features_key_fetch_gps_position), true);
            activate.setClickable(true);
        }else{
            Prefs.saveConditionAccepted(this, getString(R.string.oat_features_key_fetch_gps_position), false);
            activate.setChecked(false);
            activate.setClickable(false);
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
