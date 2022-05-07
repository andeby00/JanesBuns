package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    private TextView textViewTitle;
    private Button buttonRegisterUser;
    private EditText editTextFullName, editTextPhone, editTextEmail, editTextPassword, editTextAddress, editTextCity, editTextPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textViewTitle = findViewById(R.id.titleRegisterTextView);
        buttonRegisterUser = findViewById(R.id.registerButtonRegister);
        editTextFullName = findViewById(R.id.nameRegisterEditText);
        editTextPhone = findViewById(R.id.phoneRegisterEditText);
        editTextEmail = findViewById(R.id.emailRegisterEditText);
        editTextPassword = findViewById(R.id.passwordRegisterEditText);
        editTextAddress = findViewById(R.id.addressRegisterEditText);
        editTextCity = findViewById(R.id.cityRegisterEditText);
        editTextPostalCode = findViewById(R.id.postalCodeRegisterEditText);

        textViewTitle.setOnClickListener(this::onClickTitle);
        buttonRegisterUser.setOnClickListener(view -> registerUser());

    }

    private void onClickTitle(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        int postalCode = Integer.parseInt(editTextPostalCode.getText().toString().trim());

        if(fullName.isEmpty()) {
            editTextFullName.setError("Name can't be empty");
            editTextFullName.requestFocus();
            return;
        }

        if(phone.isEmpty()) {
            editTextPhone.setError("Phone can't be empty");
            editTextPhone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone).matches()) {
            editTextPhone.setError("Phone number isn't valid");
            editTextPhone.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editTextEmail.setError("Email can't be empty");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email isn't valid");
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

        if(address.isEmpty()) {
            editTextAddress.setError("Address can't be empty");
            editTextAddress.requestFocus();
            return;
        }

        if(city.isEmpty()) {
            editTextCity.setError("City can't be empty");
            editTextCity.requestFocus();
            return;
        }

        if(postalCode <= 0) {
            editTextPostalCode.setError("Address can't be empty");
            editTextPostalCode.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        User user = new User(fullName, phone, email, address, city, postalCode, false);
                        String uid = task.getResult().getUser().getUid();
                        db.collection("users")
                                .document(uid).set(user)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + uid);
                                    Toast.makeText(RegisterActivity.this, "" + R.string.toastRegister, Toast.LENGTH_LONG).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "" + R.string.toastRegisterF, Toast.LENGTH_LONG).show();
                                    Log.w(TAG, "Error adding document", e);
                                });

                        /*FirebaseDatabase.getInstance("https://janes--buns-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User has been registered", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "User has failed to register", Toast.LENGTH_LONG).show();
                            }
                        });*/
                    }
                    else {
                        Log.w(TAG, "registerUser: " + task.getException());
                        Toast.makeText(RegisterActivity.this, "User has failed to register", Toast.LENGTH_LONG).show();
                    }
                });
    }
}