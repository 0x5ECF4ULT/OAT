package at.tacticaldevc.oat.ui.FeaturePage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import at.tacticaldevc.oat.R;
import at.tacticaldevc.oat.ui.FeaturePage.InstantPhoto.InstantPhotoActivity;
import at.tacticaldevc.oat.ui.FeaturePage.PhotoTrap.PhotoTrapActivity;
import at.tacticaldevc.oat.ui.FeaturePage.Tracking.TrackingActivity;

public class FeaturesPageActivity extends AppCompatActivity {

    private Button photoTrap;
    private Button instantPhoto;
    private Button tracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features_page);

        photoTrap = findViewById(R.id.feature_settings_phototrap);
        photoTrap.setOnClickListener(cl -> openActivityPhotoTrap());

        instantPhoto = findViewById(R.id.feature_settings_instant_photo);
        instantPhoto.setOnClickListener(cl -> openActivityInstantPhoto());

        tracking = findViewById(R.id.feature_settings_tracking);
        tracking.setOnClickListener(cl -> openActivityTracking());
    }

    public void openActivityPhotoTrap(){
        Intent intent = new Intent(this, PhotoTrapActivity.class);
        startActivity(intent);
    }

    public void openActivityInstantPhoto(){
        Intent intent = new Intent(this, InstantPhotoActivity.class);
        startActivity(intent);
    }

    public void openActivityTracking(){
        Intent intent = new Intent(this, TrackingActivity.class);
        startActivity(intent);
    }
}
