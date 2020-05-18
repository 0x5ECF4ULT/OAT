package at.tacticaldevc.oat.ui.FeaturePage.InstantPhoto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.utils.Prefs;

public class InstantPhotoActivity extends AppCompatActivity {

    private CheckBox accept;
    private Switch activate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_photo);

        accept = findViewById(R.id.instant_photo_checkbox);
        accept.setOnClickListener(cl -> conditionsAcceptedCheck());

        activate = findViewById(R.id.instant_photo_switch);
        activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activateFeature();
                //featureActiveCheck();
            }
        });
        activate.setClickable(false);

        featureActiveCheck();
    }

    public void featureActiveCheck(){
        if(Prefs.fetchFeatureEnabledStatus(this,getString(R.string.oat_features_key_trigger_instant_photo))) {
            accept.setChecked(true);
            activate.setChecked(true);
            activate.setClickable(true);
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
            Prefs.saveFeatureEnabledStatus(this, getString(R.string.oat_features_key_trigger_instant_photo), true);
        }else{
            Prefs.saveFeatureEnabledStatus(this, getString(R.string.oat_features_key_trigger_instant_photo), false);
        }
    }
}
