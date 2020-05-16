package at.tacticaldevc.oat.ui.PasswordSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import at.tacticaldevc.oat.R;

public class PasswordSettingsActivity extends AppCompatActivity {

    private Button ChangePwButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_settings);

        initButton();
    }

    private void initButton(){
        ChangePwButton = findViewById(R.id.password_settings_button);
        ChangePwButton.setOnClickListener(cl -> openDialog());
    }

    private void openDialog(){
        ChangePasswordDialog pwdia = new ChangePasswordDialog();
        pwdia.show(getSupportFragmentManager(), "Change Password Dialog");
    }
}
