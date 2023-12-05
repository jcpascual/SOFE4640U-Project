package net.uoit.sofe4640.group4.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.uoit.sofe4640.group4.project.database.AppDatabaseHelper;
import net.uoit.sofe4640.group4.project.database.AppUser;
import net.uoit.sofe4640.group4.project.databinding.ActivityLoginBinding;
import net.uoit.sofe4640.group4.project.databinding.ActivityMainBinding;
import net.uoit.sofe4640.group4.project.databinding.ActivityRegisterUserBinding;

public class RegisterUserActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityRegisterUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarRegisterUser);

        getSupportActionBar().setTitle(R.string.register_title);

        ProjectApplication projectApplication = (ProjectApplication)getApplication();

        // Grab the database helper from our ProjectApplication instance.
        AppDatabaseHelper dbHelper = projectApplication.getDatabaseHelper();

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.editTextUsernameRegister.getText().toString();
                String password = binding.editTextPasswordRegister.getText().toString();

                AppUser user = dbHelper.getAppUserWithUsername(username);

                if (user != null) {
                    // The user already exists. Show an error.
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.error_register_fail_title)
                            .setMessage(R.string.error_register_fail_text)
                            .setPositiveButton(R.string.error_common_button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    builder.show();
                } else {
                    // Create a new AppUser instance.
                    user = new AppUser();
                    user.username = username;
                    user.password = password;

                    // Add it to the database.
                    dbHelper.addOrUpdateAppUser(user);

                    // Notify the user that their account was created.
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.alert_register_success_title)
                            .setMessage(R.string.alert_register_success_text)
                            .setPositiveButton(R.string.error_common_button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                    builder.show();
                }
            }
        });
    }

}