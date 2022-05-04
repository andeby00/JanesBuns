package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad22spring.janesbuns.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //Widgets
    private TextView textViewRegister;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Set up widgets
        textViewRegister = findViewById(R.id.registerLoginTextView);
        editTextEmail = findViewById(R.id.emailLoginEditText);
        editTextPassword = findViewById(R.id.passwordLoginEditText);
        buttonLogin = findViewById(R.id.loginLoginMain);

        //Onclick listeners
        textViewRegister.setOnClickListener(this::onClickRegister);
        buttonLogin.setOnClickListener(this::onClickLogin);
    }

    private void onClickRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void onClickLogin(View view) {
        userLogin();
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("You have to enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Password can't be empty");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Password must be over 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(this, "Successfully logged in as " + email, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, ProfileActivity.class));
                    }
                    else {
                        Toast.makeText(this, "User has failed to login", Toast.LENGTH_LONG).show();
                    }
                });
    }
}