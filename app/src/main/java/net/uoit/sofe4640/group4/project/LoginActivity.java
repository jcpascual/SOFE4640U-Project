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

public class LoginActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarLogin);

        ProjectApplication projectApplication = (ProjectApplication)getApplication();

        // Grab the database helper from our ProjectApplication instance.
        AppDatabaseHelper dbHelper = projectApplication.getDatabaseHelper();

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUser user = dbHelper.getAppUserWithUsernameAndPassword(binding.editTextUsername.getText().toString(), binding.editTextPassword.getText().toString());

                if (user != null) {
                    projectApplication.setCurrentUser(user);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Build an AlertDialog with the provided message ID.
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.error_common_title)
                            .setMessage(R.string.error_login_fail_text)
                            .setPositiveButton(R.string.error_common_button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    builder.show();
                }
            }
        });
    }

}