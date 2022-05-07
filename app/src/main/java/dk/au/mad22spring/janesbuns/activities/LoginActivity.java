package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad22spring.janesbuns.LoginViewModel;
import dk.au.mad22spring.janesbuns.MainViewModel;
import dk.au.mad22spring.janesbuns.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    LoginViewModel vm;

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


        vm = new ViewModelProvider(this).get(LoginViewModel.class);
        vm.initializeVM();
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
            editTextEmail.setError("" + R.string.invalidMail);
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("" + R.string.invalidPass);
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("" + R.string.invalidPass2);
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(this, "" + R.string.toastLogin + email, Toast.LENGTH_LONG).show();
                        vm.updateCurrentUser(task.getResult().getUser().getUid());
                        //startActivity(new Intent(this, ProfileActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "" + R.string.toastLoginF, Toast.LENGTH_LONG).show();
                    }
                });
    }
}